package entities;

import java.util.List;

import controllers.ClasificacionController;
import controllers.DirectorController;

public class Pelicula {
    private int id_pelicula;
    private String nombre;
    private int duracion;
    private int id_clasificacion;
    private String nombreClasificacion;
    private int id_director;
    private String nombreDirector;
    private String sinopsis;
    private String imagen;
    private String nacionalidad;
    private List<Genero> generos;

    DirectorController directorController = new DirectorController();
    ClasificacionController clasificacionController = new ClasificacionController();

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public int getIdPelicula() {
        return id_pelicula;
    }

    public void setIdPelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getIdClasificacion() {
        return id_clasificacion;
    }

    public void setIdClasificacion(int id_clasificacion) {
        this.id_clasificacion = id_clasificacion;
        this.setNombreClasificacion(clasificacionController.findById(id_clasificacion).getNombre());
    }

    public String getNombreClasificacion() {
        return nombreClasificacion;
    }

    public void setNombreClasificacion(String nombreClasificacion) {
        this.nombreClasificacion = nombreClasificacion;
    }

    public int getIdDirector() {
        return id_director;
    }

    public void setIdDirector(int id_director) {
        this.id_director = id_director;
        this.setNombreDirector(directorController.findById(id_pelicula).getNombre());
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
