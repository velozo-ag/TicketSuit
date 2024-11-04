package views.gerente;
import java.io.IOException;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import views.MainController;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;

import java.sql.PreparedStatement;
/**1 */
public class ChartsLogisticaController {

    private MainController mainController = new MainController();

    private Connection connection;
    @FXML
    private Pane mainPanel;
    @FXML
    public StackedBarChart<String, Number> capacidadSalasChart;
    @FXML
    private BarChart<String, Number> barChartPeliculasPorGenero;
    @FXML
    private BarChart<String, Number> barChartFuncionesPorDia;
    @FXML
    private BarChart<String,Number> peliculasPorMesChart;
    @FXML
    private PieChart pieChartPeliculasPorGenero;

    @FXML
    private DatePicker desdeDatePicker, hastaDatePicker;


    public ChartsLogisticaController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
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

        filtrarPorFecha();

        desdeDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
        hastaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrarPorFecha());
    }

    private void filtrarPorFecha() {
        LocalDate desde = desdeDatePicker.getValue();
        LocalDate hasta = hastaDatePicker.getValue();

        if (desde != null && hasta != null) {
            cargarPeliculasPorMes(desde, hasta);
            loadPeliculasPorGeneroData(desde, hasta);
            cargarPeliculasPorGenero(desde, hasta);
        }
    }

    private void loadPeliculasPorGeneroData(LocalDate desde, LocalDate hasta) {
        barChartPeliculasPorGenero.getData().clear(); 
    
        String query = "SELECT g.descripcion AS genero, COUNT(DISTINCT gp.id_pelicula) AS cantidad_peliculas " +
                       "FROM Genero g " +
                       "JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero " +
                       "JOIN Pelicula p ON gp.id_pelicula = p.id_pelicula " +
                       "JOIN Funcion f ON p.id_pelicula = f.id_pelicula " +
                       "WHERE f.fecha_ingreso BETWEEN ? AND ? OR f.fecha_final BETWEEN ? AND ? " +
                       "GROUP BY g.descripcion " +
                       "ORDER BY cantidad_peliculas DESC;";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
            pstmt.setDate(3, java.sql.Date.valueOf(desde));
            pstmt.setDate(4, java.sql.Date.valueOf(hasta));
    
            ResultSet rs = pstmt.executeQuery();
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

    public void cargarPeliculasPorGenero(LocalDate desde, LocalDate hasta) {
    String query = "SELECT g.descripcion AS genero, COUNT(gp.id_pelicula) AS cantidad_peliculas " +
                   "FROM Genero g " +
                   "JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero " +
                   "JOIN Pelicula p ON gp.id_pelicula = p.id_pelicula " +
                   "JOIN Funcion f ON f.id_pelicula = p.id_pelicula " +
                   "WHERE f.fecha_ingreso BETWEEN ? AND ? " +
                   "GROUP BY g.descripcion " +
                   "ORDER BY cantidad_peliculas DESC;";

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String genero = rs.getString("genero");
                    int cantidadPeliculas = rs.getInt("cantidad_peliculas");
                    pieChartData.add(new PieChart.Data(genero, cantidadPeliculas));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de películas por género: " + e.getMessage());
        }

        pieChartPeliculasPorGenero.setData(pieChartData); // Asegúrate de que pieChartPeliculasPorGenero sea el ID de tu PieChart
    }

    // public void cargarCapacidadSalas(LocalDate desde, LocalDate hasta) {
    //     capacidadSalasChart.getData().clear();
    //     XYChart.Series<String, Number> capacidadTotalSeries = new XYChart.Series<>();
    //     XYChart.Series<String, Number> capacidadUtilizadaSeries = new XYChart.Series<>();
        
    //     String query = "SELECT S.nombre AS sala, S.capacidad AS capacidad_total, COUNT(T.id_ticket) AS capacidad_utilizada " +
    //                    "FROM Sala S LEFT JOIN Sala_Funcion SF ON S.id_sala = SF.id_sala " +
    //                    "LEFT JOIN Ticket T ON SF.id_funcion = T.id_funcion " +
    //                    "WHERE SF.inicio_funcion BETWEEN ? AND ? " +
    //                    "GROUP BY S.nombre, S.capacidad " +
    //                    "ORDER BY S.nombre;";

    //     capacidadTotalSeries.setName("Capacidad Total");
    //     capacidadUtilizadaSeries.setName("Capacidad Utilizada");

    //     try (PreparedStatement pstmt = connection.prepareStatement(query)) {
    //         pstmt.setDate(1, java.sql.Date.valueOf(desde));
    //         pstmt.setDate(2, java.sql.Date.valueOf(hasta));
    //         ResultSet rs = pstmt.executeQuery();

    //         while (rs.next()) {
    //             String sala = rs.getString("sala");
    //             int capacidadTotal = rs.getInt("capacidad_total");
    //             int capacidadUtilizada = rs.getInt("capacidad_utilizada");

    //             capacidadUtilizadaSeries.getData().add(new XYChart.Data<>(sala, capacidadUtilizada));
    //             capacidadTotalSeries.getData().add(new XYChart.Data<>(sala, capacidadTotal - capacidadUtilizada));
    //         }

    //         capacidadSalasChart.getData().addAll(capacidadTotalSeries, capacidadUtilizadaSeries);

    //     } catch (SQLException e) {
    //         System.out.println("Error al cargar datos de capacidad de salas: " + e.getMessage());
    //     }
    // }

    public void cargarPeliculasPorMes(LocalDate desde, LocalDate hasta) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Películas Estrenadas por Mes");
    
        String query = "SELECT YEAR(funcion.fecha_ingreso) AS anio, " +
                       "MONTH(funcion.fecha_ingreso) AS mes, " +
                       "COUNT(DISTINCT funcion.id_pelicula) AS total_peliculas " +
                       "FROM Funcion funcion " +
                       "WHERE funcion.fecha_ingreso BETWEEN ? AND ? " +
                       "GROUP BY YEAR(funcion.fecha_ingreso), MONTH(funcion.fecha_ingreso) " +
                       "ORDER BY anio, mes;";
    
        int totalPeliculas = 0;
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(desde));
            pstmt.setDate(2, java.sql.Date.valueOf(hasta));
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String label = obtenerNombreMes(rs.getInt("mes")) + String.valueOf(rs.getInt("anio")).substring(2);
                    int peliculas = rs.getInt("total_peliculas");
                    series.getData().add(new XYChart.Data<>(label, peliculas));
                    totalPeliculas += peliculas;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos de películas por mes: " + e.getMessage());
        }
    
        peliculasPorMesChart.getData().clear(); 
        peliculasPorMesChart.getData().add(series);
        // peliculasLabel.setText("Películas Estrenadas: " + totalPeliculas);
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
