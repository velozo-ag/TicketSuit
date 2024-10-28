package entities;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Sala_Funcion {
    private int id_funcion;
    private int id_sala;
    private Date inicioFuncion;
    private Date finalFuncion;

    public Date getFechaFuncion() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return (Date) dateFormat.parse(dateFormat.format(inicioFuncion));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getHoraInicioFuncion() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            return (Date) timeFormat.parse(timeFormat.format(inicioFuncion));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public Date getInicioFuncion() {
        return inicioFuncion;
    }

    public void setInicioFuncion(Date inicioFuncion) {
        this.inicioFuncion = inicioFuncion;
    }

    public Date getFinalFuncion() {
        return finalFuncion;
    }

    public void setFinalFuncion(Date finalFuncion) {
        this.finalFuncion = finalFuncion;
    }

}
