package entities;

import controllers.PerfilController;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String password;
    private boolean estado;
    private int id_perfil;
    private String nombrePerfil;
    private int id_cine;

    public void setIdUsuario(int id_usuario){
        this.id_usuario = id_usuario;
    }

    public int getIdUsuario(){
        return this.id_usuario;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return this.password;
    }

    public void setEstado(boolean estado){
        this.estado = estado;
    }
    
    public boolean getEstado(){
        return this.estado;
    }
    
    public void setIdPerfil(int id_perfil){
        this.id_perfil = id_perfil;
    }
    
    public int getIdPerfil(){
        return this.id_perfil;
    }

    public void setNombrePerfil(String nombrePerfil){
        this.nombrePerfil = nombrePerfil;
    }

    public String getNombrePerfil(){
        return this.nombrePerfil;
    }

    public void setIdCine(int id_cine){
        this.id_cine = id_cine;
    }

    public int getIdCine(){
        return this.id_cine;
    }
}
