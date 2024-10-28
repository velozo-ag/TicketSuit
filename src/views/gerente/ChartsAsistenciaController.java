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
import javafx.scene.control.DateCell;
import javafx.scene.layout.Pane;
import views.MainController;
/**1 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.DatePicker;
import java.sql.PreparedStatement;
import java.time.LocalDate;

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
    private LineChart<String, Number> lineChartAsistencia;
    @FXML
    private ComboBox<String> filterComboBoxAsistencia;
    @FXML
    private DatePicker desdeDatePicker;
    @FXML
    private DatePicker hastaDatePicker;

    public ChartsAsistenciaController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @FXML
    void toLogin(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAsistenciaPorPelicula(LocalDate desde, LocalDate hasta) {
        barChartAsistenciaPorPelicula.getData().clear();

        String query = "SELECT p.nombre AS pelicula, COUNT(t.id_ticket) AS total_asistencia " +
                       "FROM Pelicula p " +
                       "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                       "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                       "WHERE f.fecha_ingreso BETWEEN ? AND ? " +
                       "GROUP BY p.nombre " +
                       "ORDER BY total_asistencia DESC;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Asistencia por Película");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String pelicula = rs.getString("pelicula");
                    int totalAsistencia = rs.getInt("total_asistencia");
                    series.getData().add(new XYChart.Data<>(pelicula, totalAsistencia));
                }
            }

            barChartAsistenciaPorPelicula.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAsistenciaPorGenero(LocalDate desde, LocalDate hasta) {
        barChartAsistenciaPorGenero.getData().clear();

        String query = "SELECT g.descripcion AS genero, COUNT(t.id_ticket) AS total_asistencia " +
                       "FROM Genero g " +
                       "JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero " +
                       "JOIN Pelicula p ON gp.id_pelicula = p.id_pelicula " +
                       "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                       "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                       "WHERE f.fecha_ingreso BETWEEN ? AND ? " +
                       "GROUP BY g.descripcion " +
                       "ORDER BY total_asistencia DESC;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Asistencia por Género");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String genero = rs.getString("genero");
                    int totalAsistencia = rs.getInt("total_asistencia");
                    series.getData().add(new XYChart.Data<>(genero, totalAsistencia));
                }
            }

            barChartAsistenciaPorGenero.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAsistenciaPorClasificacion(LocalDate desde, LocalDate hasta) {
        pieChartAsistenciaPorClasificacion.getData().clear();

        String query = "SELECT cl.nombre AS clasificacion, COUNT(t.id_ticket) AS total_asistencia " +
                       "FROM Clasificacion cl " +
                       "JOIN Pelicula p ON cl.id_clasificacion = p.id_clasificacion " +
                       "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                       "JOIN Ticket t ON f.id_funcion = t.id_funcion " +
                       "WHERE f.fecha_ingreso BETWEEN ? AND ? " +
                       "GROUP BY cl.nombre " +
                       "ORDER BY total_asistencia DESC;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String clasificacion = rs.getString("clasificacion");
                    int totalAsistencia = rs.getInt("total_asistencia");
                    PieChart.Data slice = new PieChart.Data(clasificacion, totalAsistencia);
                    pieChartAsistenciaPorClasificacion.getData().add(slice);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

        loadAsistenciaPorPelicula(desde, hoy);
        loadAsistenciaPorGenero(desde, hoy);
        loadAsistenciaPorClasificacion(desde, hoy);

        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
    }

    private void filtrarPorFecha() {
        LocalDate desde = desdeDatePicker.getValue();
        LocalDate hasta = hastaDatePicker.getValue();

        if (desde != null && hasta != null) {
            loadAsistenciaPorPelicula(desde, hasta);
            loadAsistenciaPorGenero(desde, hasta);
            loadAsistenciaPorClasificacion(desde, hasta);
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