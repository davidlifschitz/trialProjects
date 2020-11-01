package edu.yu.cs.com1320.project.stage4.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.yu.cs.com1320.project.Utils;


public class MyDocumentImplTest {
    private URI textUri;
    private String textString;
    private int textHashCode;

    private URI pdfUri;
    private String pdfString;
    private int pdfHashCode;
    private byte[] pdfData;

    @Before
    public void setUp() throws Exception {
        this.textUri = new URI("http://edu.yu.cs/com1320/txt");
        this.textString = "This is text content. Lots of it. This is is is.";
        this.textHashCode = this.textString.hashCode();

        this.pdfUri = new URI("http://edu.yu.cs/com1320/pdf");
        this.pdfString = "This is a PDF, brought to you by Adobe.";
        this.pdfHashCode = this.pdfString.hashCode();
        this.pdfData = Utils.textToPdfData(this.pdfString);
    }

    @Test
    public void testGetTextDocumentAsTxt() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
        assertEquals(this.textString, textDocument.getDocumentAsTxt());
    }

    @Test
    public void testGetPdfDocumentAsTxt() {
        DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
        assertEquals(this.pdfString, pdfDocument.getDocumentAsTxt());
    }

    @Test
    public void testGetTextDocumentAsPdf() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
        byte[] pdfBytes = textDocument.getDocumentAsPdf();
        String textAsPdfString = Utils.pdfDataToText(pdfBytes);
        assertEquals(this.textString, textAsPdfString);
    }

    @Test
    public void testGetPdfDocumentAsPdf() {
        DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
        byte[] pdfBytes = pdfDocument.getDocumentAsPdf();
        String pdfAsPdfString = Utils.pdfDataToText(pdfBytes);
        assertEquals(this.pdfString, pdfAsPdfString);
    }

    @Test
    public void testGetTextDocumentTextHashCode() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
        assertEquals(this.textHashCode, textDocument.getDocumentTextHashCode());
    }

    @Test
    public void testGetPdfDocumentTextHashCode() {
        DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
        assertEquals(this.pdfHashCode, pdfDocument.getDocumentTextHashCode());
        assertNotEquals(this.pdfHashCode, 25);
    }

    @Test
    public void testGetTextDocumentKey() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
        assertEquals(this.textUri, textDocument.getKey());
        URI fakeUri = null;
        try {
            fakeUri = new URI("http://wrong.com");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assertNotEquals(this.textUri, fakeUri);
    }

    @Test
    public void testGetPdfDocumentKey() {
        DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
        assertEquals(this.pdfUri, pdfDocument.getKey());
        URI fakeUri = null;
        try {
            fakeUri = new URI("http://wrong.com");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assertNotEquals(this.pdfUri, fakeUri);
    }


    //NEW TESTS FOR STAGE 3
    @Test 
    public void testWordCountSuccess()
    {
        DocumentImpl dI = new DocumentImpl(textUri, textString, textHashCode);
        int timesTheWordThisIsInTheString = dI.wordCount("this");
        assertTrue(timesTheWordThisIsInTheString == 2);
        int timesTheWordIsIsInTheString = dI.wordCount("is");
        assertTrue(timesTheWordIsIsInTheString == 4);
    }
    @Test 
    public void testWordCountFailure()
    {
        DocumentImpl dI = new DocumentImpl(textUri, textString, textHashCode);
        int timesTheWordNOTAWORDIsInTheString = dI.wordCount("NOTAWORD");
        assertTrue(timesTheWordNOTAWORDIsInTheString == 0);
        
    }
}