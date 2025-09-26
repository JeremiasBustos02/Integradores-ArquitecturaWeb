package main.java.daos;

import java.util.List;

import main.java.entities.FacturaProducto;

public interface FacturaProductoDAO {

	FacturaProducto findById(int id, int id2);
	List<FacturaProducto> findAll();
	void insert(FacturaProducto facturaProducto);
	void update(FacturaProducto facturaProducto);
	void delete(int id, int id2);
	void deleteAll();
	
}
