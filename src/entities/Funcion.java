package entities;

import java.sql.Date;

import controllers.PeliculaController;
import controllers.TipoFuncionController;

public class Funcion {
    private int id_funcion;
    private String hora_inicio;
    private String hora_final;
    private Date fecha;
    private int id_pelicula;
    private String nombrePelicula;
    private int id_tipoFuncion;
    private String tipoFuncion;
    private Sala sala;
    private String nombreSala;

    private PeliculaController peliculaController = new PeliculaController();
    private TipoFuncionController tipoFuncionController = new TipoFuncionController();

    public int getIdFuncion() {
        return id_funcion;
    }

    public void setIdFuncion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdPelicula() {
        return id_pelicula;
    }

    public void setIdPelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
        this.setNombrePelicula(peliculaController.findById(id_pelicula).getNombre());
    }

    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setNombrePelicula(String nombrePelicula) {
        this.nombrePelicula = nombrePelicula;
    }

    public int getIdTipoFuncion() {
        return id_tipoFuncion;
    }

    public void setIdTipoFuncion(int id_tipoFuncion) {
        this.id_tipoFuncion = id_tipoFuncion;
        this.setTipoFuncion(tipoFuncionController.findById(id_tipoFuncion).getDescripcion());
    }

    public String getTipoFuncion() {
        return tipoFuncion;
    }

    public void setTipoFuncion(String tipoFuncion) {
        this.tipoFuncion = tipoFuncion;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
        this.setNombreSala(sala.getNombre());
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }
}
