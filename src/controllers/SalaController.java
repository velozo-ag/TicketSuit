package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Sala;
import entities.Usuario;

public class SalaController {
    Connection connection;
    AsientoController asientoController = new AsientoController();

    public SalaController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void createSala(Sala sala) {
        String query = "INSERT INTO Sala (nombre, capacidad, cantColumnas, cantFilas, id_cine) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, sala.getNombre());
            stmt.setInt(2, sala.getCapacidad());
            stmt.setInt(3, sala.getCantColumnas());
            stmt.setInt(4, sala.getCantFilas());
            stmt.setInt(5, sala.getIdCine());
            stmt.executeUpdate();

            System.out.println("Sala creada");

        } catch (SQLException e) {
            System.out.println("Error al crear sala: " + e.getMessage());
        }
    }

    
    public void updateSala(Sala sala) {
        String query = "UPDATE Sala SET nombre = ?, capacidad = ?, cantFilas = ?, cantColumnas = ?, estado = ? WHERE id_sala = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, sala.getNombre());
            stmt.setInt(2, sala.getCapacidad());
            stmt.setInt(3, sala.getCantFilas());
            stmt.setInt(4, sala.getCantColumnas());
            stmt.setInt(5, sala.getEstado());
            stmt.setInt(6, sala.getIdSala());
            stmt.executeUpdate();
            System.out.println("Sala actualizada");

        } catch (SQLException e) {
            System.out.println("Error al actualizar sala: " + e.getMessage());
        }
    }


    public Sala findById(int id_sala) {
        String query = "SELECT s.id_sala, s.nombre, s.capacidad, s.capacidad, s.id_cine, s.cantColumnas, s.cantFilas, s.estado FROM Sala s WHERE id_sala = ?";
        Sala sala = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_sala);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setNombre(rs.getString("nombre"));
                sala.setCapacidad(rs.getInt("capacidad"));
                sala.setCantColumnas(rs.getInt("cantColumnas"));
                sala.setCantFilas(rs.getInt("cantFilas"));
                sala.setIdCine(rs.getInt("id_cine"));
                sala.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar sala: " + e.getMessage());
        }

        return sala;
    }

    public Sala findByNombre(String nombre) {
        String query = "SELECT s.id_sala, s.nombre, s.capacidad, s.capacidad, s.id_cine, s.cantColumnas, s.cantFilas, s.estado FROM Sala s WHERE nombre LIKE ?";
        Sala sala = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, nombre);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setNombre(rs.getString("nombre"));
                sala.setCapacidad(rs.getInt("capacidad"));
                sala.setCantColumnas(rs.getInt("cantColumnas"));
                sala.setCantFilas(rs.getInt("cantFilas"));
                sala.setIdCine(rs.getInt("id_cine"));
                sala.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar sala: " + e.getMessage());
        }

        return sala;

    }

    public int eliminarFuncionesSinTickets(int idSala) {
        String query = """
            DELETE FROM Sala_Funcion
            WHERE id_sala = ?
            AND NOT EXISTS (
                SELECT 1
                FROM Ticket
                WHERE Ticket.id_funcion = Sala_Funcion.id_funcion
                AND Ticket.id_sala = Sala_Funcion.id_sala
                AND Ticket.inicio_funcion = Sala_Funcion.inicio_funcion
            )
        """;
        
        int rowsDeleted = 0;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idSala); // Configura el parámetro de la consulta
            rowsDeleted = stmt.executeUpdate(); // Ejecuta la eliminación
            System.out.println(rowsDeleted + " funciones eliminadas para la sala con ID: " + idSala);
    
        } catch (SQLException e) {
            System.err.println("Error al eliminar funciones sin tickets para la sala con ID " + idSala + ": " + e.getMessage());
        }
    
        return rowsDeleted; // Devuelve la cantidad de funciones eliminadas
    }

    public int contarFuncionesSinTickets(int idSala) {
        String query = """
            SELECT COUNT(*)
            FROM Sala_Funcion sf
            WHERE sf.id_sala = ?
            AND NOT EXISTS (
                SELECT 1
                FROM Ticket t
                WHERE t.id_funcion = sf.id_funcion
                AND t.id_sala = sf.id_sala
                AND t.inicio_funcion = sf.inicio_funcion
            );
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idSala);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar funciones sin tickets: " + e.getMessage());
        }
        return 0;
    }

    public int contarFuncionesConTickets(int idSala) {
        String query = """
            SELECT COUNT(DISTINCT sf.id_funcion)
            FROM Sala_Funcion sf
            INNER JOIN Ticket t ON t.id_funcion = sf.id_funcion
            WHERE sf.id_sala = ?;
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idSala);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar funciones con tickets: " + e.getMessage());
        }
        return 0;
    }

    public List<Sala> findAll(){
        String query = "SELECT s.id_sala, s.nombre, s.capacidad, s.cantFilas, s.cantColumnas, s.id_cine, s.estado FROM Sala s";
        List<Sala> salas = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setIdCine(rs.getInt("id_cine"));
                sala.setNombre(rs.getString("nombre"));
                sala.setCapacidad(rs.getInt("capacidad"));
                sala.setCantFilas(rs.getInt("cantFilas"));
                sala.setCantColumnas(rs.getInt("cantColumnas"));
                sala.setEstado(rs.getInt("estado"));
                salas.add(sala);
            }
            
            System.out.println("Salas encontradas");

        }catch(SQLException e){
            System.out.println("Error al encontrar salas: " + e.getMessage());
        }
    
        return salas;
    }

    // public Sala findByFuncion(int id_funcion) {
    // String query = "SELECT Sala.id_sala, Sala.nombre, Sala.capacidad,
    // Sal.cpacidad FROM Sala " +
    // "INNER JOIN Sala_Funcion ON Sala.id_sala = Sala_Funcion.id_sala " +
    // "WHERE Sala_Funcion.id_funcion = ?";
    // }
}
