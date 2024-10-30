package entities;

import java.sql.Date;

import controllers.PeliculaController;
import controllers.TipoFuncionController;

public class Funcion {
    private int id_funcion;
    private int id_pelicula;
    private int id_tipoFuncion;

    private Date fechaIngreso;
    private Date fechaFinal;

    private PeliculaController peliculaController = new PeliculaController();
    private TipoFuncionController tipoFuncionController = new TipoFuncionController();

    public int getId_funcion() {
        return id_funcion;
    }

    public void setId_funcion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public int getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public String getNombrePelicula() {
        return peliculaController.findById(this.getId_pelicula()).getNombre();
    }

    public int getId_tipoFuncion() {
        return id_tipoFuncion;
    }

    public void setId_tipoFuncion(int id_tipoFuncion) {
        this.id_tipoFuncion = id_tipoFuncion;
    }

    public String getTipoFuncion() {
        return tipoFuncionController.findById(this.getId_tipoFuncion()).getDescripcion();
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String toString() {
        return this.getTipoFuncion() + " - " + this.getNombrePelicula();
    }

}
