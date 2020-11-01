package edu.yu.cs.com1320.project.stage3.impl;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.yu.cs.com1320.project.Utils;
import edu.yu.cs.com1320.project.stage3.DocumentStore;

public class DocumentStoreImplTest {

    //variables to hold possible values for doc1
    private URI uri1;
    private String txt1;
    private byte[] pdfData1;
    private String pdfTxt1;

    //variables to hold possible values for doc2
    private URI uri2;
    private String txt2;
    private byte[] pdfData2;
    private String pdfTxt2;


    //variables to hold possible values for doc2
    private URI uri3;
    private String txt3;

    //variables to hold possible values for doc2
    private URI uri4;
    private String txt4;

    private DocumentStoreImpl createStoreAndPutOne(){
        DocumentStoreImpl dsi = new DocumentStoreImpl();
        ByteArrayInputStream bas1 = new ByteArrayInputStream(this.txt1.getBytes());
        dsi.putDocument(bas1,this.uri1, DocumentStore.DocumentFormat.TXT);
        return dsi;
    }

    private DocumentStoreImpl createStoreAndPutAll(){
        DocumentStoreImpl dsi = new DocumentStoreImpl();
        //doc1
        ByteArrayInputStream bas = new ByteArrayInputStream(this.txt1.getBytes());
        dsi.putDocument(bas,this.uri1, DocumentStore.DocumentFormat.TXT);
        //doc2
        bas = new ByteArrayInputStream(this.txt2.getBytes());
        dsi.putDocument(bas,this.uri2, DocumentStore.DocumentFormat.TXT);
        //doc3
        bas = new ByteArrayInputStream(this.txt3.getBytes());
        dsi.putDocument(bas,this.uri3, DocumentStore.DocumentFormat.TXT);
        //doc4
        bas = new ByteArrayInputStream(this.txt4.getBytes());
        dsi.putDocument(bas,this.uri4, DocumentStore.DocumentFormat.TXT);
        return dsi;
    }
    private DocumentStoreImpl createStoreAndPutMultipleDocsWithTheSameWord()
    {
        DocumentStoreImpl dsi = new DocumentStoreImpl();
        String string1 = "this is the first document";
        String string2 = "this this is the second document";
        String string3 = "this this this is the third document";
        String string4 = "this this this this is the fourth document";
        ByteArrayInputStream bas1 = new ByteArrayInputStream(string1.getBytes());
        ByteArrayInputStream bas2 = new ByteArrayInputStream(string2.getBytes());
        ByteArrayInputStream bas3 = new ByteArrayInputStream(string3.getBytes());
        ByteArrayInputStream bas4 = new ByteArrayInputStream(string4.getBytes());
        dsi.putDocument(bas1, this.uri1, DocumentStore.DocumentFormat.TXT);
        dsi.putDocument(bas2, this.uri2, DocumentStore.DocumentFormat.TXT);
        dsi.putDocument(bas3, this.uri3, DocumentStore.DocumentFormat.TXT);
        dsi.putDocument(bas4, this.uri4, DocumentStore.DocumentFormat.TXT);
        return dsi;
    }
    private DocumentStoreImpl createStoreAndMultipleWordsValuesInDifferentWordsFromOnePrefix()
    {
        DocumentStoreImpl dsi = new DocumentStoreImpl();
        String string1 = "to to too tooth toothpaste";
        String string2 = "to too too tooth toothpaste";
        String string3 = "to too tooth tooth toothpaste";
        String string4 = "to too tooth toothpaste toothpaste";
        ByteArrayInputStream bas1 = new ByteArrayInputStream(string1.getBytes());
        ByteArrayInputStream bas2 = new ByteArrayInputStream(string2.getBytes());
        ByteArrayInputStream bas3 = new ByteArrayInputStream(string3.getBytes());
        ByteArrayInputStream bas4 = new ByteArrayInputStream(string4.getBytes());
        dsi.putDocument(bas1, this.uri1, DocumentStore.DocumentFormat.TXT);
        dsi.putDocument(bas2, this.uri2, DocumentStore.DocumentFormat.TXT);
        dsi.putDocument(bas3, this.uri3, DocumentStore.DocumentFormat.TXT);
        dsi.putDocument(bas4, this.uri4, DocumentStore.DocumentFormat.TXT);
        return dsi;
    }


    @Before
    public void init() throws Exception {
        //init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        this.pdfTxt1 = "This is some PDF text for doc1, hat tip to Adobe.";
        this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String.";
        this.pdfTxt2 = "PDF content for doc2: PDF format was opened in 2008.";
        this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

        //init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "This is the text of doc3 - doc doc goose";

        //init possible values for doc4
        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "doc4: how much wood would a woodchuck chuck...";
    }

    @Test
    public void testPutPdfDocumentNoPreviousDocAtURI(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
    }

    @Test
    public void testPutTxtDocumentNoPreviousDocAtURI(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
    }

    @Test
    public void testPutDocumentWithNullArguments(){
        DocumentStore store = new DocumentStoreImpl();
        try {
            store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), null, DocumentStore.DocumentFormat.TXT);
            fail("null URI should've thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){}
        try {
            store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, null);
            fail("null format should've thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){}
    }

    @Test
    public void testPutNewVersionOfDocumentPdf(){
        //put the first version
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
        assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));

        //put the second version, testing both return value of put and see if it gets the correct text
        returned = store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue("should return hashcode of old text",this.pdfTxt1.hashCode() == returned || this.pdfTxt2.hashCode() == returned);
        assertEquals("failed to return correct pdf text", this.pdfTxt2,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
    }

    @Test
    public void testPutNewVersionOfDocumentTxt(){
        //put the first version
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
        assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));

        //put the second version, testing both return value of put and see if it gets the correct text
        returned = store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue("should return hashcode of old text",this.txt1.hashCode() == returned || this.txt2.hashCode() == returned);
        assertEquals("failed to return correct text",this.txt2,store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void testGetTxtDocAsPdf(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
        assertEquals("failed to return correct pdf text",this.txt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
    }

    @Test
    public void testGetTxtDocAsTxt(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
        assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void testGetPdfDocAsPdf(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
        assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
    }

    @Test
    public void testGetPdfDocAsTxt(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
        assertEquals("failed to return correct text",this.pdfTxt1,store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void testDeleteDoc(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.deleteDocument(this.uri1);
        assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri1));
    }

    @Test
    public void testDeleteDocReturnValue(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //should return true when deleting a document
        assertEquals("failed to return true when deleting a document",true,store.deleteDocument(this.uri1));
        //should return false if I try to delete the same doc again
        assertEquals("failed to return false when trying to delete that which was already deleted",false,store.deleteDocument(this.uri1));
        //should return false if I try to delete something that was never there to begin with
        assertEquals("failed to return false when trying to delete that which was never there to begin with",false,store.deleteDocument(this.uri2));
    }

    //NEW TESTS FOR STAGE 3
    @Test
    public void testONEDOCUMENTSearchSuccessWithPDFInCaps()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        List<String> newList = store.search("PDF");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchSuccessWithPDFInLowerCase()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        List<String> newList = store.search("pdf");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchSuccessWithTextInCaps()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfTxt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        List<String> newList = store.search("PDF");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchSuccessWithTextInLowerCase()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfTxt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        List<String> newList = store.search("pdf");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchWithNullTrie()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        List<String> newList = store.search("pdf");
        assertTrue(newList == null);
    }
    @Test
    public void testONEDOCUMENTSearchPDFSuccessWithPDFInCaps()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        List<byte[]> newList = store.searchPDFs("PDF");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchPDFSuccessWithPDFInLowerCase()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        List<byte[]> newList = store.searchPDFs("pdf");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchPDFSuccessWithTextInCaps()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfTxt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        List<byte[]> newList = store.searchPDFs("PDF");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchPDFSuccessWithTextInLowerCase()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfTxt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        List<byte[]> newList = store.searchPDFs("pdf");
        assertTrue(newList != null);
    }
    @Test
    public void testONEDOCUMENTSearchPDFWithNullTrie()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        List<byte[]> newList = store.searchPDFs("pdf");
        assertTrue(newList == null);
    }
    @Test
    public void testFOURTXTDOCUMENTSSearchSearchSuccessWithTHISInCaps()
    {
        DocumentStoreImpl store = this.createStoreAndPutMultipleDocsWithTheSameWord();
        List<String> newList = store.search("this");
        assertTrue(newList != null);
    }
    @Test
    public void testFOURDOCUMENTSSearchPDFSuccessWithPDFInCaps()
    {
        DocumentStoreImpl store = this.createStoreAndPutMultipleDocsWithTheSameWord();
        List<byte[]> newList = store.searchPDFs("this");
        assertTrue(newList != null);
    }

    @Test
    public void testFOURDOCUMENTSSearchByPrefixSuccess()
    {
        DocumentStoreImpl store = this.createStoreAndMultipleWordsValuesInDifferentWordsFromOnePrefix();
        List<String> newList = store.searchByPrefix("to");
        assertTrue(newList.size() == 4);
    }
    @Test
    public void testFOURDOCUMENTSSearchByPrefixFailure()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        List<String> newList = store.searchByPrefix("to");
        assertTrue(newList.size() == 0);
    }
    @Test
    public void testFOURDOCUMENTSSearchPDFByPrefixSuccess()
    {
        DocumentStoreImpl store = this.createStoreAndMultipleWordsValuesInDifferentWordsFromOnePrefix();
        List<byte[]> newList = store.searchPDFsByPrefix("to");
        assertTrue(newList.size() == 4);
    }
    @Test
    public void testFOURDOCUMENTSSearchPDFByPrefixFailure()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        List<byte[]> newList = store.searchPDFsByPrefix("to");
        assertTrue(newList.size() == 0);
    }

    @Test
    public void testFOURDOCUMENTSDeleteAllSuccess()
    {
        DocumentStoreImpl store = this.createStoreAndPutMultipleDocsWithTheSameWord();
        Set<URI> docURIs = store.deleteAll("this");
        assertTrue(docURIs.size() == 4);
    }
    @Test
    public void testFOURDOCUMENTSDeleteAllFailure()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        Set<URI> docURIs = store.deleteAll("this");
        assertTrue(docURIs.size() == 0);
    }
    @Test
    public void testFOURDOCUMENTSDeleteAllWithPrefixSuccess()
    {
        DocumentStoreImpl store = this.createStoreAndMultipleWordsValuesInDifferentWordsFromOnePrefix();
        Set<URI> docURIs = store.deleteAllWithPrefix("to");
        assertTrue(docURIs.size() == 4);
    }
    @Test
    public void testFOURDOCUMENTSDeleteAllWithPrefixFailure()
    {
        DocumentStoreImpl store = new DocumentStoreImpl();
        Set<URI> docURIs = store.deleteAllWithPrefix("to");
        assertTrue(docURIs.size() == 0);
    }
}