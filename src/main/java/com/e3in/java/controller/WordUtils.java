package com.e3in.java.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public void WordTest() throws URISyntaxException, InvalidFormatException, IOException {
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

            XWPFRun subTitleRun = subTitle.createRun();
            subTitleRun.setText("sous-titre");
            subTitleRun.setColor("00CC44");
            subTitleRun.setFontFamily("Courier");
            subTitleRun.setFontSize(16);
            subTitleRun.setTextPosition(20);
            subTitleRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);

            XWPFParagraph image = document.createParagraph();
            image.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun imageRun = image.createRun();
            imageRun.setTextPosition(20);
            URL imageUrl = new URL("https://products-images.di-static.com/image/christopher-paolini-eragon-tome-1-eragon/9791036313691-475x500-1.jpg");
            InputStream imageStream = imageUrl.openStream();
            imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", Units.toEMU(200), Units.toEMU(200));

            XWPFParagraph para1 = document.createParagraph();
            para1.setAlignment(ParagraphAlignment.BOTH);
            String string1 = "paragraph 1";
            XWPFRun para1Run = para1.createRun();
            para1Run.setText(string1);

            XWPFParagraph para2 = document.createParagraph();
            para2.setAlignment(ParagraphAlignment.RIGHT);
            String string2 = "paragraph2";
            XWPFRun para2Run = para2.createRun();
            para2Run.setText(string2);
            para2Run.setItalic(true);

            XWPFParagraph para3 = document.createParagraph();
            para3.setAlignment(ParagraphAlignment.LEFT);
            String string3 = "paragraph3";
            XWPFRun para3Run = para3.createRun();
            para3Run.setText(string3);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
