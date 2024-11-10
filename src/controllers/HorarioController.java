package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import entities.Horario;

public class HorarioController {
    Connection connection;

    public HorarioController() {
        this.connection = DatabaseConnection.getInstance().connect();
    }

    public List<Horario> findBySala(int id_sala) {
        // sf.inicio_funcion > h.horario AND sf.final_funcion < h.horario
        String query = "SELECT * FROM Horario h " +
                "LEFT JOIN Sala_Funcion sf ON sf.id_sala = ? " +
                "WHERE h.horario NOT BETWEEN CAST(sf.inicio_funcion AS TIME) AND DATEADD(MINUTE, 30, CAST(sf.final_funcion AS TIME)) "
                +
                "OR sf.inicio_funcion IS NULL OR sf.final_funcion IS NULL";

        List<Horario> horarios = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_sala);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Horario horario = new Horario();
                horario.setId_horario(rs.getInt("id_horario"));
                horario.setHorario(rs.getTime("horario"));
                horario.setEstado(rs.getInt("estado"));

                horarios.add(horario);
            }

            System.out.println("Horarios encontrados");

        } catch (SQLException e) {
            System.out.println("Error al encontrar horarios: " + e.getMessage());
        }

        return horarios;

    }

    public List<Horario> findBySalaDuracion(int id_sala, int duracion, Date fecha) {
        // String query = "SELECT h.* FROM Horario h WHERE NOT EXISTS ( SELECT 1 FROM Sala_Funcion sf  WHERE sf.id_sala = ? AND ( (h.horario BETWEEN DATEADD(MINUTE, ?, CAST(sf.inicio_funcion AS TIME)) AND DATEADD(MINUTE, 30, CAST(sf.final_funcion AS TIME)))  OR (CAST(sf.inicio_funcion AS TIME) > CAST(sf.final_funcion AS TIME) AND  (h.horario < DATEADD(MINUTE, 30, CAST(sf.final_funcion AS TIME)) OR h.horario > DATEADD(MINUTE, ?, CAST(sf.inicio_funcion AS TIME))))  ) ) OR NOT EXISTS (SELECT 1 FROM Sala_Funcion sf WHERE sf.id_sala = ?) ";
        String query = "SELECT h.* FROM Horario h WHERE NOT EXISTS" +
                        "(SELECT 1 FROM Sala_Funcion sf WHERE sf.id_sala = ? " +
                        "   AND ( " +
                        "       (h.horario BETWEEN DATEADD (MINUTE, ? , CAST(sf.inicio_funcion AS TIME)) AND DATEADD  (MINUTE, 30, CAST(sf.final_funcion AS TIME))) " +
                        "       OR " +
                        "       (CAST(sf.inicio_funcion AS TIME) > CAST(sf.final_funcion AS TIME) " +
                        "       AND " +
                        "           (h.horario < DATEADD (MINUTE, 30, CAST(sf.final_funcion AS TIME)) " +
                        "           OR h.horario > DATEADD (MINUTE, ? , CAST(sf.inicio_funcion AS TIME))) " +
                        "       )" +
                        "   ) " +
                        "   AND (" +
                        "       CAST(sf.inicio_funcion AS DATE) = ? " +
                        "   )" +
                        ")" +
                        "OR NOT EXISTS (SELECT 1 FROM Sala_Funcion sf WHERE sf.id_sala = ? )";


        List<Horario> horarios = new ArrayList<>();

        FuncionController funcionController = new FuncionController();
        PeliculaController peliculaController = new PeliculaController();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_sala);
            stmt.setInt(2, -duracion - 30);
            stmt.setInt(3, -duracion - 30);
            stmt.setDate(4, fecha);
            System.out.println(fecha);
            stmt.setInt(5, id_sala);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Horario horario = new Horario();
                horario.setId_horario(rs.getInt("id_horario"));
                horario.setHorario(rs.getTime("horario"));
                horario.setEstado(rs.getInt("estado"));

                horarios.add(horario);
            }

            System.out.println("Horarios encontrados");

        } catch (SQLException e) {
            System.out.println("Error al encontrar horarios: " + e.getMessage());
        }

        return horarios;

    }

}
