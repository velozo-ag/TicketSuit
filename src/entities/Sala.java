package entities;

public class Sala {
    private int id_sala;
    private String nombre;
    private int capacidad;
    private int id_cine;
    private int estado;
    private String estadoNombre;

    private int cantFilas;
    private int cantColumnas;

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
        this.setEstadoNombre(estado == 1 ? "Activa" : "Inactiva");
    }

    public int getCantFilas() {
        return cantFilas;
    }

    public void setCantFilas(int cantFilas) {
        this.cantFilas = cantFilas;
    }

    public int getCantColumnas() {
        return cantColumnas;
    }

    public void setCantColumnas(int cantColumnas) {
        this.cantColumnas = cantColumnas;
    }

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

    public String getDimensiones() {
        return capacidad + " (" + this.getCantFilas() + "x" + this.getCantColumnas() + ")";
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
