package main.java.repositories.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.daos.FacturaDAO;
import main.java.entities.Factura;

public class MySQLFacturaDAO implements FacturaDAO {
	
	// Clase que implementa la interfaz FacturaDAO para manejar operaciones CRUD en una base de datos MySQL
	
	private final Connection conn;
	
	public MySQLFacturaDAO(Connection conn) {
		this.conn = conn;
		createTableIfNotExists();
	}
	
	private void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS Factura (" +
					 "idFactura INT PRIMARY KEY," +
					 "idCliente INT NOT NULL," +
					 "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
					 ");";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabla 'Factura' creada o ya existía.");
		} catch (Exception e) {
			System.out.println("Error al crear las tablas");
			e.printStackTrace();
		}
	}

	@Override
	public Factura findById(int id) {
		final String sql = "SELECT * FROM Factura WHERE idFactura = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				// Si se encuentra la factura, se crea y retorna el objeto Factura
				if (rs.next()) {
					Factura factura = new Factura(id, rs.getInt("idCliente"));
					return factura;
				} else {
					System.out.println("Factura no encontrado con ID: " + id);
					return null;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar la Factura por ID");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Factura> findAll() {
		List<Factura> facturas = new ArrayList<>();
		final String sql = "SELECT * FROM Factura";
		try (PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Factura factura = new Factura(rs.getInt("idFactura"), rs.getInt("idCliente"));
				facturas.add(factura);
			}
		} catch (SQLException e) {
			System.out.println("Error al obtener todas las facturas");
			e.printStackTrace();
		}
		return facturas;
	}

	@Override
	public void insert(Factura factura) {
		final String sql = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, factura.getIdFactura());
			ps.setInt(2, factura.getIdCliente());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error al insertar la factura");
			e.printStackTrace();
		} 
	}

	@Override
	public void update(Factura factura) {
		final String sql = "UPDATE Factura SET idCliente = ? WHERE idFactura = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, factura.getIdCliente());
			ps.setInt(2, factura.getIdFactura());
			ps.executeUpdate();
			System.out.println("Factura actualizada correctamente");
		} catch (SQLException e) {
			System.out.println("Error al actualizar la factura");
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(int id) {
		final String sql = "DELETE FROM Factura WHERE idFactura = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println("Factura eliminada correctamente");
		} catch (SQLException e) {
			System.out.println("Error al eliminar la factura");
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAll() {
		final String sql = "DELETE FROM Factura";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate();
			System.out.println("Todas las facturas eliminadas correctamente");
		} catch (SQLException e) {
			System.out.println("Error al eliminar todas las facturas");
			e.printStackTrace();
		}
		
	}

}
