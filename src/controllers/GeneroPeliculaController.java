package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DatabaseConnection;

public class GeneroPeliculaController {
    Connection connection = DatabaseConnection.getInstance().connect();

    public void createGeneroPelicula(int id_genero, int id_pelicula) {
        String query = "INSERT INTO Genero_Pelicula (id_pelicula, id_genero) VALUES (?, ?)";

        System.out.println(id_genero);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_pelicula);
            preparedStatement.setInt(2, id_genero);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al crear generoPelicula: " + e.getMessage());
        }
    }
}
