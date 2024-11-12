package views.gerente;
import java.io.IOException;
import views.gerente.ChartsUtilController;
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
import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;


public class ChartsIngresosController {

    private MainController mainController = new MainController();
    private Connection connection;

    @FXML
    private Pane mainPanel;

    @FXML
    private BarChart<String, Number> barChartIngresosPeliculas;

    @FXML
    private BarChart<String, Number> barChartIngresosPorGenero;

    @FXML
    private DatePicker desdeDatePicker;

    @FXML
    private DatePicker hastaDatePicker;

    public ChartsIngresosController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void initialize() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioAño = LocalDate.of(hoy.getYear(), 1, 1);

        desdeDatePicker.setValue(inicioAño);
        hastaDatePicker.setValue(hoy);

        configurarDatePickers(hoy);

        // Cargar gráficos con el rango inicial
        actualizarGraficos(inicioAño, hoy);

        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> actualizarGraficos(newValue, hastaDatePicker.getValue()));
        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> actualizarGraficos(desdeDatePicker.getValue(), newValue));
    }

    private void configurarDatePickers(LocalDate hoy) {
        desdeDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(hoy));
            }
        });

        hastaDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(hoy) || date.isBefore(desdeDatePicker.getValue()));
            }
        });

        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && hastaDatePicker.getValue() != null && newValue.isAfter(hastaDatePicker.getValue())) {
                hastaDatePicker.setValue(newValue);
            }
        });

        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && desdeDatePicker.getValue() != null && newValue.isBefore(desdeDatePicker.getValue())) {
                desdeDatePicker.setValue(newValue);
            }
        });
    }

    private void actualizarGraficos(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            loadTotalIngresosData(desde, hasta);
            loadTotalIngresosPorGenero(desde, hasta);
        }
    }

    private void loadTotalIngresosData(LocalDate desde, LocalDate hasta) {
        barChartIngresosPeliculas.getData().clear();
    
        String query = """
            SELECT 
                p.nombre AS pelicula, 
                SUM(t.valor) AS total_recaudado
            FROM 
                Ticket t
            JOIN 
                Sala_Funcion sf ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala AND t.inicio_funcion = sf.inicio_funcion
            JOIN 
                Funcion f ON sf.id_funcion = f.id_funcion
            JOIN 
                Pelicula p ON f.id_pelicula = p.id_pelicula
            WHERE 
                sf.inicio_funcion BETWEEN ? AND ?
            GROUP BY 
                p.nombre
            ORDER BY 
                total_recaudado DESC;
        """;
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
    
            try (ResultSet rs = pstmt.executeQuery()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Total Recaudado");
    
                while (rs.next()) {
                    String pelicula = rs.getString("pelicula");
                    double totalRecaudado = rs.getDouble("total_recaudado");
                    series.getData().add(new XYChart.Data<>(pelicula, totalRecaudado));
                }
    
                barChartIngresosPeliculas.getData().add(series);
    
                // Añadir Tooltips
                ChartsUtilController.addTooltipToBarChartCash(series);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTotalIngresosPorGenero(LocalDate desde, LocalDate hasta) {
        barChartIngresosPorGenero.getData().clear();
    
        String query = """
            SELECT 
                g.descripcion AS genero, 
                SUM(t.valor) AS total_recaudado
            FROM 
                Ticket t
            JOIN 
                Sala_Funcion sf ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala AND t.inicio_funcion = sf.inicio_funcion
            JOIN 
                Funcion f ON sf.id_funcion = f.id_funcion
            JOIN 
                Pelicula p ON f.id_pelicula = p.id_pelicula
            JOIN 
                Genero_Pelicula gp ON p.id_pelicula = gp.id_pelicula
            JOIN 
                Genero g ON gp.id_genero = g.id_genero
            WHERE 
                sf.inicio_funcion BETWEEN ? AND ?
            GROUP BY 
                g.descripcion
            ORDER BY 
                total_recaudado DESC;
        """;
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
    
            try (ResultSet rs = pstmt.executeQuery()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Ingresos por Género");
    
                while (rs.next()) {
                    String genero = rs.getString("genero");
                    double totalRecaudado = rs.getDouble("total_recaudado");
                    series.getData().add(new XYChart.Data<>(genero, totalRecaudado));
                }
    
                barChartIngresosPorGenero.getData().add(series);
                ChartsUtilController.addTooltipToBarChartCash(series);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
