import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;


public class test1 
{
    
    public static void main(String[] args) throws IOException, URISyntaxException 
    {
        DocumentStoreImpl newStore = new DocumentStoreImpl();
        
        //First Document
        URI uriAbsolute = new URI("file://Users/TheBigLipper/LifschitzDavid/");
        byte[] array = Files.readAllBytes(Paths.get("/Users/", "TheBigLipper/","LifschitzDavid/", "pdfNew.pdf"));
        ByteArrayInputStream newBAIS = new ByteArrayInputStream(array);
        URI uri = new URI("helloPDF");
        uriAbsolute.resolve(uri);
        //Testing getting a null byte Array
        byte[] nullByteArray = newStore.getDocumentAsPdf(uri);
        //Testing putting the document into the first Node in HashTable
        newStore.putDocument(newBAIS, uri, DocumentStore.DocumentFormat.PDF);
        //Testing getting the document from the first Node in HashTable as a PDF (byte[])
        //byte[] docAsPDF = newStore.getDocumentAsPdf(uri);
        //Testing getting the document from the first Node in HashTable as a TXT File (String)
        //String docAsTXT = newStore.getDocumentAsTxt(uri);
        

        //Second Document 
        array = Files.readAllBytes(Paths.get("/Users/", "TheBigLipper/","LifschitzDavid/", "helloPDF.txt"));
        newBAIS = new ByteArrayInputStream(array);
        URI uri2 = new URI("helloTXT");
        //Testing putting the document into the first Node in HashTable
        newStore.putDocument(newBAIS, uri2, DocumentStore.DocumentFormat.TXT);
        //Testing getting the document from the first Node in HashTable as a PDF (byte[])
        //byte[] docAsPDF2 = newStore.getDocumentAsPdf(uri2);
        //Testing getting the document from the first Node in HashTable as a TXT File (String)
        //String docAsTXT2 = newStore.getDocumentAsTxt(uri2);

        //2a
        array = Files.readAllBytes(Paths.get("/Users/", "TheBigLipper/","LifschitzDavid/", "helloPDF.txt"));
        newBAIS = new ByteArrayInputStream(array);
        URI uri3 = new URI("helloTXA");
        newStore.putDocument(newBAIS, uri3, DocumentStore.DocumentFormat.TXT);
        //byte[] docAsPDF3 = newStore.getDocumentAsPdf(uri3);
        //String docAsTXT3 = newStore.getDocumentAsTxt(uri3);
        
        //2b
        array = Files.readAllBytes(Paths.get("/Users/", "TheBigLipper/","LifschitzDavid/", "helloPDF.txt"));
        newBAIS = new ByteArrayInputStream(array);
        URI uri4 = new URI("helloTXB");
        newStore.putDocument(newBAIS, uri4, DocumentStore.DocumentFormat.TXT);
        //byte[] docAsPDF4 = newStore.getDocumentAsPdf(uri4);
        //String docAsTXT4 = newStore.getDocumentAsTxt(uri4);
        
        //2c
        // array = Files.readAllBytes(Paths.get("/Users/", "TheBigLipper/","LifschitzDavid/", "helloPDF.txt"));
        // newBAIS = new ByteArrayInputStream(array);
        // URI uri5 = new URI("helloTXC");
        // newStore.putDocument(newBAIS, uri5, DocumentStore.DocumentFormat.TXT);
        //byte[] docAsPDF5 = newStore.getDocumentAsPdf(uri5);
        //String docAsTXT5 = newStore.getDocumentAsTxt(uri5);



        //Third Document Put
        // String test3 = "Hello My Name is David. This is test 3.";
        // array = test3.getBytes();
        // newBAIS = new ByteArrayInputStream(array);
        // URI uri6 = new URI("helloPDF");
        // newStore.putDocument(newBAIS, uri6, DocumentStore.DocumentFormat.TXT);
        //Testing Deleting documents
        Boolean bools1 = newStore.deleteDocument(uri4);
        Boolean bools2 = newStore.deleteDocument(uri);
        Boolean bools3 = newStore.deleteDocument(uri2);
        Boolean bools4 = newStore.deleteDocument(uri3);




        array = Files.readAllBytes(Paths.get("/Users/", "TheBigLipper/","LifschitzDavid/", "helloPDF.txt"));
        newBAIS = new ByteArrayInputStream(array);
        uri = new URI("helloTXT");
        newStore.putDocument(newBAIS, uri, DocumentStore.DocumentFormat.TXT);
        byte[] pdfFile = newStore.getDocumentAsPdf(uri);
        int hi = 1;

    }

}