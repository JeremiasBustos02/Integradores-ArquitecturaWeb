package main.java.factories;

import main.java.daos.ClienteDAO;
import main.java.daos.FacturaDAO;
import main.java.daos.FacturaProductoDAO;
import main.java.daos.ProductoDAO;
import main.java.repositories.MySQL.MySQLDAOFactory;

public abstract class DAOFactory {
	
	// Clase abstracta que define los métodos para obtener los DAOs
	
	// Implementación del patrón Singleton 
	private static volatile DAOFactory instance;
	
	// Método para obtener la instancia única de DAOFactory
	public static DAOFactory getInstance(DBType dbType) {
		if (instance == null) {
			// syncronized asegura que solo un hilo puede acceder a este bloque a la vez
			synchronized (DAOFactory.class) {
				if (instance == null) {
					switch (dbType) {
						case MYSQL:
							instance = new MySQLDAOFactory();
							break;
						// case POSTGRESQL:
						// 	instance = new PostgreSQLDAOFactory();
						// 	break;
						// case ORACLE:
						// 	instance = new OracleDAOFactory();
						// 	break;
						// case SQLSERVER:
						// 	instance = new SQLServerDAOFactory();
						// 	break;
						default:
							throw new IllegalArgumentException("Tipo de base de datos no soportado.");
					}
				}
			}
		}
		return instance;
	}
	
	// Métodos abstractos para obtener los DAOs
	public abstract ClienteDAO createClienteDAO();
	public abstract ProductoDAO createProductoDAO();
	public abstract FacturaDAO createFacturaDAO();
	public abstract FacturaProductoDAO createFacturaProductoDAO();
}
