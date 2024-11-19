package views.gerente;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class ChartsUtilController {
    
    public static void addTooltipToBarChartCash(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip("Valor: $" + data.getYValue());
            tooltip.setShowDelay(Duration.millis(200));
            tooltip.setHideDelay(Duration.millis(50));
            Tooltip.install(data.getNode(), tooltip);

            String estiloPorDefecto = "-fx-bar-fill: #4682B4;"; 
            String estiloMouseEncima = "-fx-bar-fill: #EE6E84;"; 
            data.getNode().setStyle(estiloPorDefecto);

            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle(estiloMouseEncima));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle(estiloPorDefecto));
        }
    }

    public static void addTooltipToBarChartAsist(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip("Asistencia: " + data.getYValue());
            tooltip.setShowDelay(Duration.millis(200));
            tooltip.setHideDelay(Duration.millis(50));
            Tooltip.install(data.getNode(), tooltip);

            String estiloPorDefecto = "-fx-bar-fill: #4682B4;"; 
            String estiloMouseEncima = "-fx-bar-fill: #EE6E84;"; 
            data.getNode().setStyle(estiloPorDefecto);

            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle(estiloMouseEncima));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle(estiloPorDefecto));
        }
    }

    public static void addTooltipToBarChartLog(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(data.getYValue() + " Peliculas");
            tooltip.setShowDelay(Duration.millis(200));
            tooltip.setHideDelay(Duration.millis(50));
            Tooltip.install(data.getNode(), tooltip);

            String estiloPorDefecto = "-fx-bar-fill: #4682B4;"; 
            String estiloMouseEncima = "-fx-bar-fill: #EE6E84;"; 

            data.getNode().setStyle(estiloPorDefecto);

            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle(estiloMouseEncima));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle(estiloPorDefecto));
        }
    }

    public static void addTooltipToBarChartDash(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(data.getYValue() + " Tickets");
            tooltip.setShowDelay(Duration.millis(200));
            tooltip.setHideDelay(Duration.millis(50));
            Tooltip.install(data.getNode(), tooltip);

            String estiloPorDefecto = "-fx-bar-fill: #4682B4;"; 
            String estiloMouseEncima = "-fx-bar-fill: #EE6E84;"; 

            data.getNode().setStyle(estiloPorDefecto);

            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle(estiloMouseEncima));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle(estiloPorDefecto));
        }
    }

    public void addTooltipsToPieChart(PieChart pieChart) {
        for (PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);

            data.getNode().setOnMouseEntered(e -> {
                data.getNode().setStyle("-fx-opacity: 0.7;"); 
            });
            data.getNode().setOnMouseExited(e -> {
                data.getNode().setStyle("-fx-opacity: 1.0;"); 
            });
        }
    }

    public static void configurarTooltipsLineChart(LineChart<String, Number> lineChart) {
        lineChart.getData().forEach(series -> {
            series.getData().forEach(data -> {
                Tooltip tooltip = new Tooltip();
                String label = data.getXValue();  
                double value = data.getYValue().doubleValue();  
                tooltip.setText(String.format("%s: $%.2f", label, value));

                Node node = data.getNode();
                Tooltip.install(node, tooltip);

                final boolean[] tooltipVisible = {false};
                node.setOnMouseClicked(event -> {
                    if (tooltipVisible[0]) {
                        tooltip.hide();  
                        tooltipVisible[0] = false;  
                    } else {
                        tooltip.show(node, event.getScreenX(), event.getScreenY());  
                        tooltipVisible[0] = true;  
                    }
                });

                node.setOnMouseEntered(event -> {
                    node.setStyle("-fx-bar-fill: #EE6E84;"); 
                });

                node.setOnMouseExited(event -> {
                    node.setStyle("-fx-bar-fill: #4682B4;");  
                });
            });
        });
    }

}
