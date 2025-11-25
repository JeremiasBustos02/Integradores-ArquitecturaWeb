package com.mock.mercadopago.dto;

public class SaldoResponse {
    private Long idCuenta;
    private Double saldo;

    public SaldoResponse(Long idCuenta, Double saldo){
        this.idCuenta = idCuenta;
        this.saldo = saldo;
    }

    public Long getIdCuenta(){ return idCuenta; }
    public Double getSaldo(){ return saldo; }
}
