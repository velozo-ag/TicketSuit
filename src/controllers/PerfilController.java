package controllers;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Perfil;

public class PerfilController {

    private Connection connection;

    public PerfilController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Perfil> findAll() {
        List<Perfil> perfiles = new ArrayList<>();
        String query = "SELECT * FROM Perfil";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Perfil perfil = new Perfil();
                perfil.setIdPerfil(rs.getInt("id_perfil"));
                perfil.setNombre(rs.getString("nombre"));
                perfiles.add(perfil);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer perfiles: " + e.getMessage());
        }
        return perfiles;
    }

    public Perfil findById(int id_perfil){
        Perfil perfil = null;
        String query = "SELECT * FROM Perfil WHERE id_perfil = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id_perfil);
            ResultSet resultSet = stmt.executeQuery();
            System.out.println("Perfil encontrado");

            if(resultSet.next()){
                perfil = new Perfil(resultSet.getInt("id_perfil"), resultSet.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }

        return perfil;
    }
}
