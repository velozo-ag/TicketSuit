package entities;

public class Clasificacion {
    private int id_clasificacion;
    private String descripcion;
    private String nombre;

    public int getIdClasificacion() {
        return id_clasificacion;
    }

    public void setIdClasificacion(int id_clasificacion) {
        this.id_clasificacion = id_clasificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
