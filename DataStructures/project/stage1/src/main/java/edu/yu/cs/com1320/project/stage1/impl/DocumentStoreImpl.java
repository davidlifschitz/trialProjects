package edu.yu.cs.com1320.project.stage1.impl;


import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.yu.cs.com1320.project.impl.HashTableImpl;



import edu.yu.cs.com1320.project.stage1.DocumentStore;

public class DocumentStoreImpl implements DocumentStore {
    HashTableImpl hashTableImpl;

    
    public DocumentStoreImpl()
    {
        this.hashTableImpl = new HashTableImpl<>();
    }

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the String version of the previous doc. 
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     * @throws IOException
     */

    
    public int putDocument(InputStream input, URI uri, DocumentFormat format)
    {
        int returningHashCode = 0;
        if(uri == null || format == null)
        {
            throw new IllegalArgumentException();
        } else 
        {

            try {
                byte[] bytesArray = convertISToByteArray(input);
                
                int hashCode = StamHashFunction(uri);
                if(format == DocumentFormat.TXT)
                {
                    String string1 = convertTXTToString(bytesArray);
                    DocumentImpl newDoc = new DocumentImpl(uri, string1, hashCode);
                    int key = hashCode;
                    if (this.hashTableImpl.put(key, newDoc) == null) {
                        returningHashCode = 0;
                    } else {
                        returningHashCode = newDoc.thisString.hashCode();    
                    }
                    //I'm stuck right here. I need to return 0 when i don't have a previous doc at the same uri
                    //But! the hashtableimpl.put returns a boolean. so i need to create an if-else statement for the state of the boolean and then return an int in either situation.
                    //However! i'm not able to do .equals or == on a null (which is what I'm required to return if a previous doc doesn't exist in the hashtableimpl)
                        
                    //Temporary answer is to place the hashTableImpl put method in an if-else statement
                    // It seems to be working
                    
                    
                    //FIGURE OUT HOW TO RETURN THE returningHashCode
                } else if(format == DocumentFormat.PDF)
                {
                    String string2 = convertPDFISToString(bytesArray);
                    DocumentImpl newDoc = new DocumentImpl(uri, string2, hashCode, bytesArray);
                    int key = hashCode;
                    if (this.hashTableImpl.put(key, newDoc) == null) {
                        returningHashCode = 0;
                    } else {
                        returningHashCode = newDoc.thisString.hashCode();    
                    }
                    //FIGURE OUT HOW TO RETURN THE returningHashCode   
                }
        
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Figure out how to return different Hashs from here.
                return returningHashCode; 
        }
        
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that URI
     */
    public byte[] getDocumentAsPdf(URI uri)
    {
        HashTableImpl hti = this.hashTableImpl;
        int index = HashFunction(uri);
        DocumentImpl newDoc = (DocumentImpl) (hti.get(uri)); 
        if(newDoc == null)
        {
            return null;
        } else {
        byte[] returnByteArray = newDoc.getDocumentAsPdf();
        return returnByteArray;
        }
        
    }
        
    

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as TXT, i.e. a String, or null if no document exists with that URI
     */
    public String getDocumentAsTxt(URI uri)
    {
        if(uri != null){
            HashTableImpl hti = this.hashTableImpl;
            DocumentImpl newDoc = (DocumentImpl) (hti.get(uri)); 
            if(newDoc == null)
            {
                return null;
            } else {
                String newString = newDoc.getDocumentAsTxt();
                return newString;
            }
            
        } else 
        {
            return null;
        }
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri) {
        HashTableImpl hti = this.hashTableImpl;
        Boolean deleted = true;
        if((hti.put((uri), null)) == null)
        {
            deleted = false;
        } 
        return deleted;
}


    private int StamHashFunction(URI myURI) {return myURI.hashCode();}
    private int HashFunction(URI myKey){return (myKey.hashCode() & 0xfffffff) % 5;}
    
    private byte[] convertISToByteArray(InputStream is)
    {
        byte[] byteArray = new byte[2048];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int readN;
        try 
        {
            while((readN = is.read(byteArray, 0, byteArray.length)) != -1)
            {
                baos.write(byteArray, 0, readN);
            }
            baos.flush();
        } catch (IOException e) {e.printStackTrace();}
        return baos.toByteArray();
    }
    
    private String convertTXTToString(byte[] is)
    {
        return new String(is);
    }
    
    private String convertPDFISToString(byte[] is)
    {
        String pdfString = "";
        try {
                PDDocument doc = PDDocument.load(is);
                pdfString = new PDFTextStripper().getText(doc).trim();
                doc.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return pdfString;
    }
}