package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Funcion;
import entities.TipoFuncion;

public class TipoFuncionController {
    Connection connection = DatabaseConnection.getInstance().connect();
    public TipoFuncionController() {
                // this.connection = DatabaseConnection.getInstance().connect();

    }

    public TipoFuncion findById(int id_tipoFuncion) {
        String query = "SELECT TipoFuncion.id_tipoFuncion, TipoFuncion.descripcion, TipoFuncion.precio FROM TipoFuncion WHERE id_tipoFuncion = ?";
        TipoFuncion tipoFuncion = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_tipoFuncion);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tipoFuncion = new TipoFuncion();
                tipoFuncion.setIdTipoFuncion(rs.getInt("id_tipoFuncion"));
                tipoFuncion.setDescripcion(rs.getString("descripcion"));
                tipoFuncion.setPrecio(rs.getFloat("precio"));
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar tipo de funcion: " + e.getMessage());
        }

        return tipoFuncion;
    }

    public List<TipoFuncion> findAll() {
        String query = "SELECT * FROM TipoFuncion ORDER BY id_tipoFuncion";
        List<TipoFuncion> tiposFuncion = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TipoFuncion tipoFuncion = new TipoFuncion();
                tipoFuncion.setIdTipoFuncion(rs.getInt("id_tipoFuncion"));
                tipoFuncion.setDescripcion(rs.getString("descripcion"));
                tipoFuncion.setPrecio(rs.getFloat("precio"));

                tiposFuncion.add(tipoFuncion);
            }

            System.out.println("Tipos de funcion encontradas");
        } catch (SQLException e) {
            System.out.println("Error al encontrar tipo de funcion: " + e.getMessage());
        }

        return tiposFuncion;
    }
}
