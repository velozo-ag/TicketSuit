package entities;

public class TipoFuncion {
    private int id_tipoFuncion;
    private String descripcion;
    private float precio;

    public int getIdTipoFuncion() {
        return id_tipoFuncion;
    }

    public void setIdTipoFuncion(int id_tipoFuncion) {
        this.id_tipoFuncion = id_tipoFuncion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
