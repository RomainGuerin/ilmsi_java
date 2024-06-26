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
import java.util.logging.Logger;

import static com.e3in.java.utils.Common.getCurrentDateTime;

/**
 * La classe WordController permet de créer et manipuler un document Word.
 * Elle permet notamment d'ajouter des en-têtes, pieds de page, table des matières, page de couverture, et de lister des livres.
 */
public class WordController {
    private final XWPFDocument document;
    private final String path;

    static Logger logger = Logger.getLogger(WordController.class.getName());

    /**
     * Constructeur de la classe WordController.
     *
     * @param path Le chemin où le document sera sauvegardé.
     */
    public WordController(String path) {
        this.document = new XWPFDocument();
        this.path = path;
        initializeDocument();
    }

    // Initialisation du document Word
    private void initializeDocument() {
        addHeader();
        addFooter();
        addCoverPage();
        addTableOfContent();
    }

    /**
     * Ajoute un en-tête au document avec des informations sur l'exportation de la bibliothèque.
     */
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
            logger.severe("Erreur lors de l'ajout de l'entête du document Word. " + e.getMessage());
        }
    }

    /**
     * Ajoute un pied de page au document avec des informations sur les auteurs et la pagination.
     */
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
            logger.severe("Erreur lors de l'ajout du pied de page du document Word. " + e.getMessage());
        }
    }

    /**
     * Ajoute une table des matières au document.
     */
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

    /**
     * Ajoute une page de couverture au document.
     */
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
            logger.severe("Erreur lors de l'ajout de la page de couverture du document Word. " + e.getMessage());
        }
    }

    /**
     * Ajoute une liste de livres au document.
     *
     * @param books La liste des livres à ajouter.
     */
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
            addParagraph("Le livre a été écrit par " + book.getAuteur()+".");
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

    /**
     * Ajoute une liste des livres empruntés au document.
     *
     * @param books La liste des livres à vérifier pour emprunt.
     */
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

    /**
     * Ajoute un titre au document.
     *
     * @param text Le texte du titre.
     */
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

    /**
     * Ajoute un sous-titre au document.
     *
     * @param text Le texte du sous-titre.
     */
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

    /**
     * Ajoute une image au document.
     *
     * @param path Le chemin ou l'URL de l'image.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     * @throws URISyntaxException Si l'URI est incorrect.
     * @throws InvalidFormatException Si le format de l'image est invalide.
     */
    public void addImage(String path) throws IOException, URISyntaxException, InvalidFormatException {
        XWPFParagraph image = document.createParagraph();
        image.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun imageRun = image.createRun();
        imageRun.setTextPosition(20);
        InputStream imageStream = path.startsWith("http") ? new URI(path).toURL().openStream() : new FileInputStream(path);
        imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", Units.toEMU(175), Units.toEMU(200));
    }

    /**
     * Ajoute un paragraphe au document.
     *
     * @param text Le texte du paragraphe.
     */
    public void addParagraph(String text){
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        run.setText(text);
    }

    /**
     * Vérifie si la liste des livres est vide et ajoute un message approprié.
     *
     * @param books La liste des livres à vérifier.
     */
    private void isDataEmpty(List<Livre> books) {
        if (books.isEmpty()) {
            XWPFParagraph empty = document.createParagraph();
            XWPFRun emptyRun = empty.createRun();
            emptyRun.setText("Aucun livre n'a été trouvé dans la bibliothèque.");
        }
    }

    /**
     * Sauvegarde le document à l'emplacement spécifié.
     */
    public void saveDocument() {
        try {
            FileOutputStream file = new FileOutputStream(path);
            document.write(file);
            file.close();
            document.close();
        } catch (IOException e) {
            logger.severe("Erreur lors de la sauvegarde du document Word. " + e.getMessage());
        }
    }

    /**
     * Ajoute un style de titre personnalisé au document.
     *
     * @param document Le document à modifier.
     * @param strStyleId L'ID du style.
     * @param headingLevel Le niveau de titre.
     */
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
