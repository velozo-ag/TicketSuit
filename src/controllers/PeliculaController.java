package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import database.DatabaseConnection;
import entities.Director;
import entities.Pelicula;

public class PeliculaController {
    Connection connection = DatabaseConnection.getInstance().connect();
    private GeneroController generoController = new GeneroController();

    public PeliculaController() {
                // this.connection = DatabaseConnection.getInstance().connect();

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

    public Pelicula findById(int id_pelicula) {
        String query = "SELECT * " +
                "FROM Pelicula WHERE id_pelicula = ?";
        Pelicula pelicula = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_pelicula);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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
        } catch (SQLException e) {
            System.out.println("Error al encontrar pelicula: " + e.getMessage());
        }

        return pelicula;
    }

    public List<Pelicula> findByNombre(String nombre) {
        String query = "SELECT * FROM Pelicula WHERE nombre LIKE ?";
        List<Pelicula> peliculas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, "%" + nombre + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setDuracion(rs.getInt("duracion"));
                pelicula.setIdClasificacion(rs.getInt("id_clasificacion"));
                pelicula.setIdDirector(rs.getInt("id_director"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setImagen(rs.getString("imagen"));
                pelicula.setNacionalidad(rs.getString("nacionalidad"));
                pelicula.setGeneros(generoController.findByPelicula(rs.getInt("id_pelicula")));

                peliculas.add(pelicula);
                System.out.println(pelicula.getNombre());
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar pelicula: " + e.getMessage());
        }

        return peliculas;
    }

    public List<Pelicula> findByNombreFecha(String nombre, Date fecha) {
        String query = "SELECT DISTINCT p.* FROM Pelicula p " +
                "   JOIN Funcion f ON f.id_pelicula = p.id_pelicula " +
                "   JOIN Sala_Funcion sf ON f.id_funcion = sf.id_funcion " +
                "WHERE p.nombre LIKE ? AND CAST(sf.inicio_funcion AS DATE) = ? ";
        List<Pelicula> peliculas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, "%" + nombre + "%");
            stmt.setDate(2, fecha);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setDuracion(rs.getInt("duracion"));
                pelicula.setIdClasificacion(rs.getInt("id_clasificacion"));
                pelicula.setIdDirector(rs.getInt("id_director"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setImagen(rs.getString("imagen"));
                pelicula.setNacionalidad(rs.getString("nacionalidad"));
                pelicula.setGeneros(generoController.findByPelicula(rs.getInt("id_pelicula")));

                peliculas.add(pelicula);
                System.out.println(pelicula.getNombre());
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar pelicula: " + e.getMessage());
        }

        return peliculas;
    }

    public List<Pelicula> findByFecha(Date fecha) {
        String query = "SELECT DISTINCT p.* FROM Pelicula p " +
                "   JOIN Funcion f ON f.id_pelicula = p.id_pelicula " +
                "   JOIN Sala_Funcion sf ON f.id_funcion = sf.id_funcion " +
                "WHERE CAST(sf.inicio_funcion AS DATE) = ? ";
        List<Pelicula> peliculas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, fecha);
            System.out.println(fecha.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setDuracion(rs.getInt("duracion"));
                pelicula.setIdClasificacion(rs.getInt("id_clasificacion"));
                pelicula.setIdDirector(rs.getInt("id_director"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setImagen(rs.getString("imagen"));
                pelicula.setNacionalidad(rs.getString("nacionalidad"));
                pelicula.setGeneros(generoController.findByPelicula(rs.getInt("id_pelicula")));

                peliculas.add(pelicula);
                System.out.println(pelicula.getNombre());
            }

        } catch (SQLException e) {
            System.out.println("Error al encontrar pelicula: " + e.getMessage());
        }

        return peliculas;
    }

    public void createPelicula(Pelicula pelicula, String nombreDirector) {
        DirectorController directorController = new DirectorController();

        Director director = directorController.findByName(nombreDirector);

        if (director == null) {
            director = new Director();
            director.setNombre(nombreDirector);
            directorController.createDirector(director);

            director = directorController.findByName(nombreDirector);
        }

        int idDirector = director.getIdDirector();

        String query = "INSERT INTO Pelicula (nombre, duracion, id_clasificacion, id_director, sinopsis, imagen, nacionalidad) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, pelicula.getNombre());
            preparedStatement.setInt(2, pelicula.getDuracion());
            preparedStatement.setInt(3, pelicula.getIdClasificacion());
            preparedStatement.setInt(4, idDirector);
            preparedStatement.setString(5, pelicula.getSinopsis());
            preparedStatement.setString(6, pelicula.getImagen());
            preparedStatement.setString(7, pelicula.getNacionalidad());

            preparedStatement.executeUpdate();

            System.out.println("Película creada con éxito");

        } catch (SQLException e) {
            System.out.println("Error al crear la película: " + e.getMessage());
        }
    }
}
