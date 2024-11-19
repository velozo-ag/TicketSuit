package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import entities.Director;

public class DirectorController {
    Connection connection = DatabaseConnection.getInstance().connect();
    public DirectorController(){
        // this.connection = DatabaseConnection.getInstance().connect();

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

        // Método para buscar un director por nombre (sin distinguir mayúsculas)
        public Director findByName(String nombre) {
            Director director = null;
            String query = "SELECT * FROM Director WHERE LOWER(nombre) = LOWER(?)";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    director = new Director();
                    director.setIdDirector(resultSet.getInt("id_director"));
                    director.setNombre(resultSet.getString("nombre"));
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar director por nombre: " + e.getMessage());
            }
    
            return director;
        }
    
        // Método para crear un nuevo director en la base de datos
        public void createDirector(Director director) {
            String query = "INSERT INTO Director (nombre) VALUES (?)";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, director.getNombre());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error al crear director: " + e.getMessage());
            }
        }
}
