package com.mock.mercadopago.controller;

import com.mock.mercadopago.dto.SaldoResponse;
import com.mock.mercadopago.service.MercadoPagoMockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock/mp")
public class MercadoPagoMockController {

    private final MercadoPagoMockService service;

    public MercadoPagoMockController(MercadoPagoMockService service) {
        this.service = service;
    }

    @GetMapping("/consultarSaldo/{idCuenta}")
    public ResponseEntity<SaldoResponse> consultarSaldo(@PathVariable Long idCuenta) {
        Double saldo = service.consultarSaldo(idCuenta);
        return ResponseEntity.ok(new SaldoResponse(idCuenta, saldo));
    }
}
