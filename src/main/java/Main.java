package main.java;

import java.util.List;

import main.java.daos.ClienteDAO;
import main.java.daos.FacturaDAO;
import main.java.daos.FacturaProductoDAO;
import main.java.daos.ProductoDAO;

import main.java.entities.Cliente;
import main.java.entities.Producto;
import main.java.factories.DAOFactory;
import main.java.factories.DBType;

import main.java.utils.DataDelete;
import main.java.utils.DataLoader;

public class Main {

	public static void main(String[] args) {
		
		// Eliminar datos anteriores
		new DataDelete().deleteAllData();
		System.out.println("Datos anteriores eliminados");
		System.out.println("------------------------------------");
		
		// Cargar datos iniciales
		new DataLoader().loadData();
		System.out.println("Datos cargados correctamente");
		System.out.println("------------------------------------");

		DAOFactory f = DAOFactory.getInstance(DBType.MYSQL);
		ClienteDAO clienteDAO = f.createClienteDAO();
		FacturaDAO facturaDAO = f.createFacturaDAO();
		ProductoDAO productoDAO = f.createProductoDAO();
		FacturaProductoDAO facturaProductoDAO = f.createFacturaProductoDAO();
		System.out.println("------------------------------------");
		
		// Ejemplo de uso de los DAOs
		// Buscamos por ID
		Cliente c = clienteDAO.findById(1);
		System.out.println("Cliente con ID 1: " + c.getNombre() + " - " + c.getEmail());
		System.out.println("------------------------------------");
		
		// Actualizamos un cliente
		c.setNombre("Jeremias Bustos");
		clienteDAO.update(c);
		c = clienteDAO.findById(1);
		System.out.println("Cliente actualizado: " + c.getNombre() + " - " + c.getEmail());
		System.out.println("------------------------------------");
	
		List<Cliente> clientes = clienteDAO.findAll();
		System.out.println("Todos los Clientes: ");
		for (Cliente cliente : clientes) {
			System.out.println("Cliente: " + cliente.getIdCliente() + " - " + cliente.getNombre() + " - " + cliente.getEmail());
		}
		System.out.println("------------------------------------");
		
		List<Producto> productos = productoDAO.findAll();
		System.out.println("Todos los Productos: ");
		for (Producto producto : productos) {
			System.out.println("Producto: " + producto.getIdProducto() + " - " + producto.getNombre());
		}
		System.out.println("------------------------------------");
		
		System.out.println("Fin del programa.");
		
	}

}
