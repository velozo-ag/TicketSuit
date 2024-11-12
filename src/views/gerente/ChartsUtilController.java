package views.gerente;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class ChartsUtilController {

    /**
     * Añade Tooltips y eventos al pasar el mouse para resaltar barras en un BarChart.
     *
     * @param series La serie de datos del gráfico
     */
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
            // Crear el tooltip con el nombre y el valor
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);

            // Opcional: Cambiar estilo al pasar el mouse
            data.getNode().setOnMouseEntered(e -> {
                data.getNode().setStyle("-fx-opacity: 0.7;"); // Reducir opacidad
            });
            data.getNode().setOnMouseExited(e -> {
                data.getNode().setStyle("-fx-opacity: 1.0;"); // Restaurar opacidad
            });
        }
    }

    public static void configurarTooltipsLineChart(LineChart<String, Number> lineChart) {
        // Iterar sobre las series de datos del LineChart
        lineChart.getData().forEach(series -> {
            // Iterar sobre los datos de la serie
            series.getData().forEach(data -> {
                // Crear el tooltip con la información de cada punto de la serie
                Tooltip tooltip = new Tooltip();
                String label = data.getXValue();  // Obtener la etiqueta del eje X (por ejemplo, mes/año)
                double value = data.getYValue().doubleValue();  // Obtener el valor en el eje Y (ingresos, por ejemplo)
                tooltip.setText(String.format("%s: $%.2f", label, value));

                Node node = data.getNode();
                Tooltip.install(node, tooltip);

                // Usar una variable para llevar el estado de visibilidad del tooltip
                final boolean[] tooltipVisible = {false};  // Estado inicial del tooltip (no visible)

                // Evento de clic para alternar la visibilidad del tooltip
                node.setOnMouseClicked(event -> {
                    if (tooltipVisible[0]) {
                        tooltip.hide();  // Si está visible, lo ocultamos
                        tooltipVisible[0] = false;  // Cambiar el estado a no visible
                    } else {
                        tooltip.show(node, event.getScreenX(), event.getScreenY());  // Mostrar el tooltip
                        tooltipVisible[0] = true;  // Cambiar el estado a visible
                    }
                });

                // Cambiar color del nodo al pasar el mouse por encima
                node.setOnMouseEntered(event -> {
                    node.setStyle("-fx-bar-fill: #EE6E84;");  // Cambiar color al pasar el mouse
                });

                // Restaurar color original cuando el mouse sale del nodo
                node.setOnMouseExited(event -> {
                    node.setStyle("-fx-bar-fill: #4682B4;");  // Restaurar color cuando el mouse sale
                });
            });
        });
    }

}
