package main.java.daos;

import java.util.List;

import main.java.entities.Factura;

public interface FacturaDAO {
	
	Factura findById(int id);
	List<Factura> findAll();
	void insert(Factura factura);
	void update(Factura factura);
	void delete(int id);
	void deleteAll();

}
