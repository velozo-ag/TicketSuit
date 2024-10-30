package entities;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import controllers.FuncionController;
import controllers.PeliculaController;
import controllers.TipoFuncionController;

public class Sala_Funcion {
    private int id_funcion;
    private int id_sala;
    private Timestamp inicioFuncion;
    private Timestamp finalFuncion;

    private String tipoFuncion;
    private Pelicula pelicula;
    private Date fecha;
    private Time horaInicio;
    private Time horaFinal;

    public Date getFecha() {
        return Date.valueOf(inicioFuncion.toLocalDateTime().format(DateTimeFormatter.ISO_DATE));
    }

    public Time getHoraInicio() {
        return Time.valueOf(inicioFuncion.toLocalDateTime().format(DateTimeFormatter.ISO_TIME));
    }
    
    public Time getHoraFinal() {
        return Time.valueOf(finalFuncion.toLocalDateTime().format(DateTimeFormatter.ISO_TIME));
    }

    public String getTipoFuncion() {
        FuncionController funcionController = new FuncionController();
        TipoFuncionController tipoFuncionController = new TipoFuncionController();
        return tipoFuncionController.findById(funcionController.findById(this.getId_funcion()).getId_tipoFuncion())
                .getDescripcion();
    }

    public void setTipoFuncion(String tipoFuncion) {
        this.tipoFuncion = tipoFuncion;
    }

    public Pelicula getPelicula() {
        FuncionController funcionController = new FuncionController();
        PeliculaController peliculaController = new PeliculaController();
        return peliculaController.findById(funcionController.findById(this.getId_funcion()).getId_pelicula());
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Timestamp getFechaFuncion() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return (Timestamp) dateFormat.parse(dateFormat.format(inicioFuncion));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Timestamp getHoraInicioFuncion() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            return (Timestamp) timeFormat.parse(timeFormat.format(inicioFuncion));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getId_funcion() {
        return id_funcion;
    }

    public void setId_funcion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public int getId_sala() {
        return id_sala;
    }

    public void setId_sala(int id_sala) {
        this.id_sala = id_sala;
    }

    public Timestamp getInicioFuncion() {
        return inicioFuncion;
    }

    public void setInicioFuncion(Timestamp inicioFuncion) {
        this.inicioFuncion = inicioFuncion;
    }

    public Timestamp getFinalFuncion() {
        return finalFuncion;
    }

    public void setFinalFuncion(Timestamp finalFuncion) {
        this.finalFuncion = finalFuncion;
    }

}
