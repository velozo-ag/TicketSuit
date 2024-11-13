package views.gerente;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;
import entities.Funcion;
import entities.TipoFuncion;

import javax.swing.JFileChooser;

public class exportarDatosPDF {
    
    private Connection connection;

    public exportarDatosPDF() {
        // Obtener la conexión utilizando el patrón Singleton
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void generateReport() throws SQLException, DocumentException, FileNotFoundException {
        // Usar JFileChooser para permitir al usuario seleccionar la ubicación del archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar ubicación para guardar el reporte PDF");
        fileChooser.setSelectedFile(new File("reporte_cine.pdf")); // Nombre predeterminado del archivo
        
        // Mostrar el diálogo para guardar el archivo
        int userSelection = fileChooser.showSaveDialog(null);
        
        // Verificar si el usuario seleccionó "Guardar"
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            // Asegurarse de que el archivo tenga la extensión .pdf
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";  // Agregar la extensión si no está presente
            }

            // Crear el documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Título del reporte
            document.add(new Paragraph("Reporte de Cine", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));

            // Agregar Tickets vendidos por mes
            agregarTicketsPorMes(document);
            document.add(Chunk.NEWLINE);

            // Agregar Tickets por tipo de función
            agregarTicketsPorTipoFuncion(document);
            document.add(Chunk.NEWLINE);

            // Agregar Ingresos por mes
            agregarIngresosPorMes(document);
            document.add(Chunk.NEWLINE);

            // Consultar y agregar los datos de Películas por Género
            cargarPeliculasPorGenero(document);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Películas por Mes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
            
            // Consultar y agregar los datos de Películas por Mes
            cargarPeliculasPorMes(document);
            document.add(Chunk.NEWLINE);

            // Agregar título al reporte
            document.add(new Paragraph("Reporte de Ingresos por Género" , FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
            
            // Consultar y agregar los datos de Ingresos por Género
            cargarIngresosPorGenero(document);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Ingresos por Película" , FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
            
            // Consultar y agregar los datos de Ingresos por Película
            cargarIngresosPorPelicula(document);

            document.close();  // Cerrar el documento PDF
            System.out.println("Reporte generado exitosamente en: " + filePath);
        } else {
            System.out.println("El usuario canceló la operación.");
        }
    }

    private void cargarIngresosPorGenero(Document document) throws SQLException, DocumentException {
        // Consulta modificada sin fecha para los ingresos por género
        String query = """
            SELECT 
                g.descripcion AS genero, 
                SUM(t.valor) AS total_recaudado
            FROM 
                Ticket t
            JOIN 
                Sala_Funcion sf ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala AND t.inicio_funcion = sf.inicio_funcion
            JOIN 
                Funcion f ON sf.id_funcion = f.id_funcion
            JOIN 
                Pelicula p ON f.id_pelicula = p.id_pelicula
            JOIN 
                Genero_Pelicula gp ON p.id_pelicula = gp.id_pelicula
            JOIN 
                Genero g ON gp.id_genero = g.id_genero
            GROUP BY 
                g.descripcion
            ORDER BY 
                total_recaudado DESC;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            PdfPTable table = new PdfPTable(2);
            table.addCell("Género");
            table.addCell("Total Recaudado");

            while (rs.next()) {
                table.addCell(rs.getString("genero"));
                table.addCell(String.format("%.2f", rs.getDouble("total_recaudado")));
            }

            document.add(table);
        }
    }

    private void cargarIngresosPorPelicula(Document document) throws SQLException, DocumentException {
        // Consulta modificada sin fecha para los ingresos por película
        String query = """
            SELECT 
                p.nombre AS pelicula, 
                SUM(t.valor) AS total_recaudado
            FROM 
                Ticket t
            JOIN 
                Sala_Funcion sf ON t.id_funcion = sf.id_funcion AND t.id_sala = sf.id_sala AND t.inicio_funcion = sf.inicio_funcion
            JOIN 
                Funcion f ON sf.id_funcion = f.id_funcion
            JOIN 
                Pelicula p ON f.id_pelicula = p.id_pelicula
            GROUP BY 
                p.nombre
            ORDER BY 
                total_recaudado DESC;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            PdfPTable table = new PdfPTable(2);
            table.addCell("Película");
            table.addCell("Total Recaudado");

            while (rs.next()) {
                table.addCell(rs.getString("pelicula"));
                table.addCell(String.format("%.2f", rs.getDouble("total_recaudado")));
            }

            document.add(table);
        }
    }

    private void agregarTicketsPorMes(Document document) throws SQLException, DocumentException {
        String sql = "SELECT YEAR(sala_funcion.inicio_funcion) AS anio, " +
                     "MONTH(sala_funcion.inicio_funcion) AS mes, " +
                     "COUNT(ticket.id_ticket) AS total_tickets " +
                     "FROM Ticket ticket " +
                     "JOIN Sala_Funcion sala_funcion ON ticket.id_funcion = sala_funcion.id_funcion " +
                     "GROUP BY YEAR(sala_funcion.inicio_funcion), MONTH(sala_funcion.inicio_funcion) " +
                     "ORDER BY anio, mes;";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        document.add(new Paragraph("Tickets vendidos por mes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        PdfPTable table = new PdfPTable(3);
        table.addCell("Año");
        table.addCell("Mes");
        table.addCell("Total Tickets");

        while (rs.next()) {
            table.addCell(rs.getString("anio"));
            table.addCell(rs.getString("mes"));
            table.addCell(rs.getString("total_tickets"));
        }
        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void agregarTicketsPorTipoFuncion(Document document) throws SQLException, DocumentException {
        String sql = "SELECT TF.descripcion AS tipo_funcion, COUNT(T.id_ticket) AS total_tickets " +
                     "FROM Ticket T " +
                     "JOIN Sala_Funcion SF ON T.id_funcion = SF.id_funcion " +
                     "JOIN Funcion F ON SF.id_funcion = F.id_funcion " +
                     "JOIN TipoFuncion TF ON F.id_tipoFuncion = TF.id_tipoFuncion " +
                     "GROUP BY TF.descripcion;";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        document.add(new Paragraph("Tickets vendidos por tipo de función:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        PdfPTable table = new PdfPTable(2);
        table.addCell("Tipo de Funcion");
        table.addCell("Total Tickets");

        while (rs.next()) {
            table.addCell(rs.getString("tipo_funcion"));
            table.addCell(rs.getString("total_tickets"));
        }
        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void agregarIngresosPorMes(Document document) throws SQLException, DocumentException {
        String sql = "SELECT YEAR(fecha) AS anio, MONTH(fecha) AS mes, SUM(subtotal) AS total_ingresos " +
                     "FROM Compra " +
                     "GROUP BY YEAR(fecha), MONTH(fecha) ORDER BY YEAR(fecha), MONTH(fecha);";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        document.add(new Paragraph("Ingresos por mes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        PdfPTable table = new PdfPTable(3);
        table.addCell("Año");
        table.addCell("Mes");
        table.addCell("Total Ingresos");

        while (rs.next()) {
            table.addCell(rs.getString("anio"));
            table.addCell(rs.getString("mes"));
            table.addCell(rs.getString("total_ingresos"));
        }
        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void cargarPeliculasPorGenero(Document document) throws SQLException, DocumentException {
        // Consulta modificada sin fecha
        String query = """
            SELECT g.descripcion AS genero, COUNT(DISTINCT gp.id_pelicula) AS cantidad_peliculas
            FROM Genero g
            JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero
            JOIN Pelicula p ON gp.id_pelicula = p.id_pelicula
            JOIN Funcion f ON p.id_pelicula = f.id_pelicula
            GROUP BY g.descripcion
            ORDER BY cantidad_peliculas DESC;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            PdfPTable table = new PdfPTable(2);
            table.addCell("Género");
            table.addCell("Cantidad de Películas");

            while (rs.next()) {
                table.addCell(rs.getString("genero"));
                table.addCell(String.valueOf(rs.getInt("cantidad_peliculas")));
            }

            document.add(table);
        }
    }

    private void cargarPeliculasPorMes(Document document) throws SQLException, DocumentException {
        // Consulta modificada sin fecha
        String query = "SELECT YEAR(funcion.fecha_ingreso) AS anio, " +
                       "MONTH(funcion.fecha_ingreso) AS mes, " +
                       "COUNT(DISTINCT funcion.id_pelicula) AS total_peliculas " +
                       "FROM Funcion funcion " +
                       "GROUP BY YEAR(funcion.fecha_ingreso), MONTH(funcion.fecha_ingreso) " +
                       "ORDER BY anio, mes;";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            PdfPTable table = new PdfPTable(3);
            table.addCell("Año");
            table.addCell("Mes");
            table.addCell("Total Películas");

            while (rs.next()) {
                table.addCell(rs.getString("anio"));
                table.addCell(rs.getString("mes"));
                table.addCell(String.valueOf(rs.getInt("total_peliculas")));
            }

            document.add(table);
        }
    }
}