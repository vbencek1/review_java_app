package org.vbencek.pdf;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.vbencek.model.Request;
import org.vbencek.properties.PropertiesLoader;

/**
 * Class that generated PDF file. 
 * Open source library that was used for generating can be found at: https://github.com/LibrePDF/OpenPDF
 * @author vbencek
 */
public class PdfGenerator {
    
  public static String CREATED_REQUEST_PDF;
  
  public PdfGenerator(){
      PropertiesLoader propLoader = new PropertiesLoader();
      CREATED_REQUEST_PDF = propLoader.getProperty("pdf.path.requestList");
            
  }
  
  private String convertToFriendlyDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date).toUpperCase();
    }
  
  public void generatePdfForRequests(List<Request> listRequest,String pdfName) {
  Document document = null;			
    try {
      System.out.println("PDF GENERATOR: staring pdf generation...");
      document = new Document();
      PdfWriter.getInstance(document,new FileOutputStream(CREATED_REQUEST_PDF+pdfName));
      Font font = new Font(Font.HELVETICA, 12, Font.BOLD);
      Table table = new Table(6); 
      table.setPadding(5);
      table.setSpacing(1);
      table.setWidth(100);
      // Setting table headers
      Cell cell = new Cell("Requests");
      cell.setHeader(true);
      cell.setVerticalAlignment(VerticalAlignment.CENTER);
      cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
      cell.setColspan(6);
      cell.setBackgroundColor(Color.LIGHT_GRAY);
      table.addCell(cell);
      
      table.addCell(new Phrase("Id", font));
      table.addCell(new Phrase("Date", font));
      table.addCell(new Phrase("User", font));          
      table.addCell(new Phrase("ISBN", font));
      table.addCell(new Phrase("Title", font));
      table.addCell(new Phrase("Description", font));
      table.endHeaders();
      // Request information to table cells
      for(Request req : listRequest) {
        table.addCell(req.getRequestId().toString());
        table.addCell(convertToFriendlyDate(req.getRequestDate()));
        table.addCell(req.getUserId().getUsername());
        table.addCell(req.getIsbn());
        table.addCell(req.getTitle());
        table.addCell(req.getDescription());
      }
      document.open();
      document.add(table);
      document.close();
    } catch (DocumentException | FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
    
}
