package com.microservices.viajes.service.implementacion;

import com.microservices.viajes.clients.*;
import com.microservices.viajes.dto.request.EstadosFactura;
import com.microservices.viajes.dto.request.FacturaRequestDTO;
import com.microservices.viajes.dto.response.*;
import com.microservices.viajes.entity.Pausa;
import com.microservices.viajes.entity.Viaje;
import com.microservices.viajes.exception.InvalidViajeException;
import com.microservices.viajes.exception.MonopatinNotFound;
import com.microservices.viajes.exception.ViajeNotFoundException;
import com.microservices.viajes.mapper.ViajeMapper;
import com.microservices.viajes.repository.ViajeRepository;
import com.microservices.viajes.service.ViajeServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ViajeService implements ViajeServiceI {
    private final ViajeRepository viajeRepository;
    private final ViajeMapper mapper;
    private final ParadaClientRest paradaClient;
    private final MonopatinClientRest monopatinClient;
    private final UsuarioClientRest usuarioClient;
    private final TarifaClientRest tarifaClient;
    private final FacturacionClientRest facturaClient;

    @Override
    public ViajeResponseDTO createViaje(Long monopatinId, Long usuarioId, Long tarifaId, Long paradaInicioId) {
        log.info("Creando viaje para usuario: {}", usuarioId);
        if (usuarioId == null || monopatinId == null) {
            throw new InvalidViajeException("Monopatin o usuario no encontrados");
        }
        try {
            paradaClient.getParadaById(paradaInicioId);
        } catch (Exception e) {
            throw new InvalidViajeException("La parada de inicio no existe");
        }
        try {
            boolean puedeViajar = usuarioClient.saldoSuficiente(usuarioId);
            if (!puedeViajar) throw new InvalidViajeException("El usuario no tiene fondo suficiente");

        } catch (Exception e) {
            throw new InvalidViajeException("Usuario no valido o sin saldo: " + e.getMessage());
        }
        MonopatinDTO monopatin;
        try {
            monopatin = monopatinClient.getMonopatinById(monopatinId);
            if (!monopatin.getEstado().equals("DISPONIBLE")) {
                throw new InvalidViajeException("El monopatin no esta disponible");
            }
        } catch (Exception e) {
            throw new InvalidViajeException("El monopatin con id " + monopatinId + " no es valido o no esta disponible: " + e.getMessage());
        }

        try {
            monopatinClient.actualizarEstado(monopatin.getId(), "EN USO");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar el estado del monopatin (servicio externo fallo): " + e.getMessage());
        }

        Viaje viaje = new Viaje(monopatinId, paradaInicioId, usuarioId, tarifaId);
        viaje.setPausas(new ArrayList<>());
        viaje.setHoraInicio(LocalDateTime.now());

        Viaje viajeSaved = viajeRepository.save(viaje);
        log.info("Viaje creado correctamente con id: {}", viajeSaved.getId());

        return this.getViajeById(viajeSaved.getId());
    }

    @Override
    public ViajeResponseDTO finalizarViaje(String id, Long paradaFin, Double kmRecorridos) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del viaje es incorrecto");
        }

        if (paradaFin == null || kmRecorridos == null || kmRecorridos < 0) {
            throw new IllegalArgumentException("Parada y kilómetros son obligatorios");
        }
        try {
            ParadaDTO paradaIni = paradaClient.getParadaById(paradaFin);
        } catch (Exception e) {
            throw new InvalidViajeException("No existe la parada indicada");
        }
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new ViajeNotFoundException(id));
        if (viaje.getHoraFin() != null) throw new InvalidViajeException("El viaje ya esta finalizado");
        viaje.setParadaDestinoId(paradaFin);
        viaje.setDistanciaRecorrida(kmRecorridos);
        viaje.setHoraFin(LocalDateTime.now());
        Double costoTotal;
        try {
            TarifaDTO tarifa = tarifaClient.getTarifaById(viaje.getTarifaId());

            long minutosTotales = Duration.between(viaje.getHoraInicio(), viaje.getHoraFin()).toMinutes();

            // calculo de la tarifa
            costoTotal = minutosTotales * tarifa.getMontoBase();

            log.info("Viaje {} finalizado. Duración: {} min. Costo: ${}", id, minutosTotales, costoTotal);


        } catch (Exception e) {
            // no existio tarifa
            throw new RuntimeException("Error al calcular la tarifa del viaje: " + e.getMessage());
        }
        try {
            // esperar que me expongan el endpoint para obtener la cuenta a facturar
            Long cuentaIdParaFacturar = usuarioClient.getCuentaParaFacturar(viaje.getUsuarioId());


            FacturaRequestDTO facturaRequest = new FacturaRequestDTO(
                    cuentaIdParaFacturar,
                    viaje.getId(),
                    costoTotal,
                    LocalDateTime.now(),
                    EstadosFactura.PENDIENTE
            );

            // creamos la factura
            facturaClient.crearFactura(facturaRequest);
            log.info("Notificación de factura enviada para el viaje: {}", id);

        } catch (Exception e) {

            throw new RuntimeException("Error al generar la factura: " + e.getMessage());
        }
        try {
            monopatinClient.actualizarEstado(viaje.getMonopatinId(), "DISPONIBLE");
        } catch (Exception e) {
            log.error("Fallo al notificar a MSVC-MONOPATIN. Viaje ID: {}", id, e);
            throw new MonopatinNotFound("No se encontro el monopatin ");
        }

        Viaje viajeAct = viajeRepository.save(viaje);
        return this.getViajeById(viajeAct.getId());
    }

    @Override
    public ViajeResponseDTO iniciarPausa(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El id debe ser valido");
        Viaje viaje = viajeRepository.findById(id).orElseThrow(() -> new ViajeNotFoundException(id));
        if (viaje.getHoraFin() != null) throw new InvalidViajeException("No se puede pausar un viaje finalizado");
        if (viaje.getPausas() != null && viaje.getPausas().stream().anyMatch(p -> p.getTiempoFin() == null))
            throw new InvalidViajeException("Ya existe una pausa activa");
        Pausa pausa = new Pausa();
        pausa.setTiempoInicio(LocalDateTime.now());
        if (viaje.getPausas() == null) viaje.setPausas(new ArrayList<>());
        viaje.getPausas().add(pausa);
        Viaje viajeAct = viajeRepository.save(viaje);
        return mapper.toResponseDTO(viajeAct);
    }

    @Override
    public ViajeResponseDTO finalizarPausa(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El id debe ser valido");
        Viaje viaje = viajeRepository.findById(id).orElseThrow(() -> new ViajeNotFoundException(id));
        if (viaje.getHoraFin() != null) throw new InvalidViajeException("No se puede pausar un viaje finalizado");
        if (viaje.getPausas() == null || viaje.getPausas().isEmpty())
            throw new InvalidViajeException("No existe pausa para finalizar");
        Pausa pausaActiva = viaje.getPausas().stream()
                .filter(p -> p.getTiempoFin() == null)
                .findFirst()
                .orElseThrow(() -> new InvalidViajeException("No hay pausa activa para finalizar"));
        pausaActiva.setTiempoFin(LocalDateTime.now());
        Viaje viajeAct = viajeRepository.save(viaje);
        return mapper.toResponseDTO(viajeAct);
    }

    @Override
    public ViajeResponseDTO getViajeById(String id) {
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new ViajeNotFoundException(id));

        ViajeResponseDTO viajeDTO = mapper.toResponseDTO(viaje);

        try {
            ParadaDTO paradaInicio = paradaClient.getParadaById(viaje.getParadaOrigenId());
            viajeDTO.setParadaInicio(paradaInicio);

            if (viaje.getParadaDestinoId() != null) {
                ParadaDTO paradaFin = paradaClient.getParadaById(viaje.getParadaDestinoId());
                viajeDTO.setParadaFin(paradaFin);
            }
        } catch (Exception e) {
            log.warn("Error al obtener datos de paradas: {}", e.getMessage());
        }
        return viajeDTO;
    }

    @Override
    public List<ViajeResponseDTO> getAllViajes() {
        List<Viaje> viajes = viajeRepository.findAll();
        return mapper.toResponseDTOList(viajes);
    }

    @Override
    public List<ViajeResponseDTO> getViajesByUsuario(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El ID de usuario debe ser mayor a 0");
        }

        List<Viaje> viajes = viajeRepository.findByUsuarioId(usuarioId);
        return mapper.toResponseDTOList(viajes);


    }

    @Override
    public List<ViajeResponseDTO> getViajesByMonopatin(Long monopatinId) {
        if (monopatinId == null || monopatinId <= 0) throw new IllegalArgumentException("El id debe ser mayor a 0");
        List<Viaje> viajes = viajeRepository.getViajesByMonopatinId(monopatinId);
        return mapper.toResponseDTOList(viajes);
    }

    @Override
    public ReporteUsoMonopatinDTO getReporteUsoMonopatin(Long monopatinId, boolean incluirTiempoPausas) {
        if (monopatinId == null || monopatinId <= 0) {
            throw new IllegalArgumentException("El ID de monopatín debe ser mayor a 0");
        }

        List<Viaje> viajes = viajeRepository.getViajesByMonopatinId(monopatinId);

        double totalKm = 0;
        for (Viaje viaje : viajes) {
            if (viaje.getDistanciaRecorrida() > 0) {
                totalKm += viaje.getDistanciaRecorrida();
            }
        }

        long totalMinutos = 0;
        for (Viaje viaje : viajes) {
            totalMinutos += calcularTiempoViaje(viaje, incluirTiempoPausas);
        }
        return new ReporteUsoMonopatinDTO(monopatinId, totalKm, totalMinutos, viajes.size());
    }

    @Override
    public Long contarViajesPorMonopatinEnAnio(Long monopatinId, Integer anio) {
        LocalDateTime inicioAnio = LocalDateTime.of(anio, 1, 1, 0, 0);
        LocalDateTime finAnio = LocalDateTime.of(anio, 12, 31, 23, 59);

        return viajeRepository.countByMonopatinIdAndHoraInicioBetween(
                monopatinId, inicioAnio, finAnio
        );
    }

    private long calcularTiempoViaje(Viaje viaje, boolean incluirPausas) {
        if (viaje.getHoraInicio() == null || viaje.getHoraFin() == null) {
            return 0;
        }

        long tiempoTotal = Duration.between(viaje.getHoraInicio(), viaje.getHoraFin()).toMinutes();


        if (!incluirPausas && viaje.getPausas() != null) {
            long tiempoPausas = 0;

            for (Pausa pausa : viaje.getPausas()) {
                if (pausa.getTiempoInicio() != null && pausa.getTiempoFin() != null) {
                    long duracionPausa = Duration.between(pausa.getTiempoInicio(), pausa.getTiempoFin()).toMinutes();
                    tiempoPausas += duracionPausa;
                }
            }

            tiempoTotal -= tiempoPausas;
        }

        return tiempoTotal;
    }

    @Override
    public void deleteViaje(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El id no puede estar vacio");
        Optional<Viaje> viajeOpt = viajeRepository.findById(id);
        if (!viajeRepository.existsById(id)) {
            throw new ViajeNotFoundException(id);
        }

        viajeRepository.deleteById(id);
    }

}

