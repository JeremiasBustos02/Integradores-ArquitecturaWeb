package main.java.entities;

public class Factura {
	private int idFactura;
	private int idCliente;
	
	public Factura(int idFactura, int idCliente) {
		this.idFactura = idFactura;
		this.idCliente = idCliente;
	}

	public int getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	public int getIdCliente() {
		return idCliente;
	}
	
	
}
