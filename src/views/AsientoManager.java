package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class AsientoManager {

    public static void insertarAsientos() {
        DatabaseConnection dbController = new DatabaseConnection();
        Connection connection = dbController.connect();

        String insertSQL = "INSERT INTO asiento (id_asiento, letra_fila, numero_columna, id_sala) VALUES (?, ?, ?, ?)";
        String checkExistSQL = "SELECT COUNT(*) FROM asiento WHERE letra_fila = ? AND numero_columna = ? AND id_sala = ?";
        int totalAsientos = 56;
        int columnas = 8;
        int filas = totalAsientos / columnas;

        char[] letrasFilas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        try (PreparedStatement pstmtInsert = connection.prepareStatement(insertSQL);
             PreparedStatement pstmtCheck = connection.prepareStatement(checkExistSQL)) {

            connection.setAutoCommit(false);

            int idSala = 1; // Cambiar a 1, 2, etc Dependiendo de la sala
            int idAsiento = 1;
            int count = 0;

            for (int i = 1; i <= filas; i++) {
                char fila = letrasFilas[i - 1];

                for (int columna = 1; columna <= columnas; columna++) {
                    // Verificar si el asiento ya existe
                    pstmtCheck.setString(1, String.valueOf(fila));
                    pstmtCheck.setInt(2, columna);
                    pstmtCheck.setInt(3, idSala);
                    ResultSet rs = pstmtCheck.executeQuery();

                    if (rs.next() && rs.getInt(1) == 0) { // Si no existe
                        pstmtInsert.setInt(1, idAsiento);
                        pstmtInsert.setString(2, String.valueOf(fila));
                        pstmtInsert.setInt(3, columna);
                        pstmtInsert.setInt(4, idSala);
                        pstmtInsert.addBatch();
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

            pstmtInsert.executeBatch();
            connection.commit(); // Hacer commit de la transacción
            System.out.println("Asientos insertados exitosamente!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}