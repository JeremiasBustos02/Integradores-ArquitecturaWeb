package main.java.daos;

import java.util.List;

import main.java.entities.Cliente;

public interface ClienteDAO {

	Cliente findById(int id);
	List<Cliente> findAll();
	void insert(Cliente cliente);
	void update(Cliente cliente);
	void delete(int id);
	void deleteAll();
	List<Cliente> findClientesOrdenadosPorFacturacion();
	
}
