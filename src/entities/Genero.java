package entities;

public class Genero {
    private int id_genero;
    private String descripcion;

    public int getIdGenero() {
        return id_genero;
    }

    public void setIdGenero(int id_genero) {
        this.id_genero = id_genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString(){
        return this.getDescripcion();
    }
}
