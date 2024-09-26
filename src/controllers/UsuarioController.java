package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import entities.Usuario;

public class UsuarioController {
    private Connection connection;

    public UsuarioController(Connection connection) {
        this.connection = connection;
    }

    public Usuario createUsuario(Usuario usuario) {
        String query = "INSERT INTO Usuario (nombre, password, estado, id_perfil, id_cine) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPassword());
            stmt.setBoolean(3, usuario.getEstado());
            stmt.setInt(4, usuario.getIdPerfil());
            stmt.setInt(5, usuario.getIdCine());
            stmt.executeUpdate();
            System.out.println("Usuario creado con éxito");

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }
        return usuario;
    }

    public Usuario findById(int id_usuario) throws SQLException {
        String query = "SELECT * FROM Usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setIdCine(rs.getInt("id_cine"));
            }
        } catch (SQLException e) {
            System.out.println("Error al leer usuario: " + e.getMessage());
        }

        return usuario;
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuario";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                usuario.setIdPerfil(rs.getInt("id_perfil"));
                usuario.setIdCine(rs.getInt("id_cine"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer usuarios: " + e.getMessage());
        }
        return usuarios;
    }
}
