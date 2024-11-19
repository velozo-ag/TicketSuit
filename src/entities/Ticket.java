package entities;

import java.sql.Timestamp;

public class Ticket {
    private int id_ticket;
    private int id_funcion;
    private int id_sala;
    private Timestamp inicioFuncion;

    private int id_asiento;
    private int id_compra;
    private float valor;

    public int getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(int id_ticket) {
        this.id_ticket = id_ticket;
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

    public int getId_asiento() {
        return id_asiento;
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

}
