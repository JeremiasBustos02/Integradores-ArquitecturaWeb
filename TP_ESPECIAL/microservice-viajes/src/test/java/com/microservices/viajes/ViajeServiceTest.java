package com.microservices.viajes;

// --- Imports de Spring y JUnit ---

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq; // Importante para "EN USO"
import static org.mockito.Mockito.*; // Para when, verify, never, etc.

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

// --- Imports de tus Clases ---
import com.microservices.viajes.clients.*;
import com.microservices.viajes.dto.request.EstadosFactura;
import com.microservices.viajes.dto.request.EstadosMonopatin;
import com.microservices.viajes.dto.request.FacturaRequestDTO;
import com.microservices.viajes.dto.response.*;
import com.microservices.viajes.entity.Viaje;
import com.microservices.viajes.exception.InvalidViajeException;
import com.microservices.viajes.mapper.PausaMapper;
import com.microservices.viajes.mapper.ViajeMapper;
import com.microservices.viajes.repository.ViajeRepository;
import com.microservices.viajes.service.implementacion.ViajeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class ViajeServiceTest {

    // --- Mocks (Los "actores" que simulan los otros servicios) ---
    @Mock
    private ViajeRepository viajeRepository;
    @Mock
    private ViajeMapper mapper;
    @Mock
    private PausaMapper pausaMapper; // Necesario para el enriquecimiento
    @Mock
    private ParadaClientRest paradaClient;
    @Mock
    private MonopatinClientRest monopatinClient;
    @Mock
    private UsuarioClientRest usuarioClient;
    @Mock
    private TarifaClientRest tarifaClient;
    @Mock
    private FacturacionClientRest facturaClient;

    // --- SUT (Subject Under Test - La clase que probamos) ---
    @InjectMocks
    private ViajeService viajeService;

    // --- IDs y DTOs de prueba reutilizables ---
    private final Long MONOPATIN_ID = 1L;
    private final Long USUARIO_ID = 1L;
    private final Long TARIFA_ID = 1L;
    private final Long PARADA_ORIGEN_ID = 10L;
    private final Long PARADA_DESTINO_ID = 20L;
    private final Long CUENTA_ID = 99L;
    private final String VIAJE_ID = "viaje-test-123";

    private MonopatinDTO monopatinDisponible;
    private MonopatinDTO monopatinEnUso;
    private ParadaDTO paradaOrigen;
    private ParadaDTO paradaDestino;
    private TarifaDTO tarifaEstandar;
    private Viaje viajeEnCurso;

    @BeforeEach
    void setUp() {
        // --- Configuración inicial para todos los tests ---

        // Mocks de DTOs
        monopatinDisponible = new MonopatinDTO(MONOPATIN_ID, -37.0, -59.0, "DISPONIBLE");
        monopatinEnUso = new MonopatinDTO(MONOPATIN_ID, -37.0, -59.0, "EN USO");
        paradaOrigen = new ParadaDTO(PARADA_ORIGEN_ID, "Parada Origen", -37.0, -59.0);
        paradaDestino = new ParadaDTO(PARADA_DESTINO_ID, "Parada Destino", -37.1, -59.1);
        tarifaEstandar = new TarifaDTO(TARIFA_ID, 10.0, 50.0, null, true, "Tarifa Base"); // $10 por minuto, $50 extra

        // Mock de un Viaje
        viajeEnCurso = new Viaje(MONOPATIN_ID, PARADA_ORIGEN_ID, USUARIO_ID, TARIFA_ID);
        viajeEnCurso.setId(VIAJE_ID);
        viajeEnCurso.setHoraInicio(LocalDateTime.now().minusMinutes(30)); // Empezó hace 30 min
        viajeEnCurso.setPausaExtensa(false);

        // Mocks de Mappers (para el enriquecimiento)
        when(mapper.toResponseDTO(any(Viaje.class))).thenAnswer(invocation -> {
            Viaje v = invocation.getArgument(0);
            return new ViajeResponseDTO(
                    v.getId(),                      // 1. id
                    v.getHoraInicio(),              // 2. horaInicio
                    v.getHoraFin(),                 // 3. horaFin
                    v.getDistanciaRecorrida(),      // 4. kmRecorridos
                    null,                           // 5. paradaInicio
                    null,                           // 6. paradaFin
                    v.isPausaExtensa(),             // 7. pausaExtensa (¡El que faltaba!)
                    v.getMonopatinId(),             // 8. monopatinId
                    v.getUsuarioId(),               // 9. usuarioId
                    v.getTarifaId(),                // 10. tarifaId
                    List.of()                       // 11. pausas
            );
        });
    }

    // --- TEST 1: Camino de error en Create (Falla de negocio) ---
    @Test
    void testCreateViaje_Fails_WhenUsuarioNoTieneSaldo() {
        // GIVEN: Parada OK, pero Usuario SIN saldo
        when(paradaClient.getParadaById(anyLong())).thenReturn(paradaOrigen);
        when(usuarioClient.saldoSuficiente(USUARIO_ID)).thenReturn(false); // ¡Sin saldo!

        // WHEN: Se intenta crear el viaje
        // THEN: Debería lanzar una excepción ANTES de tocar el monopatín o la BD
        InvalidViajeException exception = assertThrows(InvalidViajeException.class, () -> {
            viajeService.createViaje(MONOPATIN_ID, USUARIO_ID, TARIFA_ID, PARADA_ORIGEN_ID);
        });

        // VERIFY: Verificamos que el error es el correcto
        assertTrue(exception.getMessage().contains("El usuario no tiene fondo suficiente"));

        // VERIFY (El más importante): Verificamos que NUNCA se intentó reservar el monopatín ni guardar el viaje
        Mockito.verify(monopatinClient, never()).actualizarEstado(anyLong(), any(EstadosMonopatin.class));
        Mockito.verify(viajeRepository, never()).save(any(Viaje.class));
    }

    // --- TEST 2: Camino Feliz en Finalizar (El más complejo) ---
    @Test
    void testFinalizarViaje_HappyPath_FullLogic() {
        // GIVEN: Un viaje en curso y todos los servicios responden OK
        when(viajeRepository.findById(VIAJE_ID)).thenReturn(Optional.of(viajeEnCurso));
        when(paradaClient.getParadaById(PARADA_DESTINO_ID)).thenReturn(paradaDestino);
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viajeEnCurso); // Devuelve el viaje guardado
        when(tarifaClient.getTarifaById(TARIFA_ID)).thenReturn(tarifaEstandar);
        when(usuarioClient.getCuentaParaFacturar(USUARIO_ID)).thenReturn(CUENTA_ID);
        when(facturaClient.crearFactura(any(FacturaRequestDTO.class))).thenReturn(new FacturaResponseDTO());
        when(monopatinClient.actualizarEstado(MONOPATIN_ID, EstadosMonopatin.DISPONIBLE)).thenReturn(monopatinDisponible);

        // Mocks para el enriquecimiento final (getViajeById)
        when(paradaClient.getParadaById(PARADA_ORIGEN_ID)).thenReturn(paradaOrigen);
        when(paradaClient.getParadaById(PARADA_DESTINO_ID)).thenReturn(paradaDestino);

        // WHEN: Se finaliza el viaje
        Double kmRecorridos = 5.0;
        ViajeResponseDTO viajeFinalizado = viajeService.finalizarViaje(VIAJE_ID, PARADA_DESTINO_ID, kmRecorridos);

        // THEN: Verificamos el costo (30 min * $10/min = $300)
        double costoEsperado = 300.0;

        // VERIFY 1: Que se haya llamado a Facturación con los datos correctos
        ArgumentCaptor<FacturaRequestDTO> facturaCaptor = ArgumentCaptor.forClass(FacturaRequestDTO.class);
        Mockito.verify(facturaClient).crearFactura(facturaCaptor.capture());

        assertEquals(costoEsperado, facturaCaptor.getValue().getMontoTotal());
        assertEquals(CUENTA_ID, facturaCaptor.getValue().getCuentaId());
        assertEquals(VIAJE_ID, facturaCaptor.getValue().getViajeId());
        assertEquals(EstadosFactura.PENDIENTE, facturaCaptor.getValue().getEstado());

        // VERIFY 2: Que se haya liberado el monopatín
        Mockito.verify(monopatinClient).actualizarEstado(MONOPATIN_ID, EstadosMonopatin.DISPONIBLE);

        // VERIFY 3: Que el resultado final esté enriquecido
        assertNotNull(viajeFinalizado.getParadaFin());
        assertEquals("Parada Destino", viajeFinalizado.getParadaFin().getNombre());
    }

    // --- TEST 3: Camino de Error en Finalizar (Falla de un servicio externo) ---
    @Test
    void testFinalizarViaje_Fails_WhenFacturacionFalla() {
        // GIVEN: Todo OK, excepto Facturación que falla
        when(viajeRepository.findById(VIAJE_ID)).thenReturn(Optional.of(viajeEnCurso));
        when(paradaClient.getParadaById(PARADA_DESTINO_ID)).thenReturn(paradaDestino);
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viajeEnCurso);
        when(tarifaClient.getTarifaById(TARIFA_ID)).thenReturn(tarifaEstandar);
        when(usuarioClient.getCuentaParaFacturar(USUARIO_ID)).thenReturn(CUENTA_ID);

        // ¡LA FACTURACIÓN FALLA!
        when(facturaClient.crearFactura(any(FacturaRequestDTO.class)))
                .thenThrow(new RuntimeException("Error de red en facturacion"));

        // WHEN: Se intenta finalizar el viaje
        // THEN: Debería lanzar la excepción que nosotros relanzamos
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            viajeService.finalizarViaje(VIAJE_ID, PARADA_DESTINO_ID, 5.0);
        });

        // VERIFY: Verificamos que el error es el de facturación
        assertTrue(exception.getMessage().contains("Error al generar la factura"));

        // VERIFY (El más importante): Verificamos que el monopatín NUNCA se liberó,
        // porque el error de facturación (que es más crítico) detuvo el flujo.
        Mockito.verify(monopatinClient, never()).actualizarEstado(anyLong(), any(EstadosMonopatin.class));
    }

    // --- TEST 4: Prueba de Optimización N+1 (Batch Enrichment) ---
    @Test
    void testBatchEnrichment_GetAllViajes() {
        // GIVEN: Una lista de 2 viajes que usan 3 paradas únicas
        Viaje v1 = new Viaje(1L, 10L, 1L, 1L);
        v1.setId("v1");
        v1.setParadaDestinoId(20L);
        Viaje v2 = new Viaje(2L, 20L, 2L, 1L);
        v2.setId("v2");
        v2.setParadaDestinoId(30L);
        List<Viaje> listaViajes = List.of(v1, v2);

        ParadaDTO p10 = new ParadaDTO(10L, "Parada 10", 0.0, 0.0);
        ParadaDTO p20 = new ParadaDTO(20L, "Parada 20", 0.0, 0.0);
        ParadaDTO p30 = new ParadaDTO(30L, "Parada 30", 0.0, 0.0);
        List<ParadaDTO> listaParadas = List.of(p10, p20, p30);

        when(viajeRepository.findAll()).thenReturn(listaViajes);
        // Simulamos la llamada BATCH
        when(paradaClient.getParadasByIds(anyList())).thenReturn(listaParadas);

        // WHEN: Llamamos al método que devuelve una lista
        List<ViajeResponseDTO> resultado = viajeService.getAllViajes();

        // THEN: Verificamos que los datos fueron enriquecidos
        assertEquals(2, resultado.size());
        assertEquals("Parada 10", resultado.get(0).getParadaInicio().getNombre());
        assertEquals("Parada 20", resultado.get(0).getParadaFin().getNombre());
        assertEquals("Parada 20", resultado.get(1).getParadaInicio().getNombre());
        assertEquals("Parada 30", resultado.get(1).getParadaFin().getNombre());

        // VERIFY (El más importante): Verificamos que se llamó al cliente de paradas
        // UNA SOLA VEZ (no N+1 veces) con los IDs correctos.
        ArgumentCaptor<List<Long>> idCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(paradaClient, times(1)).getParadasByIds(idCaptor.capture());

        // Verificamos que la lista de IDs que se pidió contiene todos los IDs únicos
        List<Long> idsEnviados = idCaptor.getValue();
        assertEquals(3, idsEnviados.size()); // 3 IDs únicos
        assertTrue(idsEnviados.containsAll(Set.of(10L, 20L, 30L)));
    }
}