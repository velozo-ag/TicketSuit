package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import database.DatabaseConnection;
import entities.Asiento;
import entities.Compra;

public class CompraController {
    Connection connection;

    public CompraController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public int createCompra(Compra compra) {
        String query = "INSERT INTO Compra (subtotal, fecha, cantidad, id_usuario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setFloat(1, compra.getSubtotal());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, compra.getCantidad());
            stmt.setInt(4, compra.getId_usuario());
            stmt.executeUpdate();

            System.out.println("Compra insertada");

        } catch (SQLException e) {
            System.out.println("Error al insertar compra: " + e.getMessage());
        }

        return obtenerUltimoIdCompra();
    }

    public int obtenerUltimoIdCompra() {
        String query = "SELECT TOP 1 id_compra FROM Compra ORDER BY id_compra DESC";
    
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1); 
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener id compra: " + e.getMessage());
        }
        return -1; 
    }
}
