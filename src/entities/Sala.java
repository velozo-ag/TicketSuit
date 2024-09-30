package entities;

public class Sala {
    private int id_sala;
    private String nombre;
    private int capacidad;
    private int id_cine;

    public int getIdSala() {
        return id_sala;
    }

    public void setIdSala(int id_sala) {
        this.id_sala = id_sala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getIdCine() {
        return id_cine;
    }

    public void setIdCine(int id_cine) {
        this.id_cine = id_cine;
    }

}
