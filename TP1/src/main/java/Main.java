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
		
		// Obtener el producto más recaudador
		Producto productoMasRecaudador = productoDAO.findProductoMasRecaudador();
		if (productoMasRecaudador != null) {
			System.out.println("El producto que más recaudó es: " + 
							 productoMasRecaudador.getNombre() + 
							 " (ID: " + productoMasRecaudador.getIdProducto() + 
							 ") con un valor de: $" + productoMasRecaudador.getValor() + " por unidad.");
		} else {
			System.out.println("No se pudo determinar el producto más recaudador");
		}
		System.out.println("------------------------------------");
		
		// Obtener clientes ordenados por facturación
		List<Cliente> clientesOrdenados = clienteDAO.findClientesOrdenadosPorFacturacion();
		System.out.println("Clientes ordenados por monto total facturado (mayor a menor):");
		for (int i = 0; i < clientesOrdenados.size(); i++) {
			Cliente cliente = clientesOrdenados.get(i);
			String totalFacturadoStr = "$" + cliente.getTotalFacturado();
			System.out.println((i + 1) + ". " + cliente.getNombre() + 
							 " (ID: " + cliente.getIdCliente() + 
							 ") - Email: " + cliente.getEmail() +
							 " - Total facturado: " + totalFacturadoStr);
		}
		System.out.println("------------------------------------");
		
		System.out.println("Fin del programa.");
		
	}

}
