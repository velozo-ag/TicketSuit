package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Genero;

public class GeneroController {
    private Connection connection;

    public GeneroController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Genero> findAll() {
        String query = "SELECT Genero.id_genero, Genero.descripcion FROM Genero";
        List<Genero> generos = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setIdGenero(rs.getInt("id_genero"));
                genero.setDescripcion(rs.getString("descripcion"));

                generos.add(genero);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer generos: " + e.getMessage());
        }

        return generos;
    }

    public List<Genero> findByPelicula(int id_pelicula) {
        String query = "SELECT Genero.id_genero, Genero.descripcion FROM Genero " +
                "INNER JOIN Genero_Pelicula ON Genero.id_genero = Genero_Pelicula.id_genero " +
                "WHERE Genero_Pelicula.id_pelicula = ?";
        List<Genero> generos = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_pelicula);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setIdGenero(rs.getInt("id_genero"));
                genero.setDescripcion(rs.getString("descripcion"));

                generos.add(genero);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer generos: " + e.getMessage());
        }

        return generos;
    }

}
