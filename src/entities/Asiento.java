package entities;

public class Asiento {
    private int id_asiento;
    private int id_sala;
    private String letra_fila;
    private int numero_columna;
    private int estado;

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdAsiento() {
        return id_asiento;
    }

    public void setIdAsiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getIdSala() {
        return id_sala;
    }

    public void setIdSala(int id_sala) {
        this.id_sala = id_sala;
    }

    public String getLetraFila() {
        return letra_fila;
    }

    public void setLetraFila(String letra_fila) {
        this.letra_fila = letra_fila;
    }

    public int getNumeroColumna() {
        return numero_columna;
    }

    public void setNumeroColumna(int numero_columna) {
        this.numero_columna = numero_columna;
    }

    public String toString(){
        return id_asiento + " " + id_sala + " " + letra_fila + numero_columna;
     }
}
