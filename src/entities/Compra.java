package entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import controllers.FuncionController;
import controllers.TicketController;

public class Compra {
    private int id_compra;
    private float subtotal;
    private Timestamp fecha;
    private int cantidad;
    private int id_usuario;

    String fechaFuncion;
    String horaFuncion;
    String nombrePelicula;
    
    public Compra(){
        
    }

    public Compra(int id_compra) {
        this.id_compra = id_compra;
        cargarDatos();
    }

    private void cargarDatos() {
        TicketController ticketController = new TicketController();
        FuncionController funcionController = new FuncionController();

        List<Ticket> tickets = ticketController.findByIdCompra(id_compra);
        if (!tickets.isEmpty()) {
            Ticket ticket = tickets.get(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            this.fechaFuncion = dateFormat.format(ticket.getInicioFuncion());
            this.horaFuncion = timeFormat.format(ticket.getInicioFuncion());

            Funcion funcion = funcionController.findById(ticket.getId_funcion());
            if (funcion != null) {
                this.nombrePelicula = funcion.getNombrePelicula();
            }
        }
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFechaFuncion() {
        return fechaFuncion;
    }
    
    public String getHoraFuncion() {
        return horaFuncion;
    }
    
    public String getNombrePelicula() {
        return nombrePelicula;
    }

}
