package com.e3in.java.controller;

import com.e3in.java.model.Livre;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.e3in.java.utils.Common.getCurrentDateTime;

public class WordController {
    private final XWPFDocument document;
    private final String path;

    public WordController(String path) {
        this.document = new XWPFDocument();
        this.path = path;
    }

    public void addHeader() {
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
    public void addFooter() {
        try {
            XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);

            XWPFParagraph footerParagraph = footer.createParagraph();
            XWPFRun footerRun = footerParagraph.createRun();
            footerRun.setText("Réalisé par Romain GUERIN et Nicolas DROESCH, avec la collaboration de Theo GINFRAY, Yoan RAZAFIMAHATRATRA");

            XWPFParagraph pageNumberParagraph = footer.createParagraph();
            pageNumberParagraph.setAlignment(ParagraphAlignment.RIGHT);

            XWPFRun pageNumberRun = pageNumberParagraph.createRun();
            pageNumberRun.setText("Page ");
            pageNumberRun.setFontSize(12);

            pageNumberParagraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");

            XWPFRun pageNumberSeparatorRun = pageNumberParagraph.createRun();
            pageNumberSeparatorRun.setText(" sur ");
            pageNumberSeparatorRun.setFontSize(12);

            pageNumberParagraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTableOfContent() {
        document.createTOC();
        addCustomHeadingStyle(this.document, "heading 1", 1);
        addCustomHeadingStyle(this.document, "heading 2", 2);

        XWPFParagraph paragraph = document.createParagraph();

        CTP ctP = paragraph.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\o \"1-3\" \\z \\u \\h");
        toc.setDirty(STOnOff1.ON);
        document.createParagraph().setPageBreak(true);
    }

    public void addCoverPage() {
        try {
            for (int i = 0; i < 5; i++) {
                document.createParagraph().createRun().addBreak();
            }

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            title.setVerticalAlignment(TextAlignment.CENTER);

            XWPFRun titleRun = title.createRun();
            titleRun.setText("Rapport de la bibliothèque");
            titleRun.setColor("000000");
            titleRun.setBold(true);
            titleRun.setFontFamily("Courier");
            titleRun.setFontSize(20);

            XWPFParagraph subTitle = document.createParagraph();
            subTitle.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subTitleRun = subTitle.createRun();
            subTitleRun.setText("Rapport généré le " + getCurrentDateTime().replace(" ", " à "));
            subTitleRun.setColor("262626");
            subTitleRun.setFontFamily("Courier");
            subTitleRun.setFontSize(12);
            subTitleRun.setTextPosition(20);
            
            addImage("https://images.emojiterra.com/google/noto-emoji/unicode-15/bw/512px/1f4d6.png");

            document.createParagraph().setPageBreak(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBooks(List<Livre> books) {
        addTitle("Liste des livres de la bibliothèque");

        for(int i = 0; i < books.size(); i++) {
            Livre book = books.get(i);
            addSubtitle(book.getTitre());
            try {
                addImage(book.getJaquette());
            } catch (Exception e) {
                addParagraph("Le lien de la jaquette : " + book.getJaquette());
            }
            addParagraph("Le livre a été écrit par  " + book.getAuteur()+".");
            addParagraph(book.getPresentation());
            addParagraph("Le livre est paru le " + book.getParution()+".");
            addParagraph("Le livre est placé dans la colonne numéro "+book.getColonne()+" de la rangée "+book.getRangee()+".");
            addParagraph("Le livre est " + (book.getEmprunte() ? "emprunté": "à la bibliothèque") + ".");

            if (books.indexOf(book) != books.size()-1) {
                document.createParagraph().setPageBreak(true);
            }
        }
        isDataEmpty(books);
    }

    public void addBorrowedBooks(List<Livre> books) {
        document.createParagraph().setPageBreak(true);
        addTitle("Livres empruntés");
        addParagraph("Les livres actuellement emprunté sont :");
        for(Livre book : books) {
            if(!book.getEmprunte())
                continue;
            addParagraph("- " + book.getTitre()+" écrit par "+book.getAuteur()+" et paru le "+book.getParution()+".");
        }
        isDataEmpty(books);
    }

    public void addTitle(String text) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        title.setStyle("heading 1");

        XWPFRun titleRun = title.createRun();
        titleRun.setText(text);
        titleRun.setColor("000000");
        titleRun.setBold(true);
        titleRun.setFontFamily("Courier");
        titleRun.setFontSize(20);
    }
    public void addSubtitle(String text){
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setColor("262626");
        run.setFontFamily("Courier");
        run.setFontSize(16);
        run.setTextPosition(20);
        run.setUnderline(UnderlinePatterns.SINGLE);
        run.setBold(true);
        paragraph.setStyle("heading 2");
    }
    public void addImage(String path) throws IOException, URISyntaxException, InvalidFormatException {
        XWPFParagraph image = document.createParagraph();
        image.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun imageRun = image.createRun();
        imageRun.setTextPosition(20);
        InputStream imageStream = path.startsWith("http") ? new URI(path).toURL().openStream() : new FileInputStream(path);
        imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", Units.toEMU(175), Units.toEMU(200));
    }
    public void addParagraph(String text){
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        run.setText(text);
    }
    private void isDataEmpty(List<Livre> books) {
        if (books.isEmpty()) {
            XWPFParagraph empty = document.createParagraph();
            XWPFRun emptyRun = empty.createRun();
            emptyRun.setText("Aucun livre n'a été trouvé dans la bibliothèque.");
        }
    }

    public void saveDocument() {
        try {
            FileOutputStream file = new FileOutputStream(path);
            document.write(file);
            file.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void addCustomHeadingStyle(XWPFDocument document, String strStyleId, int headingLevel) {
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        ctStyle.setQFormat(onoffnull);

        CTPPrGeneral ppr = CTPPrGeneral.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        XWPFStyles styles = document.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
    }
}
