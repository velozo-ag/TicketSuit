package views.gerente;
import java.io.IOException;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import views.MainController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**1 */
public class ChartsIngresosController {

    private MainController mainController = new MainController();
    private Connection connection;
    @FXML
    private Pane mainPanel;

    @FXML
    private BarChart<String, Number> barChartIngresosPeliculas;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private LineChart<String, Number> lineChartIngresosTiempo;

    @FXML
    private ComboBox<String> filterComboBoxLine; 
    
    public ChartsIngresosController() {
        this.connection = DatabaseConnection.getInstance().getConnection(); 
    }

    public void initialize() {
        populateComboBox();
        populateComboBoxLine(); 
        loadTotalIngresosData("Total");
    
        
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            barChartIngresosPeliculas.getData().clear();
            loadTotalIngresosData(newValue);
        });

        loadIngresosTiempoData("Año");

        filterComboBoxLine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lineChartIngresosTiempo.getData().clear();
            loadIngresosTiempoData(newValue);
        });
    }

    @FXML
    void onFilterChange(ActionEvent event) {
        String selectedFilter = filterComboBox.getValue();
        loadTotalIngresosData(selectedFilter);
    }

    private void populateComboBox() {
        ObservableList<String> filters = FXCollections.observableArrayList("Total", "Anualmente", "Mensualemente", "Semanalmente");
        filterComboBox.setItems(filters);
        filterComboBox.setValue("Total");
    }

    private void loadTotalIngresosData(String filter) {
        barChartIngresosPeliculas.getData().clear();

        String query = buildQueryByFilter(filter);

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Ingresos Totales");

            while (rs.next()) {
                String nombrePelicula = rs.getString("nombre");
                double totalIngresos = rs.getDouble("total_ingresos");

                series.getData().add(new XYChart.Data<>(nombrePelicula, totalIngresos));
            }

            barChartIngresosPeliculas.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String buildQueryByFilter(String filter) {
        String query;
        switch (filter) {
            case "Año":
                query = "SELECT p.nombre, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Pelicula p " +
                        "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                        "JOIN Sala_Funcion sf ON f.id_funcion = sf.id_funcion " +
                        "JOIN Ticket t ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala " +
                        "JOIN Compra c ON c.id_compra = t.id_compra " +
                        "WHERE YEAR(c.fecha) = YEAR(GETDATE()) " +
                        "GROUP BY p.nombre " +
                        "ORDER BY total_ingresos DESC;";
                break;
            case "Mes":
                query = "SELECT p.nombre, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Pelicula p " +
                        "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                        "JOIN Sala_Funcion sf ON f.id_funcion = sf.id_funcion " +
                        "JOIN Ticket t ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala " +
                        "JOIN Compra c ON c.id_compra = t.id_compra " +
                        "WHERE YEAR(c.fecha) = YEAR(GETDATE()) AND MONTH(c.fecha) = MONTH(GETDATE()) " +
                        "GROUP BY p.nombre " +
                        "ORDER BY total_ingresos DESC;";
                break;
            case "Semana":
                query = "SELECT p.nombre, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Pelicula p " +
                        "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                        "JOIN Sala_Funcion sf ON f.id_funcion = sf.id_funcion " +
                        "JOIN Ticket t ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala " +
                        "JOIN Compra c ON c.id_compra = t.id_compra " +
                        "WHERE YEAR(c.fecha) = YEAR(GETDATE()) AND DATEPART(WEEK, c.fecha) = DATEPART(WEEK, GETDATE()) " +
                        "GROUP BY p.nombre " +
                        "ORDER BY total_ingresos DESC;";
                break;
            default: // Total
                query = "SELECT p.nombre, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Pelicula p " +
                        "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                        "JOIN Sala_Funcion sf ON f.id_funcion = sf.id_funcion " +
                        "JOIN Ticket t ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala " +
                        "JOIN Compra c ON c.id_compra = t.id_compra " +
                        "GROUP BY p.nombre " +
                        "ORDER BY total_ingresos DESC;";
                break;
        }
        return query;
    }

    private void populateComboBoxLine() {
        ObservableList<String> filtersLine = FXCollections.observableArrayList("Año", "Mes", "Trimestre");
        filterComboBoxLine.setItems(filtersLine);
        filterComboBoxLine.setValue("Año");
    }
    
    private void loadIngresosTiempoData(String filter) {
        lineChartIngresosTiempo.getData().clear();
        String query = buildQueryForTiempo(filter);
    
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Ingresos Totales");
    
            while (rs.next()) {
                String label;
                double totalIngresos = rs.getDouble("total_ingresos");
                switch (filter) {
                    case "Año":
                        label = String.valueOf(rs.getInt("anio"));
                        break;
                    case "Mes":
                        label = String.valueOf(rs.getInt("mes"));
                        break;
                    case "Trimestre":
                        label = "Semana " + rs.getInt("semana");
                        break;
                    default:
                        throw new IllegalArgumentException("Filtro no reconocido: " + filter);
                }
    
                series.getData().add(new XYChart.Data<>(label, totalIngresos));
            }
    
            lineChartIngresosTiempo.getData().add(series);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String buildQueryForTiempo(String filter) {
        String query;
        switch (filter) {
            case "Año":
                query = "SELECT YEAR(c.fecha) AS anio, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Compra c " +
                        "GROUP BY YEAR(c.fecha) " +
                        "ORDER BY anio;";
                break;
            case "Mes":
                query = "SELECT MONTH(c.fecha) AS mes, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Compra c " +
                        "WHERE YEAR(c.fecha) = YEAR(GETDATE()) " +
                        "GROUP BY MONTH(c.fecha) " +
                        "ORDER BY mes;";
                break;
            case "Trimestre":
                query = "SELECT DATEPART(WEEK, c.fecha) AS semana, SUM(c.subtotal) AS total_ingresos " +
                        "FROM Compra c " +
                        "WHERE YEAR(c.fecha) = YEAR(GETDATE()) " +
                        "AND DATEPART(QUARTER, c.fecha) = DATEPART(QUARTER, GETDATE()) " +
                        "GROUP BY DATEPART(WEEK, c.fecha) " +
                        "ORDER BY semana;";
                break;
            default:
                throw new IllegalArgumentException("Filtro no reconocido: " + filter);
        }
        return query;
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