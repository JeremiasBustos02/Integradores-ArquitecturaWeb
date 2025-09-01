package main.java.repositories.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.daos.ProductoDAO;
import main.java.entities.Producto;

public class MySQLProductoDAO implements ProductoDAO {

	// Clase que implementa la interfaz ProductoDAO para manejar operaciones CRUD en
	// una base de datos MySQL
	
	private final Connection conn;

	public MySQLProductoDAO(Connection conn) {
		this.conn = conn;
		createTableIfNotExists();
	}

	private void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS Producto (" + 
					 "idProducto INT PRIMARY KEY," + 
					 "nombre VARCHAR(100) NOT NULL," + 
					 "valor FLOAT NOT NULL" + 
					 ");";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabla 'Producto' creada o ya existía.");
		} catch (Exception e) {
			System.out.println("Error al crear las tablas");
			e.printStackTrace();
		}
	}

	@Override
	public Producto findById(int id) {
		final String sql = "SELECT * FROM Producto WHERE idProducto = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				// Si se encuentra el producto, se crea y retorna el objeto Producto
				if (rs.next()) {
					Producto producto = new Producto(id, rs.getString("nombre"), rs.getFloat("valor"));
					return producto;
				} else {
					System.out.println("Producto no encontrado con ID: " + id);
					return null;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al buscar el Producto por ID");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Producto> findAll() {
		final String sql = "SELECT * FROM Producto";
		List<Producto> productos = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Producto producto = new Producto(rs.getInt("idProducto"), rs.getString("nombre"), rs.getFloat("valor"));
				productos.add(producto);
			}
		} catch (Exception e) {
			System.out.println("Error al buscar todos los productos");
			e.printStackTrace();
		}
		return productos;
	}

	@Override
	public void insert(Producto producto) {
		final String sql = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, producto.getIdProducto());
			ps.setString(2, producto.getNombre());
			ps.setFloat(3, producto.getValor());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error al insertar el producto");
			e.printStackTrace();
		}
	}

	@Override
	public void update(Producto producto) {
		final String sql = "UPDATE Producto SET nombre = ?, valor = ? WHERE idProducto = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, producto.getNombre());
			ps.setFloat(2, producto.getValor());
			ps.setInt(3, producto.getIdProducto());
			ps.executeUpdate();
			System.out.println("Producto actualizado correctamente");
		} catch (Exception e) {
			System.out.println("Error al actualizar el producto");
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id) {
		final String sql = "DELETE FROM Producto WHERE idProducto = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println("Producto eliminado correctamente");
		} catch (Exception e) {
			System.out.println("Error al eliminar el producto");
			e.printStackTrace();
		}

	}

	@Override
	public void deleteAll() {
		final String sql = "DELETE FROM Producto";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate();
			System.out.println("Todos los productos eliminados correctamente");
		} catch (Exception e) {
			System.out.println("Error al eliminar todos los productos");
			e.printStackTrace();
		}
	}

	@Override
	public Producto findProductoMasRecaudador() {
		// SQL que calcula la recaudación total por producto (cantidad * valor) 
		// y retorna el producto con mayor recaudación
		final String sql = "SELECT p.idProducto, p.nombre, p.valor, " +
						   "SUM(fp.cantidad * p.valor) as recaudacion_total " +
						   "FROM Producto p " +
						   "INNER JOIN FacturaProducto fp ON p.idProducto = fp.idProducto " +
						   "GROUP BY p.idProducto, p.nombre, p.valor " +
						   "ORDER BY recaudacion_total DESC " +
						   "LIMIT 1";
		
		try (PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			
			if (rs.next()) {
				int idProducto = rs.getInt("idProducto");
				String nombre = rs.getString("nombre");
				float valor = rs.getFloat("valor");
				float recaudacionTotal = rs.getFloat("recaudacion_total");
				
				System.out.println("Producto más recaudador: " + nombre + 
								 " (ID: " + idProducto + ") - Recaudación total: $" + recaudacionTotal);
				
				return new Producto(idProducto, nombre, valor);
			} else {
				System.out.println("No se encontraron productos con ventas");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Error al buscar el producto más recaudador");
			e.printStackTrace();
			return null;
		}
	}

}
