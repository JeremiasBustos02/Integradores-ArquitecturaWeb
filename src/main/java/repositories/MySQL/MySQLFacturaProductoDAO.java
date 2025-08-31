package main.java.repositories.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.daos.FacturaProductoDAO;
import main.java.entities.FacturaProducto;

public class MySQLFacturaProductoDAO implements FacturaProductoDAO {
	
	// Clase que implementa la interfaz FacturaProductoDAO para manejar operaciones CRUD en
	// una base de datos MySQL
	
	private final Connection conn;
	
	public MySQLFacturaProductoDAO(Connection conn) {
		this.conn = conn;
		createTableIfNotExists();
	}
	
	private void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS FacturaProducto (" +
					 "idFactura INT NOT NULL," +
					 "idProducto INT NOT NULL," +
					 "cantidad INT NOT NULL," +
					 "PRIMARY KEY (idFactura, idProducto)," +
					 "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura)," +
					 "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
					 ");";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabla 'FacturaProducto' creada o ya existía.");
		} catch (Exception e) {
			System.out.println("Error al crear las tablas");
			e.printStackTrace();
		}
	}

	@Override
	public FacturaProducto findById(int id, int id2) {
		final String sql = "SELECT * FROM FacturaProducto WHERE idFactura = ? AND idProducto = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.setInt(2, id2);
			try (ResultSet rs = ps.executeQuery()) {
				// Si se encuentra el registro, se crea y retorna el objeto FacturaProducto
				if (rs.next()) {
					FacturaProducto facturaProducto = new FacturaProducto(
						rs.getInt("idFactura"),
						rs.getInt("idProducto"),
						rs.getInt("cantidad")
					);
					return facturaProducto;
				} else {
					System.out.println("Registro no encontrado con ID: " + id);
					return null;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al buscar el registro por ID");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<FacturaProducto> findAll() {
		final String sql = "SELECT * FROM FacturaProducto";
		List<FacturaProducto> facturaProductos = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				FacturaProducto facturaProducto = new FacturaProducto(
					rs.getInt("idFactura"),
					rs.getInt("idProducto"),
					rs.getInt("cantidad")
				);
				facturaProductos.add(facturaProducto);
			}
		} catch (Exception e) {
			System.out.println("Error al obtener todos los registros de FacturaProducto");
			e.printStackTrace();
		}
		return facturaProductos;
	}

	@Override
	public void insert(FacturaProducto facturaProducto) {
		final String sql = "INSERT INTO FacturaProducto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, facturaProducto.getIdFactura());
			ps.setInt(2, facturaProducto.getIdProducto());
			ps.setInt(3, facturaProducto.getCantidad());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error al insertar el registro de FacturaProducto");
			e.printStackTrace();
		}
	}

	@Override
	public void update(FacturaProducto facturaProducto) {
		final String sql = "UPDATE FacturaProducto SET cantidad = ? WHERE idFactura = ? AND idProducto = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, facturaProducto.getCantidad());
			ps.setInt(2, facturaProducto.getIdFactura());
			ps.setInt(3, facturaProducto.getIdProducto());
			ps.executeUpdate();
			System.out.println("Registro de FacturaProducto actualizado correctamente");
		} catch (Exception e) {
			System.out.println("Error al actualizar el registro de FacturaProducto");
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id, int id2) {
		final String sql = "DELETE FROM FacturaProducto WHERE idFactura = ? AND idProducto = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.setInt(2, id2);
			ps.executeUpdate();
			System.out.println("Registro de FacturaProducto eliminado correctamente");
		} catch (Exception e) {
			System.out.println("Error al eliminar el registro de FacturaProducto");
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteAll() {
		final String sql = "DELETE FROM FacturaProducto";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate();
			System.out.println("Todos los registros de FacturaProducto eliminados correctamente");
		} catch (Exception e) {
			System.out.println("Error al eliminar todos los registros de FacturaProducto");
			e.printStackTrace();
		}
	}
}
