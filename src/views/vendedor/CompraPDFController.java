package views.vendedor;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;

public class CompraPDFController {

    private Connection connection;

    public CompraPDFController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void generarCompraPDF(int compraId) {
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);

        try {
            String basePath = "ticketsCompra/Compra_" + compraId;
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

            Paragraph title = new Paragraph("Detalles de la Compra", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            Paragraph separator = new Paragraph("-----------------------------");
            separator.setAlignment(Element.ALIGN_CENTER);
            document.add(separator);
            document.add(new Paragraph(" "));

            try {
                String query = "SELECT C.id_compra, C.subtotal, C.fecha, C.cantidad, U.nombre_completo " +
                               "FROM Compra C " +
                               "JOIN Usuario U ON C.id_usuario = U.id_usuario " +
                               "WHERE C.id_compra = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, compraId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(10f);

                    addTableRow(table, "ID Compra:", String.valueOf(resultSet.getInt("id_compra")), fontContent);
                    addTableRow(table, "Vendedor:", resultSet.getString("nombre_completo"), fontContent);
                    addTableRow(table, "Fecha de Compra:", resultSet.getString("fecha"), fontContent);
                    addTableRow(table, "Cantidad de Tickets:", String.valueOf(resultSet.getInt("cantidad")), fontContent);
                    addTableRow(table, "Subtotal:", "$" + resultSet.getDouble("subtotal"), fontContent);

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
            System.out.println("Compra generada en: " + outputPath);

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
}