package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Funcion;
import entities.Pelicula;

public class FuncionController {
    Connection connection = DatabaseConnection.getInstance().connect();
    public FuncionController() {
                // this.connection = DatabaseConnection.getInstance().connect();

    }

    public List<Funcion> findAll() {
        String query = "SELECT * FROM Funcion ORDER BY id_funcion";
        List<Funcion> funciones = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcion funcion = new Funcion();
                funcion.setId_funcion(rs.getInt("id_funcion"));
                funcion.setId_pelicula(rs.getInt("id_pelicula"));
                funcion.setId_tipoFuncion(rs.getInt("id_tipoFuncion"));
                funcion.setFechaIngreso(rs.getDate("fecha_ingreso"));
                funcion.setFechaFinal(rs.getDate("fecha_final"));

                funciones.add(funcion);
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar funciones: " + e.getMessage());
        }

        return funciones;
    }

    public Funcion findById(int id_funcion) {
        String query = "SELECT * FROM Funcion WHERE id_funcion = ?";
        Funcion funcion = new Funcion();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_funcion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcion.setId_funcion(rs.getInt("id_funcion"));
                funcion.setId_pelicula(rs.getInt("id_pelicula"));
                funcion.setId_tipoFuncion(rs.getInt("id_tipoFuncion"));
                funcion.setFechaIngreso(rs.getDate("fecha_ingreso"));
                funcion.setFechaFinal(rs.getDate("fecha_final"));
            }
            System.out.println("Funcion encontrada");

        } catch (SQLException e) {
            System.out.println("Error al encontrar funcion: " + e.getMessage());
        }

        return funcion;
    }

    public void createFuncion(Funcion funcion){
        String query = "INSERT INTO Funcion (id_pelicula, id_tipoFuncion, fecha_ingreso, fecha_final) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, funcion.getId_pelicula());
            stmt.setInt(2, funcion.getId_tipoFuncion());
            stmt.setDate(3, funcion.getFechaIngreso());
            stmt.setDate(4, funcion.getFechaFinal());

            stmt.executeQuery();

            System.out.println("Funcion creada con exito");

        }catch(SQLException e){
            System.out.println("Error al crear funcion: " + e.getMessage());
        }
    }
}