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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import views.MainController;
import java.time.temporal.ChronoUnit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
/**1 */
public class GerenteController {

    private MainController mainController = new MainController();

    @FXML
    private LineChart<String, Number> ingresosChart;
    @FXML
    private PieChart generoChart;
    @FXML
    public PieChart ingresosPorFuncionChart; 
    @FXML
    public StackedBarChart<String, Number> capacidadSalasChart;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public BarChart<String, Number> ticketsVendidosChart;
    @FXML
    private DatePicker desdeDatePicker;
    @FXML
    private DatePicker hastaDatePicker;
    
    private DatabaseConnection databaseConnection = new DatabaseConnection();
    private Connection connection;

    public GerenteController() {
        this.connection = databaseConnection.getConnection();
    }

    public void cargarTicketsVendidosPorMes(LocalDate desde, LocalDate hasta) {
        XYChart.Series<String, Number> seriesTicketsVendidos = new XYChart.Series<>();
        String query = "SELECT DATEPART(YEAR, c.fecha) AS anio, DATEPART(MONTH, c.fecha) AS mes, " +
                       "COUNT(t.id_ticket) AS total_tickets_vendidos " +
                       "FROM Ticket t " +
                       "JOIN Compra c ON t.id_compra = c.id_compra " +
                       "WHERE c.fecha BETWEEN '" + desde + "' AND '" + hasta + "' " +
                       "GROUP BY DATEPART(YEAR, c.fecha), DATEPART(MONTH, c.fecha) " +
                       "ORDER BY anio, mes;";
        
        seriesTicketsVendidos.setName("Tickets/Mes");
    
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Obtener el mes y el año para mostrar como "Dic22", "Ene23", etc.
                String label = obtenerNombreMes(rs.getInt("mes")) + String.valueOf(rs.getInt("anio")).substring(2);
                int totalTicketsVendidos = rs.getInt("total_tickets_vendidos");
                seriesTicketsVendidos.getData().add(new XYChart.Data<>(label, totalTicketsVendidos));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de tickets vendidos por mes: " + e.getMessage());
        }
    
        ticketsVendidosChart.getData().clear();
        ticketsVendidosChart.getData().add(seriesTicketsVendidos);
    }
    
    
    public void cargarCapacidadSalas(LocalDate desde, LocalDate hasta) {
        XYChart.Series<String, Number> capacidadTotalSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> capacidadUtilizadaSeries = new XYChart.Series<>();
        
        String query = "SELECT S.nombre AS sala, S.capacidad AS capacidad_total, COUNT(T.id_ticket) AS capacidad_utilizada " +
                       "FROM Sala S " +
                       "LEFT JOIN Sala_Funcion SF ON S.id_sala = SF.id_sala " +
                       "LEFT JOIN Ticket T ON SF.id_funcion = T.id_funcion " +
                       "LEFT JOIN Compra C ON T.id_compra = C.id_compra " +
                       "WHERE C.fecha BETWEEN '" + desde + "' AND '" + hasta + "' " +
                       "GROUP BY S.nombre, S.capacidad " +
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
    
        capacidadSalasChart.getData().clear();
        capacidadSalasChart.getData().addAll(capacidadTotalSeries, capacidadUtilizadaSeries);
    }
    
    public void cargarIngresosPorFuncion(LocalDate desde, LocalDate hasta) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = "SELECT T.descripcion AS tipo_funcion, SUM(C.subtotal) AS total_ingresos " +
                       "FROM Compra C " +
                       "JOIN Ticket TICK ON C.id_compra = TICK.id_compra " +
                       "JOIN Sala_Funcion SF ON TICK.id_funcion = SF.id_funcion " +
                       "JOIN Funcion F ON SF.id_funcion = F.id_funcion " +
                       "JOIN TipoFuncion T ON F.id_tipoFuncion = T.id_tipoFuncion " +
                       "WHERE C.fecha BETWEEN '" + desde + "' AND '" + hasta + "' " +
                       "AND T.descripcion IN ('2D', '3D') " +
                       "GROUP BY T.descripcion;";
    
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String tipoFuncion = rs.getString("tipo_funcion");
                double ingresos = rs.getDouble("total_ingresos");
                pieChartData.add(new PieChart.Data(tipoFuncion, ingresos));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de ingresos por función: " + e.getMessage());
        }
    
        ingresosPorFuncionChart.setData(pieChartData);
    }
    
    public void cargarIngresosPorMes(LocalDate desde, LocalDate hasta) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String query = "SELECT YEAR(fecha) AS anio, MONTH(fecha) AS mes, SUM(subtotal) AS total_ingresos " +
                       "FROM Compra " +
                       "WHERE fecha BETWEEN '" + desde + "' AND '" + hasta + "' " +
                       "GROUP BY YEAR(fecha), MONTH(fecha) ORDER BY YEAR(fecha), MONTH(fecha);";
    
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String label = obtenerNombreMes(rs.getInt("mes")) + String.valueOf(rs.getInt("anio")).substring(2);
                double ingresos = rs.getDouble("total_ingresos");
                series.getData().add(new XYChart.Data<>(label, ingresos));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de ingresos por mes: " + e.getMessage());
        }
    
        ingresosChart.getData().clear();
        ingresosChart.getData().add(series);
    }
    
    private String obtenerNombreMes(int mes) {
        switch (mes) {
            case 1: return "Ene";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Abr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Ago";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dic";
            default: return "Mes desconocido";
        }
    }
    
    public void initialize() {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = LocalDate.of(2024, 1, 1);
    
        desdeDatePicker.setValue(desde);
        hastaDatePicker.setValue(hoy);
    
        hastaDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(hoy));
            }
        });
    
        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && hastaDatePicker.getValue() != null && newValue.isAfter(hastaDatePicker.getValue())) {
                hastaDatePicker.setValue(newValue);
            }
    
            hastaDatePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(hoy) || date.isBefore(newValue));
                }
            });
        });
    
        desdeDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(hoy));
            }
        });
    
        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && desdeDatePicker.getValue() != null && newValue.isBefore(desdeDatePicker.getValue())) {
                desdeDatePicker.setValue(newValue);
            }
        });
    
        cargarTicketsVendidosPorMes(desde, hoy);
        cargarIngresosPorMes(desde, hoy);
        cargarIngresosPorFuncion(desde, hoy);
    
        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
    }
    
    private void filtrarPorFecha() {
        LocalDate desde = desdeDatePicker.getValue();
        LocalDate hasta = hastaDatePicker.getValue();
    
        if (desde != null && hasta != null) {
            cargarTicketsVendidosPorMes(desde, hasta);
            cargarIngresosPorMes(desde, hasta);
            cargarIngresosPorFuncion(desde, hasta);
            // cargarCapacidadSalas(desde, hasta);
        }
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
