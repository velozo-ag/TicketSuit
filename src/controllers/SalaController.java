package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import entities.Sala;

public class SalaController {
    Connection connection;

    public SalaController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Sala findById(int id_sala) {
        String query = "SELECT Sala.id_sala, Sala.nombre, Sala.capacidad, Sala.capacidad, Sala.id_cine FROM Sala WHERE id_sala = ?";
        Sala sala = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_sala);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setNombre(rs.getString("nombre"));
                sala.setCapacidad(rs.getInt("capacidad"));
                sala.setIdCine(rs.getInt("id_cine"));
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar sala: " + e.getMessage());
        }

        return sala;
    }

    // public Sala findByFuncion(int id_funcion) {
    //     String query = "SELECT Sala.id_sala, Sala.nombre, Sala.capacidad, Sal.cpacidad FROM Sala " +
    //             "INNER JOIN Sala_Funcion ON Sala.id_sala = Sala_Funcion.id_sala " +
    //             "WHERE Sala_Funcion.id_funcion = ?";
    // }
}
