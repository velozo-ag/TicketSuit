package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Funcion;
import entities.Sala_Funcion;

public class Sala_FuncionController {
    Connection connection;

    public Sala_FuncionController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Sala_Funcion> findByIdSala(int id_sala) {
        String query = "SELECT * FROM Sala_Funcion WHERE id_sala = ?";
        List<Sala_Funcion> funciones = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, id_sala);;
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sala_Funcion funcion = new Sala_Funcion();
                funcion.setId_funcion(rs.getInt("id_funcion"));
                funcion.setId_sala(rs.getInt("id_sala"));
                funcion.setInicioFuncion(rs.getTimestamp("inicio_funcion"));
                funcion.setFinalFuncion(rs.getTimestamp("final_funcion"));

                funciones.add(funcion);
            }

            System.out.println("Sala_Funcion encontrados");
            
        } catch (SQLException e) {
            System.out.println("Error al encontrar funciones: " + e.getMessage());
        }

        return funciones;
    }

    public void createSalaFuncion(Sala_Funcion sala_Funcion){
        String query = "INSERT INTO Sala_Funcion (id_funcion, id_sala, inicio_funcion, final_funcion) VALUES (?, ?, ?, ?)";
        
        try(PreparedStatement stmt = connection.prepareStatement(query)){

            stmt.setInt(1, sala_Funcion.getId_funcion());
            stmt.setInt(2, sala_Funcion.getId_sala());
            stmt.setString(3, sala_Funcion.getInicioFuncion().toString());
            stmt.setString(4, sala_Funcion.getFinalFuncion().toString());

            stmt.executeUpdate();
            
            System.out.println("SALA_FUNCION insertado con exito");

        }catch(SQLException e){
            System.out.println("Error al insertar Sala_Funcion: " + e.getMessage());
        }
    }

    public List<Sala_Funcion> findByIdPelicula(int id_pelicula){
        String query = "SELECT sf.* FROM Sala_Funcion sf INNER JOIN Funcion f ON f.id_funcion = sf.id_funcion WHERE f.id_pelicula = ?";
        List<Sala_Funcion> funciones = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, id_pelicula);;
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sala_Funcion funcion = new Sala_Funcion();
                funcion.setId_funcion(rs.getInt("id_funcion"));
                funcion.setId_sala(rs.getInt("id_sala"));
                funcion.setInicioFuncion(rs.getTimestamp("inicio_funcion"));
                funcion.setFinalFuncion(rs.getTimestamp("final_funcion"));

                funciones.add(funcion);
            }

            System.out.println("Sala_Funcion encontrados");
            
        } catch (SQLException e) {
            System.out.println("Error al encontrar funciones: " + e.getMessage());
        }

        return funciones;
    }

    public List<Sala_Funcion> findByPeliculaFecha(int id_pelicula, Date fecha){
        String query = "SELECT sf.* FROM Sala_Funcion sf " + 
                    "JOIN Funcion f ON f.id_funcion = sf.id_funcion " + 
                    "WHERE f.id_pelicula = ? " + 
                    "AND CAST(sf.inicio_funcion AS DATE) = ? " +
                    "AND sf.inicio_funcion >= GETDATE()";
                    // "AND CAST(sf.inicio_funcion AS TIME) >= CAST(GETDATE() AS TIME)";
        List<Sala_Funcion> funciones = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, id_pelicula);
            stmt.setDate(2, fecha);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sala_Funcion funcion = new Sala_Funcion();
                funcion.setId_funcion(rs.getInt("id_funcion"));
                funcion.setId_sala(rs.getInt("id_sala"));
                funcion.setInicioFuncion(rs.getTimestamp("inicio_funcion"));
                funcion.setFinalFuncion(rs.getTimestamp("final_funcion"));

                funciones.add(funcion);
            }

            System.out.println("Sala_Funcion encontrados");
            
        } catch (SQLException e) {
            System.out.println("Error al encontrar funciones: " + e.getMessage());
        }

        return funciones;
    }

    public List<Date> findDias(){
        String query = "SELECT DISTINCT CAST(sf.inicio_funcion AS DATE) AS fecha FROM sala_funcion sf " + 
                        "WHERE sf.inicio_funcion >= GETDATE() " + 
                        "GROUP BY CAST(sf.inicio_funcion AS DATE)";
        List<Date> dias = new ArrayList<>();
        
        try(PreparedStatement stmt = connection.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Date dia = rs.getDate("fecha");
                dias.add(dia);
            }

        }catch(SQLException e){
            System.out.println("Error al conseguir fechas: " + e.getMessage());
        }

        return dias;
    }
}
