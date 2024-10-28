package controllers;

import database.DatabaseConnection;
import entities.Asiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.ResultSet;

public class AsientoController {

    Connection connection;

    public AsientoController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void createAsiento(Asiento asiento) {
        String query = "INSERT INTO Asiento (id_asiento, id_sala, letra_fila, numero_columna, estado) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, asiento.getIdAsiento());
            stmt.setInt(2, asiento.getIdSala());
            stmt.setString(3, asiento.getLetraFila());
            stmt.setInt(4, asiento.getNumeroColumna());
            stmt.setInt(5, 1);
            stmt.executeUpdate();
            System.out.println("Asiento insertado");

        } catch (SQLException e) {
            System.out.println("Error al insertar asiento: " + e.getMessage());
        }
    }

    public void createAllAsientos(int filas, int columnas, int id_sala) {
        String checkExistSQL = "SELECT COUNT(*) FROM asiento WHERE letra_fila = ? AND numero_columna = ? AND id_sala = ?";
        int totalAsientos = filas * columnas;

        char[] letrasFilas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        try (PreparedStatement pstmtCheck = connection.prepareStatement(checkExistSQL)) {

            int idAsiento = 1;
            int count = 0;

            for (int i = 1; i <= filas; i++) {
                char fila = letrasFilas[i - 1];

                for (int columna = 1; columna <= columnas; columna++) {
                    // Verifica si el asiento ya existe
                    pstmtCheck.setString(1, String.valueOf(fila));
                    pstmtCheck.setInt(2, columna);
                    pstmtCheck.setInt(3, id_sala);
                    ResultSet rs = pstmtCheck.executeQuery();

                    if (rs.next() && rs.getInt(1) == 0) { // Si no existe
                        Asiento asiento = new Asiento();
                        asiento.setIdAsiento(idAsiento);
                        asiento.setIdSala(id_sala);
                        asiento.setLetraFila(String.valueOf(fila));
                        asiento.setNumeroColumna(columna);
                        createAsiento(asiento);

                        count++;
                        idAsiento++;
                    }

                    if (count == totalAsientos) {
                        break;
                    }
                }
                if (count == totalAsientos) {
                    break;
                }
            }

            System.out.println("Asientos insertados");

        } catch (SQLException e) {
            System.out.println("Error al insertar asientos: " + e.getMessage());
        }

    }

    public List<Asiento> findAll(){
        String query = "SELECT a.id_asiento, a.id_sala, a.letra_fila, a.numero_columna, a.estado FROM Asiento a";
        List<Asiento> asientos = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Asiento asiento = new Asiento();
                asiento.setIdAsiento(rs.getInt("id_asiento"));
                asiento.setIdSala(rs.getInt("id_sala"));
                asiento.setLetraFila(rs.getString("letra_fila"));
                asiento.setNumeroColumna(rs.getInt("numero_columna"));
                asientos.add(asiento);
            }

            System.out.println("Asientos encontrados");
            
        } catch (SQLException e) {
            System.out.println("Error al listar asientos: " + e.getMessage());
        }

        return asientos;
    }

    public List<Asiento> findBySala(int id_sala){
        String query = "SELECT a.id_asiento, a.id_sala, a.letra_fila, a.numero_columna, a.estado FROM Asiento a WHERE id_sala = ?";
        List<Asiento> asientos = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(query)){
            
            stmt.setInt(1, id_sala);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Asiento asiento = new Asiento();
                asiento.setIdAsiento(rs.getInt("id_asiento"));
                asiento.setIdSala(rs.getInt("id_sala"));
                asiento.setLetraFila(rs.getString("letra_fila"));
                asiento.setNumeroColumna(rs.getInt("numero_columna"));
                asientos.add(asiento);
            }

            System.out.println("Asientos encontrados");
            
        } catch (SQLException e) {
            System.out.println("Error al listar asientos: " + e.getMessage());
        }

        return asientos;
    }
}