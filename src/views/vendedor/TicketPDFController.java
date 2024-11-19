package views.vendedor;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import controllers.AsientoController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import database.DatabaseConnection;
import entities.Asiento;

public class TicketPDFController {

    private Connection connection;
    private AsientoController asientoController = new AsientoController();

    public TicketPDFController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void generarTicketPDF(int ticketId) {
        Document document = new Document(PageSize.A6, 20, 20, 20, 20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try { 
            String basePath = "Tickets/Ticket_" + ticketId;
            // String basePath = "D:\\Repos2\\TicketSuit\\tickets\\Ticket_" + ticketId;
            String outputPath = obtenerRutaDisponible(basePath); 

            PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            document.open();

            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Font fontContent = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            Font fontFooter = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

            Image logo = Image.getInstance("src/resources/logo.png");
            logo.scaleToFit(50, 50);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            
            Paragraph title = new Paragraph("Ticket de Compra", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            
            Paragraph separator = new Paragraph("-----------------------------");
            separator.setAlignment(Element.ALIGN_CENTER);
            document.add(separator);
            document.add(new Paragraph(" "));

            try {
                String query = "SELECT T.id_ticket, T.id_funcion, T.id_sala, T.inicio_funcion, T.id_asiento, T.id_compra, " +
                               "F.fecha_ingreso, P.nombre AS pelicula_nombre, S.nombre AS sala_nombre, T.valor " +
                               "FROM Ticket T " +
                               "JOIN Funcion F ON T.id_funcion = F.id_funcion " +
                               "JOIN Pelicula P ON F.id_pelicula = P.id_pelicula " +
                               "JOIN Sala S ON T.id_sala = S.id_sala " +
                               "WHERE T.id_ticket = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, ticketId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(10f);

                    addTableRow(table, "ID Ticket:", String.valueOf(resultSet.getInt("id_ticket")), fontContent);
                    addTableRow(table, "Película:", resultSet.getString("pelicula_nombre"), fontContent);
                    addTableRow(table, "Fecha:", dateFormat.format(resultSet.getDate("inicio_funcion")), fontContent);
                    addTableRow(table, "Hora:", resultSet.getString("inicio_funcion"), fontContent);
                    addTableRow(table, "Sala:", resultSet.getString("sala_nombre"), fontContent);
                    addTableRow(table, "Asiento:", obtenerAsiento(asientoController.findById(resultSet.getInt("id_asiento"), resultSet.getInt("id_sala"))), fontContent);
                    // addTableRow(table, "Asiento:", String.valueOf(resultSet.getInt("id_asiento")), fontContent);
                    addTableRow(table, "Valor:", "$" + resultSet.getDouble("valor"), fontContent);

                    document.add(table);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            document.add(new Paragraph(" "));
            document.add(separator);

            Paragraph footer = new Paragraph("Gracias por su compra\nTicketSuit", fontFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            System.out.println("Ticket generado en: " + outputPath);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addTableRow(PdfPTable table, String column1, String column2, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(column1, font));
        PdfPCell cell2 = new PdfPCell(new Phrase(column2, font));

        cell1.setBorderWidth(1);
        cell2.setBorderWidth(1);
        cell1.setPadding(5);
        cell2.setPadding(5);

        table.addCell(cell1);
        table.addCell(cell2);
    }

    private String obtenerRutaDisponible(String basePath) {
        File file = new File(basePath + ".pdf");
        if (!file.exists()) {
            return file.getAbsolutePath();
        } else {
            int count = 1;
            String newFilePath;
            do {
                newFilePath = basePath + "(" + count + ").pdf";
                file = new File(newFilePath);
                count++;
            } while (file.exists());
            return newFilePath;
        }
    }

    private String obtenerAsiento(Asiento asiento){
        return asiento.getLetraFila() + asiento.getNumeroColumna();
    }
}
