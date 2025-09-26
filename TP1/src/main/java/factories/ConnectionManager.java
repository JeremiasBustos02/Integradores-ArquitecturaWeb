package main.java.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
	
	// Clase que gestiona la conexión a la base de datos
	
	// Implementación del patrón Singleton
	private static volatile ConnectionManager instance;
	// Conexión a la base de datos
	private Connection connection;
	
	// Configuración de la conexión a la base de datos
	private static final String URL = "jdbc:mysql://localhost:3306/DB_INTEGRADOR";
	private static final String USER = "root";
	private static final String PASSWORD = "password";
	
	private ConnectionManager() {
		try {
			// Registrar el driver JDBC
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Establecer la conexión
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Conexión exitosa a la base de datos.");
		} catch (SQLException e) {
			System.err.println("Error: No se encontró el driver de MySQL.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Error al conectar con la base de datos.");
			e.printStackTrace();
		}
	}
	
	// Método para obtener la instancia única de ConnectionManager
	public static ConnectionManager getInstance() {
		if (instance == null) {
			// syncronized asegura que solo un hilo puede acceder a este bloque a la vez
			synchronized (ConnectionManager.class) {
				if (instance == null) {
					instance = new ConnectionManager();
				}
			}
		}
		return instance;
	}
	
	// Método para obtener la conexión a la base de datos
	public Connection getConnection() {
		return connection;
	}
}
