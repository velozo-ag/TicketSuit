package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Asiento;
import entities.Compra;

public class CompraController {
    Connection connection = DatabaseConnection.getInstance().connect();

    public CompraController() {
        // this.connection = DatabaseConnection.getInstance().connect();

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

    public List<Compra> findAll() {
        String query = "SELECT * FROM Compra";
        List<Compra> compras = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id_compra = rs.getInt("id_compra");
                Compra compra = new Compra(id_compra);
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setSubtotal(rs.getFloat("subtotal"));
                compra.setFecha(rs.getTimestamp("fecha"));
                compra.setCantidad(rs.getInt("cantidad"));
                compra.setId_usuario(rs.getInt("id_usuario"));

                compras.add(compra);
            }
            System.out.println("Compras encontradas");

        } catch (SQLException e) {
            System.out.println("Error al obtener compras: " + e.getMessage());
        }

        return compras;
    }
    
    public List<Compra> findByIdUsuario(int id_usuario) {
        String query = "SELECT * FROM Compra WHERE id_usuario = ?";
        List<Compra> compras = new ArrayList<>();
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                int id_compra = rs.getInt("id_compra");
                Compra compra = new Compra(id_compra);
                compra.setId_compra(rs.getInt("id_compra"));
                compra.setSubtotal(rs.getFloat("subtotal"));
                compra.setFecha(rs.getTimestamp("fecha"));
                compra.setCantidad(rs.getInt("cantidad"));
                compra.setId_usuario(rs.getInt("id_usuario"));
    
                compras.add(compra);
            }
            System.out.println("Compras encontradas");
    
        } catch (SQLException e) {
            System.out.println("Error al obtener compras: " + e.getMessage());
        }
    
        return compras;
    }
    

}
