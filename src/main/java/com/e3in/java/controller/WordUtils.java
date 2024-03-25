package com.e3in.java.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.e3in.java.model.Livre;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;

public class WordUtils {
    public XWPFDocument document;
    public FileOutputStream file;
    public String path;

    public WordUtils (String pathFile) throws FileNotFoundException {
        document = new XWPFDocument();
        path = pathFile + ".docx";
        file = new FileOutputStream(path);
    }

    public void createHeader() {
        try {
            XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);

            XWPFParagraph headerParagraph = header.createParagraph();
            XWPFRun headerRun = headerParagraph.createRun();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String date = formatter.format(LocalDateTime.now());
            String headerText = "Export - Fichier : " + path + " - " + date;
            headerRun.setText(headerText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void WordTest(List<Livre> books) throws URISyntaxException, InvalidFormatException, IOException {
        try {
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun titleRun = title.createRun();
            titleRun.setText("titre");
            titleRun.setColor("009933");
            titleRun.setBold(true);
            titleRun.setFontFamily("Courier");
            titleRun.setFontSize(20);

            XWPFParagraph subTitle = document.createParagraph();
            subTitle.setAlignment(ParagraphAlignment.CENTER);

            for(Livre book : books) {
                buildAndAddSubTitle(book.getTitre(), document);
                XWPFParagraph image = document.createParagraph();
                image.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun imageRun = image.createRun();
                imageRun.setTextPosition(20);
                URL imageUrl = new URL(book.getJaquette());
                InputStream imageStream = imageUrl.openStream();
                imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", Units.toEMU(200), Units.toEMU(200));

                buildAndAddParagraph("Auteur : " + book.getAuteur(), document);
                buildAndAddParagraph(book.getPresentation(), document);
                buildAndAddParagraph("Date de parution: " + book.getParution(), document);
                buildAndAddParagraph("Emplacement dans la bibliothéque : Colonne(" + book.getColonne()+"), Rangee("+book.getRangee()+")", document);
                buildAndAddParagraph("Le livre est " + (book.getEmprunte() ? "emprunté": "à la bibliotèque"), document);
                document.createParagraph().setPageBreak(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildAndAddParagraph(String monText, XWPFDocument document) {
        XWPFParagraph para1 = document.createParagraph();
        para1.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun para1Run = para1.createRun();
        para1Run.setText(monText);
    }

    private void buildAndAddSubTitle(String subTitle, XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun subTitleRun = paragraph.createRun();
        subTitleRun.setText(subTitle);
        subTitleRun.setColor("00CC44");
        subTitleRun.setFontFamily("Courier");
        subTitleRun.setFontSize(16);
        subTitleRun.setTextPosition(20);
        subTitleRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
    }


    public void closeDocument() {
        try {
            document.write(file);
            file.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
