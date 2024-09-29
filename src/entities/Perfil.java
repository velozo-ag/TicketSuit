package entities;

public class Perfil {
    private int id_perfil;
    private String nombre;

    public Perfil(){
    }

    public Perfil(int id_perfil, String nombre){
        this.setIdPerfil(id_perfil);
        this.setNombre(nombre);
    }

    public void setIdPerfil(int id_perfil){
        this.id_perfil = id_perfil;
    }

    public int getIdPerfil(){
        return this.id_perfil;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String toString(){
        return this.getIdPerfil() + " - " + this.getNombre();
    }
}
