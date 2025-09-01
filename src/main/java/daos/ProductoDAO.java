package main.java.daos;

import java.util.List;

import main.java.entities.Producto;

public interface ProductoDAO {

	Producto findById(int id);
	List<Producto> findAll();
	void insert(Producto producto);
	void update(Producto producto);
	void delete(int id);
	void deleteAll();
	Producto findProductoMasRecaudador();
}
