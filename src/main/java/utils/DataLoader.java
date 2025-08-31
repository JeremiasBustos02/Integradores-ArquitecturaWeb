package main.java.utils;

import java.util.List;

import main.java.daos.ClienteDAO;
import main.java.daos.FacturaDAO;
import main.java.daos.FacturaProductoDAO;
import main.java.daos.ProductoDAO;
import main.java.entities.Cliente;
import main.java.entities.Factura;
import main.java.entities.FacturaProducto;
import main.java.entities.Producto;
import main.java.factories.DAOFactory;
import main.java.factories.DBType;

public class DataLoader {
	private final ClienteDAO clienteDAO;
	private final ProductoDAO productoDAO;
	private final FacturaDAO facturaDAO;
	private final FacturaProductoDAO facturaProductoDAO;
	private CSVReader csvReader;
	
	private final DBType dbType = DBType.MYSQL;
	
	public DataLoader() {
		DAOFactory f = DAOFactory.getInstance(dbType);
		
		this.clienteDAO = f.createClienteDAO();
		this.productoDAO = f.createProductoDAO();
		this.facturaDAO = f.createFacturaDAO();
		this.facturaProductoDAO = f.createFacturaProductoDAO();
		System.out.println("------------------------------------");
		this.csvReader = new CSVReader();
	}
	
	// Cargar datos desde archivos CSV
	public void loadData() {
		loadClientes("src/main/resources/csv_files/clientes.csv");
		loadProductos("src/main/resources/csv_files/productos.csv");
		loadFacturas("src/main/resources/csv_files/facturas.csv");
		loadFacturaProductos("src/main/resources/csv_files/facturas-productos.csv");
	}

	// Métodos privados para cargar las tablas
	
	private void loadClientes(String filePath) {
		List<Cliente> clientes = csvReader.readFileCliente(filePath);
		for (Cliente cliente : clientes) {
			clienteDAO.insert(cliente);
		}
		System.out.println("Clientes cargados: " + clientes.size());
		System.out.println("------------------------------------");
	}
	
	private void loadProductos(String filePath) {
		List<Producto> productos = csvReader.readFileProducto(filePath);
		for (Producto producto : productos) {
			productoDAO.insert(producto);
		}
		System.out.println("Productos cargados: " + productos.size());
		System.out.println("------------------------------------");
	}
	
	private void loadFacturas(String filePath) {
		List<Factura> facturas = csvReader.readFileFactura(filePath);
		for (Factura factura : facturas) {
			facturaDAO.insert(factura);
		}
		System.out.println("Facturas cargadas: " + facturas.size());
		System.out.println("------------------------------------");
	}
	
	private void loadFacturaProductos(String filePath) {
		List<FacturaProducto> facturaProductos = csvReader.readFileFacturaProducto(filePath);
		for (FacturaProducto fp : facturaProductos) {
			facturaProductoDAO.insert(fp);
		}
		System.out.println("Facturas-Productos cargados: " + facturaProductos.size());
		System.out.println("------------------------------------");
	}
}