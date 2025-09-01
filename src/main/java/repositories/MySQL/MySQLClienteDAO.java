package main.java.repositories.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.daos.ClienteDAO;
import main.java.entities.Cliente;

public class MySQLClienteDAO implements ClienteDAO {

	// Clase que implementa la interfaz ClienteDAO para manejar operaciones CRUD en
	// una base de datos MySQL

	private final Connection conn;

	public MySQLClienteDAO(Connection conn) {
		this.conn = conn;
		createTableIfNotExists();
	}

	private void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS Cliente (" + 
					 "idCliente INT PRIMARY KEY," + 
					 "nombre VARCHAR(100) NOT NULL," + 
					 "email VARCHAR(100) NOT NULL" + 
					 ");";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabla 'Cliente' creada o ya existía.");
		} catch (SQLException e) {
			System.out.println("Error al crear las tablas");
			e.printStackTrace();
		}
	}

	@Override
	public Cliente findById(int id) {
		final String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				// Si se encuentra el cliente, se crea y retorna el objeto Cliente
				if (rs.next()) {
					Cliente cliente = new Cliente(id, rs.getString("nombre"), rs.getString("email"));
					return cliente;
				} else {
					System.out.println("Cliente no encontrado con ID: " + id);
					return null;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar el cliente por ID");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Cliente> findAll() {
		List<Cliente> clientes = new ArrayList<>();
		final String sql = "SELECT * FROM Cliente";
		try (PreparedStatement ps = conn.prepareStatement(sql); 
			ResultSet rs = ps.executeQuery()) {
			// Recorrer el ResultSet y crear objetos Cliente
			while (rs.next()) {
				Cliente cliente = new Cliente(rs.getInt("idCliente"), rs.getString("nombre"), rs.getString("email"));
				clientes.add(cliente);
			}
			System.out.println("Clientes encontrados: " + clientes.size());
		} catch (SQLException e) {
			System.out.println("Error al buscar todos los clientes");
			e.printStackTrace();
		}
		return clientes;
	}

	@Override
	public void insert(Cliente cliente) {
		final String sql = "INSERT INTO Cliente(idCliente, nombre, email) VALUES(?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, cliente.getIdCliente());
			ps.setString(2, cliente.getNombre());
			ps.setString(3, cliente.getEmail());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error al insertar el cliente");
			e.printStackTrace();
		}
	}

	@Override
	public void update(Cliente cliente) {
		final String sql = "UPDATE Cliente SET nombre = ?, email = ? WHERE idCliente = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, cliente.getNombre());
			ps.setString(2, cliente.getEmail());
			ps.setInt(3, cliente.getIdCliente());
			ps.executeUpdate();
			System.out.println("Cliente actualizado correctamente");
		} catch (SQLException e) {
			System.out.println("Error al actualizar el cliente");
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		final String sql = "DELETE FROM Cliente WHERE idCliente = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println("Cliente eliminado correctamente");
		} catch (SQLException e) {
			System.out.println("Error al eliminar el cliente");
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAll() {
		final String sql = "DELETE FROM Cliente";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate();
			System.out.println("Todos los clientes eliminados correctamente");
		} catch (SQLException e) {
			System.out.println("Error al eliminar todos los clientes");
			e.printStackTrace();
		}
	}

	@Override
	public List<Cliente> findClientesOrdenadosPorFacturacion() {
		// SQL que calcula el total facturado por cliente y los ordena de mayor a menor
		final String sql = "SELECT c.idCliente, c.nombre, c.email, " +
						   "COALESCE(SUM(fp.cantidad * p.valor), 0) as total_facturado " +
						   "FROM Cliente c " +
						   "LEFT JOIN Factura f ON c.idCliente = f.idCliente " +
						   "LEFT JOIN FacturaProducto fp ON f.idFactura = fp.idFactura " +
						   "LEFT JOIN Producto p ON fp.idProducto = p.idProducto " +
						   "GROUP BY c.idCliente, c.nombre, c.email " +
						   "ORDER BY total_facturado DESC";
		
		List<Cliente> clientes = new ArrayList<>();
		
		try (PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				int idCliente = rs.getInt("idCliente");
				String nombre = rs.getString("nombre");
				String email = rs.getString("email");
				
				Cliente cliente = new Cliente(idCliente, nombre, email);
				clientes.add(cliente);
			}
			
		} catch (SQLException e) {
			System.out.println("Error al buscar clientes ordenados por facturación");
			e.printStackTrace();
		}
		
		return clientes;
	}
}
