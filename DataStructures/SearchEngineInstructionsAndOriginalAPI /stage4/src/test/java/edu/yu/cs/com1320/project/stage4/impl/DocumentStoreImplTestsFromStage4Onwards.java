package edu.yu.cs.com1320.project.stage4.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import edu.yu.cs.com1320.project.Utils;
import edu.yu.cs.com1320.project.stage4.DocumentStore;


public class DocumentStoreImplTestsFromStage4Onwards {
    private String txt1;
    private String txt2;
    private String txt3;
    private String txt4;
    private String txt5;
    private String txt6;
    private String txt7;
    private String txt8;
    private String txt9;
    private String pdfTxt1;
    private String pdfTxt2;
    private String pdfTxt3;
    private String pdfTxt4;
    private String pdfTxt5;
    private String pdfTxt6;
    private String pdfTxt7;
    private String pdfTxt8;
    private String pdfTxt9;
    private byte[] pdfData1;
    private byte[] pdfData2;
    private byte[] pdfData3;
    private byte[] pdfData4;
    private byte[] pdfData5;
    private byte[] pdfData6;
    private byte[] pdfData7;
    private byte[] pdfData8;
    private byte[] pdfData9;
    private URI uri1;
    private URI uri2;
    private URI uri3;
    private URI uri4;
    private URI uri5;
    private URI uri6;
    private URI uri7;
    private URI uri8;
    private URI uri9;
    
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
    private DocumentStoreImpl AddDocumentsTXT23AND4(DocumentStoreImpl dsi){
        //doc1
        //ByteArrayInputStream bas = new ByteArrayInputStream(this.txt1.getBytes());
        //dsi.putDocument(bas,this.uri1, DocumentStore.DocumentFormat.TXT);
        //doc2
        ByteArrayInputStream bas = new ByteArrayInputStream(this.txt2.getBytes());
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
    private DocumentStoreImpl createStoreAndPutOne(){
        DocumentStoreImpl dsi = new DocumentStoreImpl();
        ByteArrayInputStream bas1 = new ByteArrayInputStream(this.txt1.getBytes());
        dsi.putDocument(bas1,this.uri1, DocumentStore.DocumentFormat.TXT);
        return dsi;
    }
    
    private DocumentStoreImpl createStoreAndPutAllWithPDFText(){
        final DocumentStoreImpl dsi = new DocumentStoreImpl();
        // doc1
        ByteArrayInputStream bas = new ByteArrayInputStream(this.txt1.getBytes());
        dsi.putDocument(bas, this.uri1, DocumentStore.DocumentFormat.TXT);
        // doc2
        bas = new ByteArrayInputStream(this.txt2.getBytes());
        dsi.putDocument(bas, this.uri2, DocumentStore.DocumentFormat.TXT);
        // doc3
        bas = new ByteArrayInputStream(this.txt3.getBytes());
        dsi.putDocument(bas, this.uri3, DocumentStore.DocumentFormat.TXT);
        // doc4
        bas = new ByteArrayInputStream(this.txt4.getBytes());
        dsi.putDocument(bas, this.uri4, DocumentStore.DocumentFormat.TXT);
        // doc5
        bas = new ByteArrayInputStream(this.pdfData5);
        dsi.putDocument(bas, this.uri5, DocumentStore.DocumentFormat.PDF);
        // doc6
        bas = new ByteArrayInputStream(this.pdfData6);
        dsi.putDocument(bas, this.uri6, DocumentStore.DocumentFormat.PDF);
        // doc7
        bas = new ByteArrayInputStream(this.pdfData7);
        dsi.putDocument(bas, this.uri7, DocumentStore.DocumentFormat.PDF);
        // doc8
        bas = new ByteArrayInputStream(this.pdfData8);
        dsi.putDocument(bas, this.uri8, DocumentStore.DocumentFormat.PDF);
        // doc9
        bas = new ByteArrayInputStream(this.pdfData9);
        dsi.putDocument(bas, this.uri9, DocumentStore.DocumentFormat.PDF);
        return dsi;
    }

    @Before
    public void init() throws Exception {
        // init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "Text doc1";
        this.pdfTxt1 = "pdf doc1";
        this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

        // init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text doc2";
        this.pdfTxt2 = "PDF doc2";
        this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

        // init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "Text doc3";
        this.pdfTxt3 = "PDF doc3";
        this.pdfData3 = Utils.textToPdfData(this.pdfTxt3);

        // init possible values for doc4
        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "Text doc4";
        this.pdfTxt4 = "PDF doc4";
        this.pdfData4 = Utils.textToPdfData(this.pdfTxt4);
        
        // init possible values for doc5
        this.uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        this.txt5 = "Text doc5";
        this.pdfTxt5 = "PDF doc5";
        this.pdfData5 = Utils.textToPdfData(this.pdfTxt5);
        
        // init possible values for doc6
        this.uri6 = new URI("http://edu.yu.cs/com1320/project/doc6");
        this.txt6 = "Text doc6";
        this.pdfTxt6 = "PDF doc6";
        this.pdfData6 = Utils.textToPdfData(this.pdfTxt6);
        
        // init possible values for doc7
        this.uri7 = new URI("http://edu.yu.cs/com1320/project/doc7");
        this.txt7 = "Text doc7";
        this.pdfTxt7 = "PDF doc7";
        this.pdfData7 = Utils.textToPdfData(this.pdfTxt7);
        
        // init possible values for doc8
        this.uri8 = new URI("http://edu.yu.cs/com1320/project/doc8");
        this.txt8 = "Text doc8";
        this.pdfTxt8 = "PDF doc8";
        this.pdfData8 = Utils.textToPdfData(this.pdfTxt8);
        
        // init possible values for doc9
        this.uri9 = new URI("http://edu.yu.cs/com1320/project/doc9");
        this.txt9 = "Text doc9";
        this.pdfTxt9 = "PDF doc9";
        this.pdfData9 = Utils.textToPdfData(this.pdfTxt9);
    }

    // NEW TESTS FOR STAGE 4!!!
    // Tests when you don't change the timing -- just to make sure setMaxDocBytes
    // and setMaxDocs work by themselves
    @Test
    public void TestforMaxDocumentBytesSuccess() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentBytes(10);
        assertTrue(dsi.documentToBytes.size() == 0);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.stackImpl.peek() == null);
    }

    @Test
    public void TestforMaxDocumentBytesSuccess2() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentBytes(180);
        assertTrue(dsi.documentToBytes.size() == 1);
        assertTrue(dsi.hashTableImpl.get(this.uri1) != null);
        assertTrue(dsi.stackImpl.peek() != null);
    }

    @Test
    public void TestforMaxDocumentBytesSuccess3() {
        final DocumentStoreImpl dsi = this.createStoreAndPutAll();
        dsi.setMaxDocumentBytes(65);
        assertEquals(3,dsi.documentToBytes.size());
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.stackImpl.peek() != null);
    }

    @Test
    public void TestforMaxDocumentBytesSuccess4() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentBytes(65);
        this.AddDocumentsTXT23AND4(dsi);
        assertTrue(dsi.documentToBytes.size() == 3);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.stackImpl.peek() != null);
    }

    @Test
    public void TestforMaxDocumentBytesSuccess5() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentBytes(37);
        this.AddDocumentsTXT23AND4(dsi);
        assertTrue(dsi.documentToBytes.size() == 2);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.stackImpl.peek() != null);
    }

    @Test
    public void TestforMaxDocumentsSuccess() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentCount(0);
        assertTrue(dsi.documentToBytes.size() == 0);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.stackImpl.peek() == null);
    }

    @Test
    public void TestforMaxDocumentsSuccess2() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentCount(1);
        assertTrue(dsi.documentToBytes.size() == 1);
        assertTrue(dsi.hashTableImpl.get(this.uri1) != null);
        assertTrue(dsi.stackImpl.peek() != null);
    }

    @Test
    public void TestforMaxDocumentsSuccess3() {
        final DocumentStoreImpl dsi = this.createStoreAndPutAll();
        dsi.setMaxDocumentCount(3);
        assertTrue(dsi.documentToBytes.size() == 3);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.hashTableImpl.get(this.uri2) != null);
        assertTrue(dsi.hashTableImpl.get(this.uri3) != null);
        assertTrue(dsi.hashTableImpl.get(this.uri4) != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() == null);
    }

    @Test
    public void TestforMaxDocumentsSuccess4() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentCount(3);
        this.AddDocumentsTXT23AND4(dsi);
        assertTrue(dsi.documentToBytes.size() == 3);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.hashTableImpl.get(this.uri2) != null);
        assertTrue(dsi.hashTableImpl.get(this.uri3) != null);
        assertTrue(dsi.hashTableImpl.get(this.uri4) != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() == null);
    }

    @Test
    public void TestforMaxDocumentsSuccess5() {
        final DocumentStoreImpl dsi = this.createStoreAndPutOne();
        dsi.setMaxDocumentCount(2);
        this.AddDocumentsTXT23AND4(dsi);
        assertTrue(dsi.documentToBytes.size() == 2);
        assertTrue(dsi.hashTableImpl.get(this.uri1) == null);
        assertTrue(dsi.hashTableImpl.get(this.uri2) == null);
        assertTrue(dsi.hashTableImpl.get(this.uri3) != null);
        assertTrue(dsi.hashTableImpl.get(this.uri4) != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() != null);
        assertTrue(dsi.stackImpl.pop() == null);
    }

    // Now time (ahh ;) ) to deal with rearranging the times and then removing the
    // right document
    @Test
    public void testREGULARSEARCHChangeTimeWorksSuccess() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.search("doc1");
        final DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
    }

    @Test
    public void testREGULARSEARCHChangeTimeWorksSuccess2() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.search("doc1");
        DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
        dsi.memoryCollector.insert(oldestDoc);
        dsi.search("doc2");
        oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt3));
        assertFalse(oldestDoc.getWholeText().equals(this.txt2));
    }

    @Test
    public void testREGULARSEARCHPDFChangeTimeWorksSuccess() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.searchPDFs("doc1");
        final DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
    }

    @Test
    public void testREGULARSEARCHPDFChangeTimeWorksSuccess2() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.searchPDFs("doc1");
        DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
        dsi.memoryCollector.insert(oldestDoc);
        dsi.search("doc2");
        oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt3));
        assertFalse(oldestDoc.getWholeText().equals(this.txt2));
    }

    @Test
    public void testREGULARSEARCHBYPREFIXChangeTimeWorksSuccess() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.searchByPrefix("doc1");
        final DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
    }

    @Test
    public void testREGULARSEARCHBYPREFIXhangeTimeWorksSuccess2() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.searchByPrefix("doc1");
        DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
        dsi.memoryCollector.insert(oldestDoc);
        dsi.searchByPrefix("doc2");
        oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt3));
        assertFalse(oldestDoc.getWholeText().equals(this.txt2));
    }

    @Test
    public void testREGULARSEARCHBYPREFIXhangeTimeWorksSuccess3() {
        final DocumentStoreImpl dsi = createStoreAndPutAllWithPDFText();
        dsi.searchByPrefix("text");
        DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertEquals(this.pdfTxt5, oldestDoc.getWholeText());
        dsi.memoryCollector.insert(oldestDoc);
        dsi.searchByPrefix("PDF");
        oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        String oldDocString = oldestDoc.getWholeText();
        assertEquals(this.txt2 , oldDocString);
    }

    @Test
    public void testREGULARSEARCHPDFsBYPREFIXChangeTimeWorksSuccess() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.searchPDFsByPrefix("doc1");
        final DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
    }

    @Test
    public void testREGULARSEARCHPDFsBYPREFIXhangeTimeWorksSuccess2() {
        final DocumentStoreImpl dsi = createStoreAndPutAll();
        dsi.searchPDFsByPrefix("doc1");
        DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt2));
        assertFalse(oldestDoc.getWholeText().equals(this.txt1));
        dsi.memoryCollector.insert(oldestDoc);
        dsi.searchByPrefix("doc2");
        oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertTrue(oldestDoc.getWholeText().equals(this.txt3));
        assertFalse(oldestDoc.getWholeText().equals(this.txt2));
    }

    @Test
    public void testREGULARSEARCHPDFsBYPREFIXhangeTimeWorksSuccess3() {
        final DocumentStoreImpl dsi = createStoreAndPutAllWithPDFText();
        dsi.searchPDFsByPrefix("PDF");
        DocumentImpl oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        assertEquals(this.txt1 ,oldestDoc.getWholeText()); 
        //assertTrue(oldestDoc.getWholeText().equals(this.txt3) || oldestDoc.getWholeText().equals(this.txt4));
        //assertFalse(oldestDoc.getWholeText().equals(this.txt1) && oldestDoc.getWholeText().equals(this.txt2));
        dsi.memoryCollector.insert(oldestDoc);
        dsi.searchByPrefix("text");
        oldestDoc = (DocumentImpl) dsi.memoryCollector.removeMin();
        final String oldDocString = oldestDoc.getWholeText();
        assertTrue(oldDocString != null);
    }
    // 
}