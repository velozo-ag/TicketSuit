package views.vendedor;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import controllers.AsientoController;
import controllers.CompraController;
import controllers.SalaController;
import controllers.TicketController;
import entities.Asiento;
import entities.Compra;
import entities.Ticket;
import entities.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import views.MainController;

public class ReporteTicketsController {

    @FXML
    private TableView<Compra> tablaVentas;

    @FXML
    private TableColumn<Compra, Integer> cCantidad;

    @FXML
    private TableColumn<Compra, String> cFechaFuncion;

    @FXML
    private TableColumn<Compra, String> cHoraInicio;

    @FXML
    private TableColumn<Compra, Integer> cIdCompra;

    @FXML
    private TableColumn<Compra, String> cNombrePelicula;

    @FXML
    private Label lCartelera11;

    @FXML
    private Label lNombreVendedor;

    @FXML
    private Pane mainPanel;

    @FXML
    private Pane panelDetalle;

    @FXML
    private Label lSeleccioneCompra;

    @FXML
    private Label tAsientos;

    @FXML
    private Label tCantidad;

    @FXML
    private Label tFechaCompra;

    @FXML
    private Label tFechaFuncion;

    @FXML
    private Label tHorario;

    @FXML
    private Label tIdCompra;

    @FXML
    private Label tNombrePelicula;

    @FXML
    private Label tNombrePelicula1;

    @FXML
    private Label tPrecioTotal;

    @FXML
    private Label tPrecioTotal1;

    @FXML
    private Label tSala;

    private MainController mainController = new MainController();
    private CompraController compraController = new CompraController();
    private TicketController ticketController = new TicketController();
    private SalaController salaController = new SalaController();
    private AsientoController asientoController = new AsientoController();
    private TicketPDFController ticketPDFController = new TicketPDFController();
    private Compra compraSeleccionada;

    @FXML
    void initialize() {
        cCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        cFechaFuncion.setCellValueFactory(new PropertyValueFactory<>("fechaFuncion"));
        cHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaFuncion"));
        cIdCompra.setCellValueFactory(new PropertyValueFactory<>("id_compra"));
        cNombrePelicula.setCellValueFactory(new PropertyValueFactory<>("nombrePelicula"));

        tablaVentas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateCompraSeleccionada(newValue);
            }
        });

        lNombreVendedor.setText("Sr. " + UserSession.getInstance().getUsuario().getNombre());

        lSeleccioneCompra.setVisible(true);
        panelDetalle.setVisible(false);
        cargarCompras();
    }

    @FXML
    void imprimirTickets(ActionEvent event) {
        if(mensajeConfirmacion("Desea generar los tickets de la compra: " + compraSeleccionada.getId_compra() + "?")){
            List<Ticket> tickets = ticketController.findByIdCompra(compraSeleccionada.getId_compra());
            
            for(Ticket t : tickets){
                ticketPDFController.generarTicketPDF(t.getId_ticket());
            }
            
            mensajeExito("Tickets generados con exito en Paquete Tickets");
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            mainController.toLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toCartelera(ActionEvent event) {
        try {
            mainController.toPVendedor(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateCompraSeleccionada(Compra compra){
        compraSeleccionada = compra;
        List<Ticket> tickets = ticketController.findByIdCompra(compraSeleccionada.getId_compra());
        List<Asiento> asientos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

        for(Ticket ticket : tickets){
            asientos.add(asientoController.findById(ticket.getId_asiento(), ticket.getId_sala()));
        }

        tFechaFuncion.setText(compraSeleccionada.getFechaFuncion());
        tHorario.setText(compraSeleccionada.getHoraFuncion());
        tSala.setText(salaController.findById(tickets.get(0).getId_sala()).getNombre());
        tAsientos.setText(listaAsientos(asientos));

        tIdCompra.setText(compraSeleccionada.getId_compra() + "");
        tFechaCompra.setText(dateFormat.format(compraSeleccionada.getFecha()));
        tCantidad.setText(compraSeleccionada.getCantidad()+ "");

        tPrecioTotal.setText(compraSeleccionada.getSubtotal() + "");

        lSeleccioneCompra.setVisible(false);
        panelDetalle.setVisible(true);
    };

    void cargarCompras() {
        ObservableList<Compra> compras = FXCollections.observableArrayList(compraController.findByIdUsuario(UserSession.getInstance().getUsuario().getIdUsuario()));
        tablaVentas.setItems(compras);
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
