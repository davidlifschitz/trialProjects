package edu.yu.cs.com1320.project.stage2.impl;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.yu.cs.com1320.project.stage2.Document;


public class DocumentImpl implements Document {

    int hashCode;
    URI uri;
    String thisString;
    byte[] thisByteArray;

    public DocumentImpl(URI uri, String txt, int txtHash){
        this.uri = uri;
        this.thisString = txt;
        this.hashCode = txtHash;
    }
    public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes){
        this.uri = uri;
        this.thisString = txt;
        this.hashCode = txtHash;
        this.thisByteArray = pdfBytes;
    }


    /*
    public DocumentImpl(URI uri, InputStream inputStream, DocumentFormat docFormat) {
        this.uri = uri;
        this.inputStream = inputStream;
        this.docFormat = docFormat;
        this.thisByteArray = convertISToByteArray(inputStream);
        if(docFormat == DocumentFormat.TXT)
        {
            this.thisString = new String(this.thisByteArray);
        } else if(docFormat == DocumentFormat.PDF)
        {
            this.thisString = convertPDFISToString(thisByteArray);
        }
        
    }
    */

    /**
     * @return the document as a PDF
     */
    public byte[] getDocumentAsPdf()
    {
            byte[] returnByteArray = new byte[1];
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            
            PDFont font = PDType1Font.HELVETICA_BOLD;

            try
            {
                PDPageContentStream contents = new PDPageContentStream(doc, page);
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(this.thisString);
                contents.endText();
                contents.close();
                
                
                
                ByteArrayOutputStream newStream = new ByteArrayOutputStream();
                doc.save(newStream);
                returnByteArray = newStream.toByteArray();
                doc.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return returnByteArray;

    }

    /**
     * @return the document as a Plain String
     */
    public String getDocumentAsTxt()
    {
        return this.thisString;
    }

    /**
     * @return hash code of the plain text version of the document
     */
    public int getDocumentTextHashCode()
    {
        return this.thisString.hashCode();
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey()
    {
        return this.uri;
    }
}