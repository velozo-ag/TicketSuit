package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import entities.Clasificacion;

public class ClasificacionController {
    Connection connection;

    public ClasificacionController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Clasificacion findById(int id_clasificacion){
        String query = "SELECT Clasificacion.id_clasificacion, Clasificacion.descripcion, Clasificacion.nombre FROM Clasificacion WHERE id_clasificacion = ?";
        Clasificacion clasificacion = null;

        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_clasificacion);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                clasificacion = new Clasificacion();
                clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
                clasificacion.setDescripcion(rs.getString("descripcion"));
                clasificacion.setNombre(rs.getString("nombre"));
            }
        }catch(SQLException e){
            System.out.println("Error al buscar clasificacion: " + e.getMessage());
        }

        return clasificacion;
    }
}
