package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import entities.Funcion;
import entities.TipoFuncion;

public class TipoFuncionController {
    Connection connection;
    
    public TipoFuncionController(){
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public TipoFuncion findById(int id_tipoFuncion){
        String query = "SELECT TipoFuncion.id_tipoFuncion, TipoFuncion.descripcion, TipoFuncion.precio FROM TipoFuncion WHERE id_tipoFuncion = ?";
        TipoFuncion tipoFuncion = null;
        
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id_tipoFuncion);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                tipoFuncion = new TipoFuncion();
                tipoFuncion.setIdTipoFuncion(rs.getInt("id_tipoFuncion"));
                tipoFuncion.setDescripcion(rs.getString("descripcion"));
                tipoFuncion.setPrecio(rs.getFloat("precio"));
            }
        }catch(SQLException e){
            System.out.println("Error al encontrar tipo de funcion: " + e.getMessage());
        }

        return tipoFuncion;
    }
}
