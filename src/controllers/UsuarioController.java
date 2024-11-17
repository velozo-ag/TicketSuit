package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;

import java.sql.Statement;

import entities.Usuario;

public class UsuarioController {
    Connection connection = DatabaseConnection.getInstance().connect();

    public UsuarioController() {
        // this.connection = DatabaseConnection.getInstance().connect();

    }

    public Usuario createUsuario(Usuario usuario) {
        String query = "INSERT INTO Usuario (nombre_completo, password, estado, id_perfil, id_cine, dni) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPassword());
            stmt.setBoolean(3, usuario.getEstado());
            stmt.setInt(4, usuario.getIdPerfil());
            stmt.setInt(5, usuario.getIdCine());
            stmt.setInt(6, usuario.getDni());
            stmt.executeUpdate();
            System.out.println("Usuario creado");

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }
        return usuario;
    }

    public void updateUsuario(Usuario usuario) {
        String query = "UPDATE Usuario SET nombre_completo = ?, password = ?, estado = ?, id_perfil = ?, dni = ? WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPassword());
            stmt.setBoolean(3, usuario.getEstado());
            stmt.setInt(4, usuario.getIdPerfil());
            stmt.setInt(5, usuario.getDni());
            stmt.setInt(6, usuario.getIdUsuario());
            stmt.executeUpdate();
            System.out.println("Usuario actualizado");

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public Usuario findById(int id_usuario) {
        String query = "SELECT * FROM Usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre_completo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setIdCine(rs.getInt("id_cine"));
                usuario.setDni(rs.getInt("dni"));

            }
        } catch (SQLException e) {
            System.out.println("Usuario no encontrado " + e.getMessage());
        }

        return usuario;
    }

    public Usuario findByDni(int dni) {
        String query = "SELECT * FROM Usuario WHERE dni = ?";
        Usuario usuario = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre_completo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setIdCine(rs.getInt("id_cine"));
                usuario.setDni(rs.getInt("dni"));

            }
        } catch (SQLException e) {
            System.out.println("Usuario no encontrado " + e.getMessage());
        }

        return usuario;
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT u.*, p.nombre AS nombre_perfil "
                + "FROM Usuario u "
                + "JOIN Perfil p ON u.id_perfil = p.id_perfil";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre_completo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setNombrePerfil(rs.getString("nombre_perfil"));
                usuario.setDni(rs.getInt("dni"));
                usuario.setIdCine(rs.getInt(1));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public List<Usuario> findNotAdmin() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT u.*, p.nombre AS nombre_perfil "
                + "FROM Usuario u "
                + "JOIN Perfil p ON u.id_perfil = p.id_perfil "
                + "WHERE u.id_perfil != 1";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setDni(rs.getInt("dni"));
                usuario.setNombrePerfil(rs.getString("nombre_perfil"));
                usuario.setIdCine(rs.getInt(1));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario findByUser(String user) {
        String query = "SELECT * FROM Usuario WHERE nombre_completo LIKE ?";
        Usuario usuario = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre_completo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setIdCine(rs.getInt("id_cine"));
                usuario.setDni(rs.getInt("dni"));
            }

        } catch (SQLException e) {
            System.out.println("Usuario no encontrado");
        }

        return usuario;
    }

    public List<Usuario> findByContain(String user) {
        String query = "SELECT u.*, p.nombre AS nombre_perfil FROM Usuario u "
                + "JOIN Perfil p ON u.id_perfil = p.id_perfil "
                + "WHERE u.nombre_completo LIKE ? OR u.dni LIKE ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // stmt.setString(1, '%' + user + '%');
            stmt.setString(1, user + '%');
            stmt.setString(2, user + '%');
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre_completo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setDni(rs.getInt("dni"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setNombrePerfil(rs.getString("nombre_perfil"));
                usuario.setIdCine(rs.getInt("id_cine"));

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Usuario no encontrado");
        }

        return usuarios;
    }
}
