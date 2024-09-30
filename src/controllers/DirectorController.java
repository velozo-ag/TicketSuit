package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import entities.Director;

public class DirectorController {
    Connection connection;

    public DirectorController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Director findById(int id_director){
        String query = "SELECT Director.id_director, Director.nombre FROM Director WHERE id_director = ?";
        Director director = null;
        
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_director);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                director = new Director();
                director.setIdDirector(rs.getInt("id_director"));
                director.setNombre(rs.getString("nombre"));
            }
        }catch (SQLException e){
            System.out.println("Error al buscar director: " + e.getMessage());
        }

        return director;
    }
}
