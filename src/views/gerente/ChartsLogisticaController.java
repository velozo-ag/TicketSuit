package views.gerente;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import views.MainController;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;
/**1 */
public class ChartsLogisticaController {

    private MainController mainController = new MainController();

    private DatabaseConnection databaseConnection = new DatabaseConnection();
    private Connection connection;
    @FXML
    private Pane mainPanel;
    @FXML
    public StackedBarChart<String, Number> capacidadSalasChart;
    @FXML
    private BarChart<String, Number> barChartPeliculasPorGenero;
    @FXML
    private BarChart<String, Number> barChartFuncionesPorDia;

    public ChartsLogisticaController() {
        this.connection = DatabaseConnection.getInstance().getConnection(); 
    }

    private void loadPeliculasPorGeneroData() {
        barChartPeliculasPorGenero.getData().clear(); 

        String query = "SELECT g.descripcion AS genero, COUNT(gp.id_pelicula) AS cantidad_peliculas " +
                       "FROM Genero g " +
                       "JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero " +
                       "GROUP BY g.descripcion " +
                       "ORDER BY cantidad_peliculas DESC;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Cantidad de Películas por Género");

            while (rs.next()) {
                String genero = rs.getString("genero");
                int cantidadPeliculas = rs.getInt("cantidad_peliculas");

                series.getData().add(new XYChart.Data<>(genero, cantidadPeliculas));
            }

            barChartPeliculasPorGenero.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarCapacidadSalas() {
        XYChart.Series<String, Number> capacidadTotalSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> capacidadUtilizadaSeries = new XYChart.Series<>();
        
        String query = "SELECT S.nombre AS sala, S.capacidad AS capacidad_total, COUNT(T.id_ticket) AS capacidad_utilizada " +
                       "FROM Sala S LEFT JOIN Sala_Funcion SF ON S.id_sala = SF.id_sala " +
                       "LEFT JOIN Ticket T ON SF.id_funcion = T.id_funcion GROUP BY S.nombre, S.capacidad " +
                       "ORDER BY S.nombre;";

        capacidadTotalSeries.setName("Capacidad Total");
        capacidadUtilizadaSeries.setName("Capacidad Utilizada");

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String sala = rs.getString("sala");
                int capacidadTotal = rs.getInt("capacidad_total");
                int capacidadUtilizada = rs.getInt("capacidad_utilizada");
                capacidadUtilizadaSeries.getData().add(new XYChart.Data<>(sala, capacidadUtilizada));
                capacidadTotalSeries.getData().add(new XYChart.Data<>(sala, capacidadTotal - capacidadUtilizada));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de capacidad de salas: " + e.getMessage());
        }

        capacidadSalasChart.getData().addAll(capacidadTotalSeries, capacidadUtilizadaSeries);
    }

    private void loadFuncionesPorDiaSemanaActual() {
        barChartFuncionesPorDia.getData().clear(); 
    
        String query = "SELECT f.fecha AS fecha, COUNT(f.id_funcion) AS cantidad_funciones " +
                       "FROM Funcion f " +
                       "WHERE DATEPART(WEEK, f.fecha) = DATEPART(WEEK, GETDATE()) " +
                       "AND YEAR(f.fecha) = YEAR(GETDATE()) " +
                       "GROUP BY f.fecha " +
                       "ORDER BY f.fecha;";
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Funciones por Día (Semana Actual)");
    
            while (rs.next()) {
                String fecha = rs.getDate("fecha").toString();
                int cantidadFunciones = rs.getInt("cantidad_funciones");
    
                series.getData().add(new XYChart.Data<>(fecha, cantidadFunciones));
            }
    
            barChartFuncionesPorDia.getData().add(series);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        cargarCapacidadSalas();
        loadPeliculasPorGeneroData();
        loadFuncionesPorDiaSemanaActual();
    }


    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ChartsIngresos(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/gerente/ChartsIngresos.fxml"));
            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(event, panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ChartsAsistencias(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/gerente/ChartsAsistencia.fxml"));
            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(event, panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ChartsLogistica(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/gerente/ChartsLogistica.fxml"));
            Scene scene = new Scene(root);
            Pane panel = (Pane) scene.lookup("#mainPanel");
            mainController.setUpScene(event, panel, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toPGerente(ActionEvent event) {
        try {
            mainController.toPGerente(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
