package com.mock.mercadopago.service;

import org.springframework.stereotype.Service;

@Service
public class MercadoPagoMockService {

    public Double consultarSaldo(Long idCuenta) {
        return 5000.00;
    }
}
