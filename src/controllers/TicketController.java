package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Compra;
import entities.Sala_Funcion;
import entities.Ticket;

public class TicketController {
    Connection connection = DatabaseConnection.getInstance().connect();

    public TicketController() {
                // this.connection = DatabaseConnection.getInstance().connect();

    }

    public int createTicket(Ticket ticket) {
        String query = "INSERT INTO Ticket (id_funcion, id_sala, inicio_funcion, id_asiento, id_compra, valor) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ticket.getId_funcion());
            stmt.setInt(2, ticket.getId_sala());
            stmt.setTimestamp(3, ticket.getInicioFuncion());
            stmt.setInt(4, ticket.getId_asiento());
            stmt.setInt(5, ticket.getId_compra());
            stmt.setFloat(6, ticket.getValor());

            stmt.executeUpdate();
            System.out.println("Ticket insertado");

        } catch (SQLException e) {
            System.out.println("Error al insertar ticket: " + e.getMessage());
        }

        return obtenerUltimoIdTicket();
    }

    public List<Ticket> findByIdCompra(int id_compra) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Ticket WHERE id_compra = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_compra);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setId_ticket(rs.getInt("id_ticket"));
                    ticket.setId_funcion(rs.getInt("id_funcion"));
                    ticket.setId_sala(rs.getInt("id_sala"));
                    ticket.setInicioFuncion(rs.getTimestamp("inicio_funcion"));
                    ticket.setId_asiento(rs.getInt("id_asiento"));
                    ticket.setId_compra(rs.getInt("id_compra"));
                    ticket.setValor(rs.getFloat("valor"));

                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los tickets: " + e.getMessage());
        }

        return tickets;
    }

    public int obtenerUltimoIdTicket() {
        String query = "SELECT TOP 1 id_ticket FROM Ticket ORDER BY id_ticket DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener id ticket: " + e.getMessage());
        }
        return -1;
    }

    public int cantVendidoPorFuncion(Sala_Funcion funcion) {
    int cantidad = 0;

    // Tu consulta SQL
    String query = "SELECT COUNT(*) AS cantidad " +
                 "FROM Ticket " +
                 "WHERE id_funcion = ? AND id_sala = ? AND inicio_funcion = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, funcion.getId_funcion());
        stmt.setInt(2, funcion.getId_sala());
        stmt.setTimestamp(3, Timestamp.valueOf(funcion.getInicioFuncion().toString()));

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cantidad = rs.getInt("cantidad");
        }
    } catch (SQLException e) {
        System.out.println("Error al conseguir cantidad de tickets: " + e.getMessage()); 
    }

    return cantidad;
}

}
