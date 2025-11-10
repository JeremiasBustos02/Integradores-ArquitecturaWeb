package com.microservices.viajes;

import com.microservices.viajes.clients.MonopatinClientRest;
import com.microservices.viajes.clients.ParadaClientRest;
import com.microservices.viajes.dto.response.MonopatinDTO;
import com.microservices.viajes.dto.response.ParadaDTO;
import com.microservices.viajes.dto.response.ViajeResponseDTO;
import com.microservices.viajes.service.implementacion.ViajeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ViajeServiceTest {

    @MockBean
    private ParadaClientRest paradaCliente;

    @MockBean
    private MonopatinClientRest monopatinClient;

    @Autowired
    private ViajeService viajeService;

    @Test
    void testCreateViaje() {
        // Mock de las respuestas
        ParadaDTO paradaMock = new ParadaDTO(1L, "Parada 1", -37.32, -59.13);
        MonopatinDTO monopatinMock = new MonopatinDTO(1L, -37.32, -59.13, "DISPONIBLE");

        when(paradaCliente.getParadaById(1L)).thenReturn(paradaMock);
        when(monopatinClient.getMonopatinById(1L)).thenReturn(monopatinMock);

        // Probar tu servicio
        ViajeResponseDTO response = viajeService.createViaje(1L, 1L, 1L, 1L);

        // --- FORMA 1: Imprimirlo para verlo (Debug) ---
        System.out.println("Respuesta del test: " + response.toString());

        // --- FORMA 2: Afirmaciones (Asserts) más potentes ---
        assertNotNull(response); // 1. Confirmamos que no es nulo
        assertNotNull(response.getId()); // 2. Confirmamos que Mongo le dio un ID

        // 3. ¡Confirmamos que se "enriqueció" con el mock de la parada!
        assertNotNull(response.getParadaInicio());
        assertEquals(1L, response.getParadaInicio().getId());
        assertEquals("Parada 1", response.getParadaInicio().getNombre());
    }

    @Test
    void testViajeLifecycle_Success() throws InterruptedException {
        // --- 1. GIVEN (Configuración) ---
        // IDs de prueba
        final Long MONOPATIN_ID = 1L;
        final Long USUARIO_ID = 1L;
        final Long TARIFA_ID = 1L;
        final Long PARADA_ORIGEN_ID = 10L;
        final Long PARADA_DESTINO_ID = 20L;
        final Double DISTANCIA_RECORRIDA = 3.5;

        // Mocks de DTOs
        ParadaDTO paradaOrigenMock = new ParadaDTO(PARADA_ORIGEN_ID, "Parada Origen", -37.0, -59.0);
        ParadaDTO paradaDestinoMock = new ParadaDTO(PARADA_DESTINO_ID, "Parada Destino", -37.1, -59.1);
        MonopatinDTO monopatinDisponibleMock = new MonopatinDTO(MONOPATIN_ID, -37.0, -59.0, "DISPONIBLE");

        // Mocks de las llamadas GET (para validación y enriquecimiento)
        when(paradaCliente.getParadaById(PARADA_ORIGEN_ID)).thenReturn(paradaOrigenMock);
        when(paradaCliente.getParadaById(PARADA_DESTINO_ID)).thenReturn(paradaDestinoMock);
        when(monopatinClient.getMonopatinById(MONOPATIN_ID)).thenReturn(monopatinDisponibleMock);

        // Mocks de las llamadas PUT (para notificación de estado)
        MonopatinDTO monopatinEnUsoMock = new MonopatinDTO(MONOPATIN_ID, -37.0, -59.0, "EN USO");
        // Usamos doNothing() porque los métodos 'actualizarEstado' devuelven void
        when(monopatinClient.actualizarEstado(MONOPATIN_ID, "EN USO")).thenReturn(monopatinEnUsoMock);
        when(monopatinClient.actualizarEstado(MONOPATIN_ID, "DISPONIBLE")).thenReturn(monopatinDisponibleMock);

        // --- 2. WHEN & THEN (Crear Viaje) ---
        ViajeResponseDTO viajeCreado = viajeService.createViaje(MONOPATIN_ID, USUARIO_ID, TARIFA_ID, PARADA_ORIGEN_ID);

        // Verificamos
        assertNotNull(viajeCreado.getId());
        assertNull(viajeCreado.getHoraFin()); // Aún no ha terminado
        assertEquals("Parada Origen", viajeCreado.getParadaInicio().getNombre()); // Fue enriquecido

        // Verificamos que se haya notificado el cambio de estado
        Mockito.verify(monopatinClient, times(1)).actualizarEstado(MONOPATIN_ID, "EN USO");

        // Obtenemos el ID para los siguientes pasos
        String viajeId = viajeCreado.getId();

        // --- 3. WHEN & THEN (Iniciar Pausa) ---
        ViajeResponseDTO viajePausado = viajeService.iniciarPausa(viajeId);

        // Verificamos
        assertEquals(1, viajePausado.getPausas().size());
        assertNotNull(viajePausado.getPausas().get(0).getTiempoInicio());
        assertNull(viajePausado.getPausas().get(0).getTiempoFin());

        // --- 4. WHEN & THEN (Finalizar Pausa) ---
        ViajeResponseDTO viajeReanudado = viajeService.finalizarPausa(viajeId);

        // Verificamos
        assertEquals(1, viajeReanudado.getPausas().size());
        assertNotNull(viajeReanudado.getPausas().get(0).getTiempoFin());

        // --- 5. WHEN & THEN (Finalizar Viaje) ---
        // (Asumimos que el DTO de finalizar ahora solo pide km y paradaId)
        ViajeResponseDTO viajeFinalizado = viajeService.finalizarViaje(viajeId, PARADA_DESTINO_ID, DISTANCIA_RECORRIDA);

        // Verificamos
        assertNotNull(viajeFinalizado.getHoraFin());
        assertEquals(DISTANCIA_RECORRIDA, viajeFinalizado.getKmRecorridos());
        assertEquals("Parada Destino", viajeFinalizado.getParadaFin().getNombre()); // Fue enriquecido

        // Verificamos que se notificó el cambio de estado de vuelta a DISPONIBLE
        Mockito.verify(monopatinClient, times(1)).actualizarEstado(MONOPATIN_ID, "DISPONIBLE");
        System.out.println("Respuesta del test: " + viajeFinalizado.toString());

    }
}