package entities;

public class Director {
    private int id_director;
    private String nombre;

    public int getIdDirector() {
        return id_director;
    }
    
    public void setIdDirector(int id_director) {
        this.id_director = id_director;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toString(){
        return this.getNombre();
    }
}
