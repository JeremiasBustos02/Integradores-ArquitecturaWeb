package main.java.repositories.MySQL;

import main.java.daos.ClienteDAO;
import main.java.daos.FacturaDAO;
import main.java.daos.FacturaProductoDAO;
import main.java.daos.ProductoDAO;
import main.java.factories.ConnectionManager;
import main.java.factories.DAOFactory;

public class MySQLDAOFactory extends DAOFactory {
	
	// Clase que se encarga de crear las instancias concretas de los DAOs para MySQL
	// Seria la fabrica de DAOs para MySQL
	
	@Override
	public ClienteDAO createClienteDAO() {
		// Devuelve la implemetnación concreta de ClienteDAO para MySQL
		return new MySQLClienteDAO(ConnectionManager.getInstance().getConnection());
	}

	@Override
	public ProductoDAO createProductoDAO() {
		return new MySQLProductoDAO(ConnectionManager.getInstance().getConnection());
	}

	@Override
	public FacturaDAO createFacturaDAO() {
		return new MySQLFacturaDAO(ConnectionManager.getInstance().getConnection());
	}

	@Override
	public FacturaProductoDAO createFacturaProductoDAO() {
		return new MySQLFacturaProductoDAO(ConnectionManager.getInstance().getConnection());
	}
}
