package views.gerente;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import views.MainController;
/**1 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;

public class ChartsAsistenciaController {

    private MainController mainController = new MainController();
    @FXML
    private BarChart<String, Number> barChartAsistenciaPorPelicula;
    private Connection connection;
    @FXML
    private BarChart<String, Number> barChartAsistenciaPorGenero;
    @FXML
    private PieChart pieChartAsistenciaPorClasificacion;
    @FXML
    private LineChart <String, Number> lineChartAsistencia;
    @FXML
    private ComboBox<String> filterComboBoxAsistencia;

    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChartsAsistenciaController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    private void loadAsistenciaPorPelicula() {
    barChartAsistenciaPorPelicula.getData().clear(); // Limpiar los datos previos

    String query = "SELECT p.nombre AS pelicula, COUNT(t.id_ticket) AS total_asistencia " +
                   "FROM Pelicula p " +
                   "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                   "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                   "GROUP BY p.nombre " +
                   "ORDER BY total_asistencia DESC;";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Asistencia por Película");

        while (rs.next()) {
            String pelicula = rs.getString("pelicula");
            int totalAsistencia = rs.getInt("total_asistencia");

            // Añadir los datos al gráfico de asistencia por película
            series.getData().add(new XYChart.Data<>(pelicula, totalAsistencia));
        }

        barChartAsistenciaPorPelicula.getData().add(series);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    private void loadAsistenciaPorGenero() {
        barChartAsistenciaPorGenero.getData().clear(); // Limpiar los datos previos
    
        String query = "SELECT g.descripcion AS genero, COUNT(t.id_ticket) AS total_asistencia " +
                       "FROM Genero g " +
                       "JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero " +
                       "JOIN Pelicula p ON gp.id_pelicula = p.id_pelicula " +
                       "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                       "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                       "GROUP BY g.descripcion " +
                       "ORDER BY total_asistencia DESC;";
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Asistencia por Género");
    
            while (rs.next()) {
                String genero = rs.getString("genero");
                int totalAsistencia = rs.getInt("total_asistencia");
    
                // Añadir los datos al gráfico de asistencia por género
                series.getData().add(new XYChart.Data<>(genero, totalAsistencia));
            }
    
            barChartAsistenciaPorGenero.getData().add(series);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAsistenciaPorClasificacion() {
    pieChartAsistenciaPorClasificacion.getData().clear(); // Limpiar los datos previos

    String query = "SELECT cl.nombre AS clasificacion, COUNT(t.id_ticket) AS total_asistencia " +
                   "FROM Clasificacion cl " +
                   "JOIN Pelicula p ON cl.id_clasificacion = p.id_clasificacion " +
                   "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                   "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                   "GROUP BY cl.nombre " +
                   "ORDER BY total_asistencia DESC;";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        // Añadir los datos al PieChart
        while (rs.next()) {
            String clasificacion = rs.getString("clasificacion");
            int totalAsistencia = rs.getInt("total_asistencia");

            PieChart.Data slice = new PieChart.Data(clasificacion, totalAsistencia);
            pieChartAsistenciaPorClasificacion.getData().add(slice);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    private void populateComboBoxAsistencia() {
        ObservableList<String> filtersAsistencia = FXCollections.observableArrayList("Total", "Año", "Trimestre", "Semana");
        filterComboBoxAsistencia.setItems(filtersAsistencia);
        filterComboBoxAsistencia.setValue("Total"); // Valor por defecto
    }

    private void loadAsistenciaGeneralData(String filter) {
        lineChartAsistencia.getData().clear(); // Limpiar los datos previos
    
        String query = buildQueryForAsistencia(filter);
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Asistencia General");
    
            while (rs.next()) {
                String label;
                int totalAsistencia = rs.getInt("total_asistencia");
    
                // Define las etiquetas dependiendo del filtro
                switch (filter) {
                    case "Año":
                        label = String.valueOf(rs.getInt("mes")); // Meses del año actual
                        break;
                    case "Trimestre":
                        label = "Semana " + rs.getInt("semana"); // Semanas del trimestre actual
                        break;
                    case "Semana":
                        label = "Día " + rs.getInt("dia"); // Días de la semana actual
                        break;
                    default: // Total (Por Años)
                        label = String.valueOf(rs.getInt("anio")); // Años
                        break;
                }
    
                series.getData().add(new XYChart.Data<>(label, totalAsistencia));
            }
    
            lineChartAsistencia.getData().add(series);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String buildQueryForAsistencia(String filter) {
        String query;
        switch (filter) {
            case "Año":
                query = "SELECT MONTH(f.fecha) AS mes, COUNT(t.id_ticket) AS total_asistencia " +
                        "FROM Funcion f " +
                        "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                        "WHERE YEAR(f.fecha) = YEAR(GETDATE()) " +
                        "GROUP BY MONTH(f.fecha) " +
                        "ORDER BY mes;";
                break;
            case "Trimestre":
                query = "SELECT DATEPART(WEEK, f.fecha) AS semana, COUNT(t.id_ticket) AS total_asistencia " +
                        "FROM Funcion f " +
                        "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                        "WHERE YEAR(f.fecha) = YEAR(GETDATE()) " +
                        "AND DATEPART(QUARTER, f.fecha) = DATEPART(QUARTER, GETDATE()) " +
                        "GROUP BY DATEPART(WEEK, f.fecha) " +
                        "ORDER BY semana;";
                break;
            case "Semana":
                query = "SELECT DATEPART(WEEKDAY, f.fecha) AS dia, COUNT(t.id_ticket) AS total_asistencia " +
                        "FROM Funcion f " +
                        "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                        "WHERE YEAR(f.fecha) = YEAR(GETDATE()) " +
                        "AND DATEPART(WEEK, f.fecha) = DATEPART(WEEK, GETDATE()) " +
                        "GROUP BY DATEPART(WEEKDAY, f.fecha) " +
                        "ORDER BY dia;";
                break;
            default: // Total (Por Años)
                query = "SELECT YEAR(f.fecha) AS anio, COUNT(t.id_ticket) AS total_asistencia " +
                        "FROM Funcion f " +
                        "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                        "GROUP BY YEAR(f.fecha) " +
                        "ORDER BY anio;";
                break;
        }
        return query;
    }


    public void initialize() {
        populateComboBoxAsistencia();
        loadAsistenciaPorGenero();
        loadAsistenciaPorPelicula();
        loadAsistenciaPorClasificacion();
        filterComboBoxAsistencia.setOnAction(event -> {
            String selectedFilter = filterComboBoxAsistencia.getValue();
            loadAsistenciaGeneralData(selectedFilter);
        });
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