package main.java.utils;

import main.java.daos.ClienteDAO;
import main.java.daos.FacturaProductoDAO;
import main.java.daos.ProductoDAO;
import main.java.factories.DAOFactory;
import main.java.factories.DBType;
import main.java.daos.FacturaDAO;

public class DataDelete {

	private final ClienteDAO clienteDAO;
	private final ProductoDAO productoDAO;
	private final FacturaDAO facturaDAO;
	private final FacturaProductoDAO facturaProductoDAO;
	// Definir el tipo de base de datos que se va a utilizar
	private final DBType dbType = DBType.MYSQL;
	
	public DataDelete() {
		// Obtener la instancia del DAOFactory correspondiente al tipo de base de datos
		DAOFactory f = DAOFactory.getInstance(dbType);
		this.clienteDAO = f.createClienteDAO();
		this.productoDAO = f.createProductoDAO();
		this.facturaDAO = f.createFacturaDAO();
		this.facturaProductoDAO = f.createFacturaProductoDAO();
	}

	public void deleteAllData() {
		// Orden de eliminación para mantener la integridad referencial
		// 1) FacturaProducto (tabla intermedia)
		// 2) Factura
		// 3) Producto
		// 4) Cliente
		
		try {
			System.out.println("------------------------------------");
			this.facturaProductoDAO.deleteAll();
			this.facturaDAO.deleteAll();
			this.productoDAO.deleteAll();
			this.clienteDAO.deleteAll();
			System.out.println("------------------------------------");
			System.out.println("Todas las tablas han sido limpiadas.");
		} catch (Exception e) {
			System.out.println("Error al eliminar los datos de las tablas.");
			e.printStackTrace();
		}
	}
}
