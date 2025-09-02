package main.java.entities;

public class Cliente {
	private int idCliente;
	private String nombre;
	private String email;
	private Float totalFacturado; // Atributo opcional
	
	public Cliente(int idCliente, String nombre, String email) {
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.email = email;
		this.totalFacturado = null; // Por defecto null (no calculado)
	}
	
	// Constructor adicional que incluye el total facturado
	public Cliente(int idCliente, String nombre, String email, float totalFacturado) {
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.email = email;
		this.totalFacturado = totalFacturado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdCliente() {
		return idCliente;
	}
	
	public Float getTotalFacturado() {
		return totalFacturado;
	}
	
	public void setTotalFacturado(Float totalFacturado) {
		this.totalFacturado = totalFacturado;
	}
	
}
