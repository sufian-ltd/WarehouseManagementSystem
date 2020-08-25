package com.warehouse.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.warehouse.constant.WarehouseConstant;
import com.warehouse.entity.ProductRequest;

@Component
public class PDFGenerator {

	private String reportFileName;
	private String reportFileNameDateFormat="dd_MMMM_yyyy";
	private String localDateFormat="dd MMMM yyyy HH:mm:ss";

	private int noOfColumns;
	private List<String> columnNames;
	private String key;
	private List<ProductRequest> productRequests;

	private static Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
	private static Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
	private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

	private Logger log = LoggerFactory.getLogger(PDFGenerator.class);
	
	public void setPdfData(String reportFileName, int noOfColumns, List<String> columnNames, String key,
			List<ProductRequest> productRequests) {
		this.reportFileName = reportFileName;
		this.noOfColumns = noOfColumns;
		this.columnNames = columnNames;
		this.key = key;
		this.productRequests = productRequests;
	}

	public void generatePdfReport() {

		Document document = new Document();

		try {
			PdfWriter.getInstance(document, new FileOutputStream(getPdfNameWithDate()));
			document.open();
			addDocTitle(document);
			createTable(document);
			addFooter(document);
			document.close();
			System.out.println("------------------Your PDF Report is ready!-------------------------");

		} catch (FileNotFoundException | DocumentException e) {
			log.info("pdf generating failed : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void addDocTitle(Document document) throws DocumentException {
		String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(localDateFormat));
		Paragraph p1 = new Paragraph();
		leaveEmptyLine(p1, 1);
		p1.add(new Paragraph(reportFileName, COURIER));
		p1.setAlignment(Element.ALIGN_CENTER);
		leaveEmptyLine(p1, 1);
		p1.add(new Paragraph("Report generated on " + localDateString, COURIER_SMALL));

		document.add(p1);

	}

	private void createTable(Document document) throws DocumentException {
		Paragraph paragraph = new Paragraph();
		leaveEmptyLine(paragraph, 3);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(noOfColumns);

		for (int i = 0; i < noOfColumns; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.CYAN);
			table.addCell(cell);
		}

		table.setHeaderRows(1);
		if (key.equals(WarehouseConstant.OWNER_TRANSACTION_REPORT)) {
			getOwnerTransactionDbData(table);
		}
		document.add(table);
	}

	private void getOwnerTransactionDbData(PdfPTable table) {

		for (ProductRequest productRequest : productRequests) {

			table.setWidthPercentage(100);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

			table.addCell(productRequest.getProduct().getStorage().getUser().getEmail());
			table.addCell(productRequest.getProduct().getStorage().getId().toString());
			table.addCell(productRequest.getProduct().getStorage().getName());
			table.addCell(productRequest.getProduct().getId().toString());
			table.addCell(productRequest.getProduct().getName());
			table.addCell(productRequest.getProduct().getPrice().toString() + " TK");
			table.addCell(productRequest.getUser().getEmail());
			table.addCell(productRequest.getCapacity().toString() + " Ton");
			table.addCell(productRequest.getDuration().toString() + " Days");
			table.addCell(productRequest.getDate().toString());
		}

	}

	private void addFooter(Document document) throws DocumentException {
		Paragraph p2 = new Paragraph();
		leaveEmptyLine(p2, 3);
		p2.setAlignment(Element.ALIGN_MIDDLE);
		p2.add(new Paragraph("------------End Of " + reportFileName + "--------------",
				COURIER_SMALL_FOOTER));

		document.add(p2);
	}

	private static void leaveEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private String getPdfNameWithDate() {
		String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat));
		return reportFileName + "-" + localDateString + ".pdf";
	}

}
