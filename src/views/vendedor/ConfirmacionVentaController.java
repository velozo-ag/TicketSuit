package views.vendedor;

import java.sql.Date;
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
import entities.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConfirmacionVentaController {

    @FXML
    private Button bCerrar;

    @FXML
    private Pane formPanel;

    @FXML
    private Label lPrecio1;

    @FXML
    private Pane mainButton;

    @FXML
    private AnchorPane pFormulario;

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
    private CompraController compraController = new CompraController();
    private TicketController ticketController = new TicketController();

    public Pelicula pelicula;
    public Sala_Funcion funcion;
    public List<Asiento> asientos;

    private boolean resultadoCompra;
    private Compra compra;

    @FXML
    void CerrarFormulario(ActionEvent event) {
        if (mensajeConfirmacion("Esta seguro que desea cancelar la compra?")) {
            Stage stage = (Stage) bCerrar.getScene().getWindow();
            resultadoCompra = false;
            stage.close();
        }
    }

    @FXML
    void realizarVenta(ActionEvent event) {
        if (mensajeConfirmacion("Desea finalizar la venta?")) {
            compra.setId_usuario(UserSession.getInstance().getUsuario().getIdUsuario());
            int id_compra = compraController.createCompra(compra);

            for (Asiento asiento : asientos) {
                Ticket ticket = new Ticket();

                ticket.setId_funcion(funcion.getId_funcion());
                ticket.setId_sala(funcion.getId_sala());
                ticket.setInicioFuncion(funcion.getInicioFuncion());
                ticket.setId_asiento(asiento.getIdAsiento());
                ticket.setId_compra(id_compra);
                ticket.setValor(compra.getSubtotal() / compra.getCantidad());

                ticketController.createTicket(ticket);
            }
            resultadoCompra = true;
            
            Stage stage = (Stage) bCerrar.getScene().getWindow();
            stage.close();
        }
    }

    public void setValores(Compra compra, Pelicula pelicula, Sala_Funcion funcion, List<Asiento> asientos) {
        this.pelicula = pelicula;
        this.funcion = funcion;
        this.asientos = asientos;
        this.compra = compra;

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

    private boolean mensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType buttonAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAceptar, buttonCancelar);

        alert.showAndWait();

        if (alert.getResult() == buttonAceptar) {
            return true;
        }

        return false;
    }

    public boolean getCompra() {
        return resultadoCompra;
    }

}
