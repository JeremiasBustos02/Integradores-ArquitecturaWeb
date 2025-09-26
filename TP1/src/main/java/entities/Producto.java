package main.java.entities;

public class Producto {
	private int idProdcuto;
	private String nombre;
	private float valor;
	
	public Producto(int idProdcuto, String nombre, float valor) {
		this.idProdcuto = idProdcuto;
		this.nombre = nombre;
		this.valor = valor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public int getIdProducto() {
		return idProdcuto;
	}
	
	
}
