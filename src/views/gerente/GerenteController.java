package views.gerente;
import java.io.FileNotFoundException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import views.MainController;
import java.time.temporal.ChronoUnit;

import com.itextpdf.text.DocumentException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
    @FXML
    private Label ticketsLabel;
    @FXML
    private Label ingresosLabel;
    @FXML
    private Label tipoFuncionLabel;
    
    private Connection connection;

    private exportarDatosPDF reportePDF;

    public GerenteController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.reportePDF = new exportarDatosPDF();
    }

    public void cargarTicketsVendidosPorMes(LocalDate desde, LocalDate hasta) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String query = "SELECT YEAR(sala_funcion.inicio_funcion) AS anio, " +
                       "MONTH(sala_funcion.inicio_funcion) AS mes, " +
                       "COUNT(ticket.id_ticket) AS total_tickets " +
                       "FROM Ticket ticket " +
                       "JOIN Sala_Funcion sala_funcion ON ticket.id_funcion = sala_funcion.id_funcion " +
                       "WHERE sala_funcion.inicio_funcion BETWEEN ? AND ? " +
                       "GROUP BY YEAR(sala_funcion.inicio_funcion), MONTH(sala_funcion.inicio_funcion) " +
                       "ORDER BY anio, mes;";
    
        int totalTickets = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String label = obtenerNombreMes(rs.getInt("mes")) + String.valueOf(rs.getInt("anio")).substring(2);
                    int tickets = rs.getInt("total_tickets");
                    series.getData().add(new XYChart.Data<>(label, tickets));
                    totalTickets += tickets;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de tickets vendidos por mes: " + e.getMessage());
        }
    
        ticketsVendidosChart.getData().clear();
        ticketsVendidosChart.getData().add(series);
        ChartsUtilController.addTooltipToBarChartDash(series);
        ticketsLabel.setText("Tickets Vendidos: " + totalTickets);
    }

    @FXML
    void toExportarDatosPDF(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/gerente/ExportarDatosPDFSeleccion.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva escena
            exportarDatosPDF controller = loader.getController();

            // Pasar los valores de los DatePicker al nuevo controlador
            LocalDate desde = desdeDatePicker.getValue();
            LocalDate hasta = hastaDatePicker.getValue();
            controller.setFechas(desde, hasta);

            // Crear y mostrar la nueva escena
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarCantidadTicketsPorFuncion(LocalDate desde, LocalDate hasta) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = """
            SELECT TF.descripcion AS tipo_funcion, COUNT(T.id_ticket) AS total_tickets
            FROM Ticket T
            JOIN Sala_Funcion SF ON T.id_funcion = SF.id_funcion
            JOIN Funcion F ON SF.id_funcion = F.id_funcion
            JOIN TipoFuncion TF ON F.id_tipoFuncion = TF.id_tipoFuncion
            WHERE SF.inicio_funcion BETWEEN ? AND ?
            GROUP BY TF.descripcion;
        """;
    
        final int[] totalTickets = {0}; // Array mutable para manejar totalTickets
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String tipoFuncion = rs.getString("tipo_funcion");
                    int cantidadTickets = rs.getInt("total_tickets");
                    pieChartData.add(new PieChart.Data(tipoFuncion, cantidadTickets));
                    totalTickets[0] += cantidadTickets; // Sumar al total
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de cantidad de tickets por función: " + e.getMessage());
            e.printStackTrace();
        }
    
        ingresosPorFuncionChart.setData(pieChartData);
    
        // Crear un tooltip mutable para controlar el actual
        final Tooltip[] currentTooltip = {null};
    
        // Configurar comportamiento del tooltip
        ingresosPorFuncionChart.getData().forEach(slice -> {
            slice.getNode().setOnMouseClicked(event -> {
                // Ocultar el tooltip actual si existe
                if (currentTooltip[0] != null) {
                    currentTooltip[0].hide();
                    currentTooltip[0] = null;
                }
    
                // Crear y mostrar un nuevo tooltip
                Tooltip tooltip = new Tooltip();
                String tipoFuncion = slice.getName();
                int cantidadTickets = (int) slice.getPieValue();
                double porcentaje = (totalTickets[0] > 0) ? (cantidadTickets * 100.0 / totalTickets[0]) : 0.0;
                tooltip.setText(String.format("%s: %.2f%% (%d tickets)", tipoFuncion, porcentaje, cantidadTickets));
                tooltip.show(slice.getNode(), event.getScreenX(), event.getScreenY());
                currentTooltip[0] = tooltip;
    
                // Configurar evento para ocultar el tooltip al hacer clic fuera
                ingresosPorFuncionChart.getScene().setOnMousePressed(e -> {
                    if (currentTooltip[0] != null) {
                        currentTooltip[0].hide();
                        currentTooltip[0] = null;
                        ingresosPorFuncionChart.getScene().setOnMousePressed(null); // Limpiar el evento
                    }
                });
    
                event.consume(); // Evitar que el evento de clic se propague
            });
        });
    }

    public void cargarIngresosPorMes(LocalDate desde, LocalDate hasta) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String query = "SELECT YEAR(fecha) AS anio, MONTH(fecha) AS mes, SUM(subtotal) AS total_ingresos " +
                       "FROM Compra " +
                       "WHERE fecha BETWEEN ? AND ? " +
                       "GROUP BY YEAR(fecha), MONTH(fecha) ORDER BY YEAR(fecha), MONTH(fecha);";
    
        int totalIngresos = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String label = obtenerNombreMes(rs.getInt("mes")) + String.valueOf(rs.getInt("anio")).substring(2);
                    double ingresos = rs.getDouble("total_ingresos");
                    series.getData().add(new XYChart.Data<>(label, ingresos));
                    totalIngresos += ingresos;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de ingresos por mes: " + e.getMessage());
        }
    
        ingresosChart.getData().clear();
        ingresosChart.getData().add(series);
        ingresosLabel.setText("Ingresos Totales: $" + totalIngresos);
    
        // Llamar al método modularizado para agregar tooltips
        ChartsUtilController.configurarTooltipsLineChart(ingresosChart); // Llamada estática
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
        cargarCantidadTicketsPorFuncion(desde, hoy);

        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
    }

    private void filtrarPorFecha() {
        LocalDate desde = desdeDatePicker.getValue();
        LocalDate hasta = hastaDatePicker.getValue();

        if (desde != null && hasta != null) {
            cargarTicketsVendidosPorMes(desde, hasta);
            cargarIngresosPorMes(desde, hasta);
            cargarCantidadTicketsPorFuncion(desde, hasta);
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
