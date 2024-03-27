package com.e3in.java.utils;

import com.e3in.java.model.Livre;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Word{
    public XWPFDocument document;
    public FileOutputStream file;
    public String path;

    public Word(String pathFile) throws FileNotFoundException {
        document = new XWPFDocument();
        path = pathFile;
        file = new FileOutputStream(path);
    }

    public void buildAndAddHeader(XWPFDocument document) {
        try {
            XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);

            XWPFParagraph headerParagraph = header.createParagraph();
            XWPFRun headerRun = headerParagraph.createRun();
            String date = getCurrentDateTime();
            String pathName = path.substring(path.lastIndexOf("\\") + 1);
            String headerText = "Export Bibliothèque - Fichier : " + pathName + " - " + date;
            headerRun.setText(headerText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFooter() {
        try {
            // Création du pied de page pour le document
            XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);

            // Paragraphe pour les informations sur les auteurs
            XWPFParagraph footerParagraph = footer.createParagraph();
            XWPFRun footerRun = footerParagraph.createRun();
            footerRun.setText("Réalisé par Romain GUERIN, Nicolas DROESCH");

            // Paragraphe pour le numéro de page
            XWPFParagraph pageNumberParagraph = footer.createParagraph();
            pageNumberParagraph.setAlignment(ParagraphAlignment.RIGHT);

            // Ajout du texte "Page"
            XWPFRun pageNumberRun = pageNumberParagraph.createRun();
            pageNumberRun.setText("Page ");
            pageNumberRun.setFontSize(12);

            // Ajout du champ pour le numéro de page
            pageNumberParagraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");

            // Ajout du texte " sur "
            XWPFRun pageNumberSeparatorRun = pageNumberParagraph.createRun();
            pageNumberSeparatorRun.setText(" sur ");
            pageNumberSeparatorRun.setFontSize(12);

            // Ajout du champ pour le nombre total de pages
            pageNumberParagraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createCoverPage() {
        try {
            for (int i = 0; i < 7; i++) {
                document.createParagraph().createRun().addBreak();
            }

            // Titre centré sur la page
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            title.setVerticalAlignment(TextAlignment.CENTER);

            XWPFRun titleRun = title.createRun();
            titleRun.setText("Rapport de la bibliothèque");
            titleRun.setColor("000000");
            titleRun.setBold(true);
            titleRun.setFontFamily("Courier");
            titleRun.setFontSize(20);

            // Sous-titre centré sur la page
            XWPFParagraph subTitle = document.createParagraph();
            subTitle.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subTitleRun = subTitle.createRun();
            subTitleRun.setText("Rapport généré le " + getCurrentDateTime());
            subTitleRun.setColor("262626");
            subTitleRun.setFontFamily("Courier");
            subTitleRun.setFontSize(12);
            subTitleRun.setTextPosition(20);
            document.createParagraph().setPageBreak(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return formatter.format(LocalDateTime.now());
    }

    public void wordContent(List<Livre> books) throws URISyntaxException, InvalidFormatException, IOException {
        try {
            CTSdtBlock block = document.getDocument().getBody().addNewSdt();
            document.createParagraph().setPageBreak(true);

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun titleRun = title.createRun();
            titleRun.setText("Liste des livres de la bibliothèque");
            titleRun.setColor("000000");
            titleRun.setBold(true);
            titleRun.setFontFamily("Courier");
            titleRun.setFontSize(20);

            for(int i = 0; i < books.size(); i++) {
                Livre book = books.get(i);
//                XWPFParagraph subTitle = document.createParagraph();
//                subTitle.setAlignment(ParagraphAlignment.CENTER);
                buildAndAddSubTitle(book.getTitre(), document);

                XWPFParagraph image = document.createParagraph();
                image.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun imageRun = image.createRun();
                imageRun.setTextPosition(20);
                URI imageUrl = new URI(book.getJaquette());
                InputStream imageStream = imageUrl.toURL().openStream();
                imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", Units.toEMU(200), Units.toEMU(200));

                buildAndAddParagraph("Auteur : " + book.getAuteur(), document);
                buildAndAddParagraph(book.getPresentation(), document);
                buildAndAddParagraph("Date de parution : " + book.getParution(), document);
                buildAndAddParagraph("Emplacement dans la bibliothèque : Colonne (" + book.getColonne()+"), Rangée ("+book.getRangee()+")", document);
                buildAndAddParagraph("Le livre est " + (book.getEmprunte() ? "emprunté": "à la bibliothèque"), document);
                if (books.indexOf(book) != books.size()-1) {
                    document.createParagraph().setPageBreak(true);
                }
            }

            // document.enforceUpdateFields();

            document.createParagraph().setPageBreak(true);

            // Title Tableau des livres de la bibliothèque empruntés
            XWPFParagraph titleEmprunt = document.createParagraph();
            titleEmprunt.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun titleRunEmprunt = titleEmprunt.createRun();
            titleRunEmprunt.setText("Les livres de la bibliothèque empruntés");
            titleRunEmprunt.setColor("000000");
            titleRunEmprunt.setBold(true);
            titleRunEmprunt.setFontFamily("Courier");
            titleRunEmprunt.setFontSize(20);

            XWPFParagraph subTitleEmprunt = document.createParagraph();
            subTitleEmprunt.setAlignment(ParagraphAlignment.CENTER);

            for (Livre book : books) {
                if (book.getEmprunte()) {
                    String livreEmprunt = "- " + book.getTitre() + ", " + book.getAuteur().toString() + " - " + String.valueOf(book.getParution());
                    buildAndAddParagraph(livreEmprunt, document);
                }
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
//        subTitleRun.setStyle(subTitle);
        subTitleRun.setStyle("heading 1");
        subTitleRun.setColor("262626");
        subTitleRun.setFontFamily("Courier");
        subTitleRun.setFontSize(16);
        subTitleRun.setTextPosition(20);
        subTitleRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
        CTP ctp = paragraph.getCTP();
        CTSimpleField toc = ctp.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff.Factory.newInstance());
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
