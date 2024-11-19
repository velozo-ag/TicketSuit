package views.gerente;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import database.DatabaseConnection;
import entities.Funcion;
import entities.TipoFuncion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.JFileChooser;

public class exportarDatosPDF {
    
    private Connection connection;

    @FXML
    private Button bCerrar;

    private LocalDate desde;
    private LocalDate hasta;

    public exportarDatosPDF() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void setFechas(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
        System.out.println("Fechas recibidas: Desde " + desde + " Hasta " + hasta);
    }

    private String generarNombreUnico(String basePath, String extension) {
        int count = 1;
        String path = basePath + "." + extension;
        while (new File(path).exists()) {
            path = basePath + " (" + count + ")." + extension;
            count++;
        }
        return path;
    }

    public void generateReport() throws SQLException, DocumentException, FileNotFoundException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar ubicación para guardar el reporte PDF");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String baseFilePath = selectedDirectory.getAbsolutePath() + File.separator + "reporteTotal_cine";
            String filePath = generarNombreUnico(baseFilePath, "pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Reporte total de datos. TicketSuit:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));

            agregarTicketsPorMes(document);
            document.add(Chunk.NEWLINE);

            agregarTicketsPorTipoFuncion(document);
            document.add(Chunk.NEWLINE);

            agregarIngresosPorMes(document);
            document.add(Chunk.NEWLINE);

            cargarPeliculasPorGenero(document);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Películas por Mes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);

            cargarPeliculasPorMes(document);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Ingresos por Género", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);

            cargarIngresosPorGenero(document);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Ingresos por Película", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);

            cargarIngresosPorPelicula(document);

            document.close();
            System.out.println("Reporte generado exitosamente en: " + filePath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reporte Generado");
            alert.setHeaderText(null);
            alert.setContentText("El reporte PDF se generó exitosamente en:\n" + filePath);
            alert.showAndWait();
        } else {
            System.out.println("El usuario canceló la operación.");
        }
    }

    @FXML
    void onGenerateReportFechas(ActionEvent event) {
        try {
            generateReportFechas(desde, hasta);
        } catch (SQLException | DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al generar reporte");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al generar el reporte. Por favor, intenta nuevamente.");
            alert.showAndWait();
        }
    }

    public void generateReportFechas(LocalDate desde, LocalDate hasta) throws SQLException, DocumentException, FileNotFoundException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar ubicación para guardar el reporte PDF");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String baseFilePath = selectedDirectory.getAbsolutePath() + File.separator + "reporteParcial_cine";
            String filePath = generarNombreUnico(baseFilePath, "pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Reporte de datos entre " + desde + " hasta " + hasta + ".TicketSuit", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));

            agregarTicketsPorMesFechas(document, desde, hasta);
            document.add(Chunk.NEWLINE);

            agregarTicketsPorTipoFuncionFechas(document, desde, hasta);
            document.add(Chunk.NEWLINE);

            agregarIngresosPorMesFechas(document, desde, hasta);
            document.add(Chunk.NEWLINE);

            cargarPeliculasPorGeneroFechas(document, desde, hasta);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Películas por Mes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);

            cargarPeliculasPorMesFechas(document, desde, hasta);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Ingresos por Género", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);

            cargarIngresosPorGeneroFechas(document, desde, hasta);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Reporte de Ingresos por Película", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);

            cargarIngresosPorPeliculaFechas(document, desde, hasta);

            document.close();
            System.out.println("Reporte generado exitosamente en: " + filePath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reporte Generado");
            alert.setHeaderText(null);
            alert.setContentText("El reporte PDF se generó exitosamente en:\n" + filePath);
            alert.showAndWait();
        } else {
            System.out.println("El usuario canceló la operación.");
        }
    }

    private void cargarIngresosPorGeneroFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
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
            WHERE 
                sf.inicio_funcion BETWEEN ? AND ?
            GROUP BY 
                g.descripcion
            ORDER BY 
                total_recaudado DESC;
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
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

    private void cargarIngresosPorGenero(Document document) throws SQLException, DocumentException {
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

    private void cargarIngresosPorPeliculaFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
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
            WHERE 
                sf.inicio_funcion BETWEEN ? AND ?
            GROUP BY 
                p.nombre
            ORDER BY 
                total_recaudado DESC;
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
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

    private void cargarIngresosPorPelicula(Document document) throws SQLException, DocumentException {
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

    private void agregarTicketsPorMesFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
        String sql = """
            SELECT 
                YEAR(sala_funcion.inicio_funcion) AS anio, 
                MONTH(sala_funcion.inicio_funcion) AS mes, 
                COUNT(ticket.id_ticket) AS total_tickets
            FROM 
                Ticket ticket
            JOIN 
                Sala_Funcion sala_funcion ON ticket.id_funcion = sala_funcion.id_funcion
            WHERE 
                sala_funcion.inicio_funcion BETWEEN ? AND ?
            GROUP BY 
                YEAR(sala_funcion.inicio_funcion), MONTH(sala_funcion.inicio_funcion)
            ORDER BY 
                anio, mes;
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
            ResultSet rs = stmt.executeQuery();
    
            document.add(new Paragraph("Tickets vendidos por mes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
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
        document.add(Chunk.NEWLINE);
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

    private void agregarTicketsPorTipoFuncionFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
        String sql = """
            SELECT 
                TF.descripcion AS tipo_funcion, 
                COUNT(T.id_ticket) AS total_tickets
            FROM 
                Ticket T
            JOIN 
                Sala_Funcion SF ON T.id_funcion = SF.id_funcion
            JOIN 
                Funcion F ON SF.id_funcion = F.id_funcion
            JOIN 
                TipoFuncion TF ON F.id_tipoFuncion = TF.id_tipoFuncion
            WHERE 
                SF.inicio_funcion BETWEEN ? AND ?
            GROUP BY 
                TF.descripcion;
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
            ResultSet rs = stmt.executeQuery();
    
            document.add(new Paragraph("Tickets vendidos por tipo de función:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
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
        document.add(Chunk.NEWLINE);
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

    private void agregarIngresosPorMesFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
        String sql = """
            SELECT 
                YEAR(fecha) AS anio, 
                MONTH(fecha) AS mes, 
                SUM(subtotal) AS total_ingresos
            FROM 
                Compra
            WHERE 
                fecha BETWEEN ? AND ?
            GROUP BY 
                YEAR(fecha), MONTH(fecha)
            ORDER BY 
                YEAR(fecha), MONTH(fecha);
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
            ResultSet rs = stmt.executeQuery();
    
            document.add(new Paragraph("Ingresos por mes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(3);
            table.addCell("Año");
            table.addCell("Mes");
            table.addCell("Total Ingresos");
    
            while (rs.next()) {
                table.addCell(rs.getString("anio"));
                table.addCell(rs.getString("mes"));
                table.addCell(String.format("%.2f", rs.getDouble("total_ingresos")));
            }
    
            document.add(table);
            document.add(new Paragraph(" "));
        }
    }

    private void agregarIngresosPorMes(Document document) throws SQLException, DocumentException {
        String sql = "SELECT YEAR(fecha) AS anio, MONTH(fecha) AS mes, SUM(subtotal) AS total_ingresos " +
                     "FROM Compra " +
                     "GROUP BY YEAR(fecha), MONTH(fecha) ORDER BY YEAR(fecha), MONTH(fecha);";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        document.add(new Paragraph("Ingresos por mes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        document.add(Chunk.NEWLINE);
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

    private void cargarPeliculasPorGeneroFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
        String query = """
            SELECT 
                g.descripcion AS genero, 
                COUNT(DISTINCT gp.id_pelicula) AS cantidad_peliculas
            FROM 
                Genero g
            JOIN 
                Genero_Pelicula gp ON g.id_genero = gp.id_genero
            JOIN 
                Pelicula p ON gp.id_pelicula = p.id_pelicula
            JOIN 
                Funcion f ON p.id_pelicula = f.id_pelicula
            WHERE 
                f.fecha_ingreso BETWEEN ? AND ?
            GROUP BY 
                g.descripcion
            ORDER BY 
                cantidad_peliculas DESC;
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
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

    private void cargarPeliculasPorGenero(Document document) throws SQLException, DocumentException {
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

    private void cargarPeliculasPorMesFechas(Document document, LocalDate desde, LocalDate hasta) throws SQLException, DocumentException {
        String query = """
            SELECT 
                YEAR(f.fecha_ingreso) AS anio, 
                MONTH(f.fecha_ingreso) AS mes, 
                COUNT(DISTINCT f.id_pelicula) AS total_peliculas
            FROM 
                Funcion f
            WHERE 
                f.fecha_ingreso BETWEEN ? AND ?
            GROUP BY 
                YEAR(f.fecha_ingreso), MONTH(f.fecha_ingreso)
            ORDER BY 
                anio, mes;
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(desde));
            stmt.setDate(2, java.sql.Date.valueOf(hasta));
    
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

    private void cargarPeliculasPorMes(Document document) throws SQLException, DocumentException {
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

    @FXML
    void CerrarFormulario(ActionEvent event) {
        Stage stage = (Stage) bCerrar.getScene().getWindow();
        stage.close();
    }
}