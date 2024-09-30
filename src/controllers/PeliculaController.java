package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import database.DatabaseConnection;
import entities.Pelicula;

public class PeliculaController {
    private Connection connection;
    private GeneroController generoController = new GeneroController();

    public PeliculaController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Pelicula> findAll() {
        String query = "SELECT Pelicula.id_pelicula, Pelicula.nombre, Pelicula.duracion, " +
                "Pelicula.id_clasificacion, Pelicula.id_director, Pelicula.sinopsis, " +
                "Pelicula.imagen, Pelicula.nacionalidad " +
                "FROM Pelicula";

        List<Pelicula> peliculas = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();

                int idPelicula = rs.getInt("id_pelicula");
                String nombre = rs.getString("nombre");
                int duracion = rs.getInt("duracion");
                int idClasificacion = rs.getInt("id_clasificacion");
                int idDirector = rs.getInt("id_director");
                String sinopsis = rs.getString("sinopsis");
                String imagen = rs.getString("imagen");
                String nacionalidad = rs.getString("nacionalidad");

                pelicula.setIdPelicula(idPelicula);
                pelicula.setNombre(nombre);
                pelicula.setDuracion(duracion);
                pelicula.setIdClasificacion(idClasificacion);
                pelicula.setIdDirector(idDirector);
                pelicula.setSinopsis(sinopsis);
                pelicula.setImagen(imagen);
                pelicula.setNacionalidad(nacionalidad);
                pelicula.setGeneros(generoController.findByPelicula(idPelicula));

                peliculas.add(pelicula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return peliculas;
    }

    public Pelicula findById(int id_pelicula){
        String query = "SELECT Pelicula.id_pelicula, Pelicula.nombre, Pelicula.duracion, " +
        "Pelicula.id_clasificacion, Pelicula.id_director, Pelicula.sinopsis, " +
        "Pelicula.imagen, Pelicula.nacionalidad " +
        "FROM Pelicula WHERE id_pelicula = ?";
        Pelicula pelicula = null;

        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_pelicula);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setDuracion(rs.getInt("duracion"));
                pelicula.setIdClasificacion(rs.getInt("id_clasificacion"));
                pelicula.setIdDirector(rs.getInt("id_director"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setImagen(rs.getString("imagen"));
                pelicula.setNacionalidad(rs.getString("nacionalidad"));
                pelicula.setGeneros(generoController.findByPelicula(rs.getInt("id_pelicula")));
            }
        }catch(SQLException e){
            System.out.println("Error al encontrar pelicula: " + e.getMessage());
        }

        return pelicula;
    }
}
