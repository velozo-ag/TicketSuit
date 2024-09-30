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
    Connection connection;

    public FuncionController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Funcion> findAll(){
        String query = "SELECT Funcion.id_funcion, Funcion.hora_inicio, Funcion.hora_final, Funcion.fecha, Funcion.id_pelicula, Funcion.id_tipoFuncion FROM Funcion";
        List<Funcion> funciones = new ArrayList<>();

        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcion funcion = new Funcion();
                funcion.setIdFuncion(rs.getInt("id_funcion"));
                funcion.setHora_inicio(rs.getString("hora_inicio"));
                funcion.setHora_final(rs.getString("hora_final"));
                funcion.setFecha(rs.getDate("fecha"));
                funcion.setIdPelicula(rs.getInt("id_pelicula"));
                funcion.setIdTipoFuncion(rs.getInt("id_tipoFuncion"));
                // funcion.set

                funciones.add(funcion);
            }
        }catch(SQLException e){
            System.out.println("Error al encontrar peliculas: " + e.getMessage());
        }

        return funciones;
    }
}
