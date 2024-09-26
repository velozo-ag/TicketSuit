package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Perfil;

public class PerfilController {

    private Connection connection;

    public PerfilController(Connection connection) {
        this.connection = connection;
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
}
