package views.vendedor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import controllers.CompraController;
import controllers.SalaController;
import controllers.TicketController;
import entities.Asiento;
import entities.Compra;
import entities.Pelicula;
import entities.Sala_Funcion;
import entities.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.SceneManager;

public class ResumenVentaController {

    @FXML
    private Button bTickets;

    @FXML
    private Label lPrecio1;

    @FXML
    private Pane mainButton;

    @FXML
    private Pane mainPanel;

    @FXML
    private Pane panelImagen;

    @FXML
    private Label tAsientos;

    @FXML
    private Label tFecha;

    @FXML
    private Label tHorario;

    @FXML
    private Label tNombrePelicula;

    @FXML
    private Label tPrecioTotal;

    @FXML
    private Label tSala;

    private SalaController salaController = new SalaController();
    private TicketPDFController ticketPDFController = new TicketPDFController();
    private CompraController compraController = new CompraController();
    private TicketController ticketController = new TicketController();

    public Pelicula pelicula;
    public Sala_Funcion funcion;
    public List<Asiento> asientos;
    public List<Ticket> tickets;
    public Compra compra;



    @FXML
    void imprimirTickets(ActionEvent event) {
        for(Ticket t : tickets){
            ticketPDFController.generarTicketPDF(t.getId_ticket());
        }

        mensajeExito("Tickets generados con exito en Paquete Tickets");
        toCartelera(event);
    }

    @FXML
    void toCartelera(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vendedor/Cartelera.fxml"));
            root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.setScene(root, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValores(Compra compra, Pelicula pelicula, Sala_Funcion funcion, List<Asiento> asientos) {
        this.pelicula = pelicula;
        this.funcion = funcion;
        this.asientos = asientos;
        this.compra = compra;
        tickets = ticketController.findByIdCompra(compraController.obtenerUltimoIdCompra());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        tNombrePelicula.setText(funcion.getPelicula().getNombre());
        tFecha.setText(dateFormatter.format(funcion.getInicioFuncion()));
        tHorario.setText(timeFormatter.format(funcion.getInicioFuncion()));
        tSala.setText(salaController.findById(funcion.getId_sala()).getNombre());
        tAsientos.setText(listaAsientos(asientos));

        tPrecioTotal.setText(compra.getSubtotal() + "");

        ImageView thumbnail = new ImageView();
        thumbnail.setFitWidth(panelImagen.getPrefWidth() - 2);
        thumbnail.setFitHeight(panelImagen.getPrefHeight() - 2);
        thumbnail.setPreserveRatio(true);
        thumbnail.setImage(new Image(pelicula.getImagen()));
        thumbnail.setLayoutX(1);
        thumbnail.setLayoutY(1);

        panelImagen.getChildren().add(thumbnail);
    }

    private String listaAsientos(List<Asiento> asientos) {
        String lista = "";

        for (int i = 0; i < asientos.size(); i++) {
            lista += asientos.get(i).getLetraFila() + asientos.get(i).getNumeroColumna();

            if (i != asientos.size() - 1) {
                lista += ",";
            }
        }

        return lista;
    }

     private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
