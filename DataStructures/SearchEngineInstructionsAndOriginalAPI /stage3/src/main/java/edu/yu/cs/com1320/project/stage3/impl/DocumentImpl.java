package edu.yu.cs.com1320.project.stage3.impl;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.net.URI;

import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.yu.cs.com1320.project.stage3.Document;

public class DocumentImpl implements Document {

    int hashCode;
    URI uri;
    String wholeText;
    byte[] thisByteArray;
    HashMap mappedWordToCount;
    

    public DocumentImpl(URI uri, String txt, int txtHash) {
        this.uri = uri;
        this.wholeText = txt;
        this.hashCode = txtHash;
        this.mappedWordToCount = new HashMap<String,Integer>();
        addAllWordsToMap();
    }

    public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes) {
        this.uri = uri;
        this.wholeText = txt;
        this.hashCode = txtHash;
        this.thisByteArray = pdfBytes;
        this.mappedWordToCount = new HashMap<String,Integer>();
        addAllWordsToMap();
    }

    /**
     * @return the document as a PDF
     */
    public byte[] getDocumentAsPdf() {
        byte[] returnByteArray = new byte[1];
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);

        PDFont font = PDType1Font.HELVETICA_BOLD;

        try {
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.showText(this.wholeText);
            contents.endText();
            contents.close();
            ByteArrayOutputStream newStream = new ByteArrayOutputStream();
            doc.save(newStream);
            returnByteArray = newStream.toByteArray();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnByteArray;

    }

    /**
     * @return the document as a Plain String
     */
    public String getDocumentAsTxt() {
        return this.wholeText;
    }

    /**
     * @return hash code of the plain text version of the document
     */
    public int getDocumentTextHashCode() {
        return this.wholeText.hashCode();
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey() {
        return this.uri;
    }

    private void addAllWordsToMap()
    {
        String allCaps = this.wholeText.toUpperCase();
        String[] alltheWords = allCaps.split("\\W+");
        for (String string : alltheWords) {

            if(!(this.mappedWordToCount.containsKey(string)))
            {
                mappedWordToCount.put(string, 1);
            } else 
            {
                mappedWordToCount.put(string, ((int)(Integer)(mappedWordToCount.get(string)) + 1));
            }
        }
    }
    /**
     * how many times does the given word appear in the document?
     * 
     * @param word
     * @return the number of times the given words appears in the document
     */
    public int wordCount(String word) {
        int wordCount = 0;
        String allCaps = word.toUpperCase();
        HashMap nm = this.mappedWordToCount;
        try {
            wordCount = (int)(Integer)nm.get(allCaps);    
        } catch (Exception e) {
            return wordCount;
        }
        
        return wordCount;
    }

  

}