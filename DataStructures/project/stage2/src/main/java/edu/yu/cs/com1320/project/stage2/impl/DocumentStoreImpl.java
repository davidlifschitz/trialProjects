package edu.yu.cs.com1320.project.stage2.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

public class DocumentStoreImpl implements DocumentStore {
    HashTableImpl hashTableImpl;
    HashTableImpl garabageCollector;
    StackImpl<Command> stackImpl;
    Function undoFunction = this.getUndoFunction();

    public DocumentStoreImpl() {
        this.hashTableImpl = new HashTableImpl<>();
        this.garabageCollector = new HashTableImpl<>();
        this.stackImpl = new StackImpl<>();
    }

    private int StamHashFunction(URI myURI) {
        return myURI.hashCode();
    }

    private void putInTwoHashTables(int uri1, DocumentImpl doc1, int uri2, DocumentImpl doc2) {
        this.hashTableImpl.put(uri1, doc1);
        this.garabageCollector.put(uri2, doc2);
    }

    private Function<URI, Boolean> getUndoFunction() {
        return uri -> {
            // Undoing a Delete
            if (this.hashTableImpl.get(uri) == null) {
                // If the doc was never added
                if (this.garabageCollector.get(uri) == null) {
                    return true;
                }
                DocumentImpl deletedDoc = (DocumentImpl) this.garabageCollector.get(uri);
                this.putInTwoHashTables(deletedDoc.hashCode, deletedDoc, deletedDoc.hashCode, null);
            }
            // Undoing a put
            else {
                // Undoing a Stam put
                if (this.garabageCollector.get(uri) == null) {
                    this.hashTableImpl.put(uri, null);
                }
                // Undoing a replacing put
                else {
                    DocumentImpl replacedDoc = (DocumentImpl) this.garabageCollector.get(uri);
                    DocumentImpl replacingDoc = (DocumentImpl) this.hashTableImpl.get(uri);
                    this.putInTwoHashTables(replacedDoc.hashCode, replacedDoc, replacingDoc.hashCode, replacingDoc);
                }
            }
            return true;
        };
    }

    private String returnAsString(byte[] byteArray, DocumentFormat format) {
        String returnString = "";
        switch (format) {
            case TXT:
                returnString = convertTXTToString(byteArray);
                break;
            case PDF:
                returnString = convertPDFISToString(byteArray);
                break;
        }
        return returnString;
    }

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a
     *         previous doc, return the hashCode of the String version of the
     *         previous doc. If InputStream is null, this is a delete, and thus
     *         return either the hashCode of the deleted doc or 0 if there is no doc
     *         to delete.
     * @throws IOException
     */
    public int putDocument(InputStream input, URI uri, DocumentFormat format) {
        int returningHashCode = 0;
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        }

        byte[] bytesArray = convertISToByteArray(input);
        int hashCode = StamHashFunction(uri);
        String string = returnAsString(bytesArray, format);
        DocumentImpl lastDoc = null;
        if (input == null) {
            lastDoc = (DocumentImpl) this.hashTableImpl.put(hashCode, null);
        } else {
            DocumentImpl newDoc = new DocumentImpl(uri, string, hashCode);
            lastDoc = (DocumentImpl) this.hashTableImpl.put(hashCode, newDoc);
        }
        if (lastDoc != null) {
            returningHashCode = lastDoc.thisString.hashCode();
            this.garabageCollector.put(lastDoc.hashCode, lastDoc);
        }
        this.stackImpl.push(new Command(uri, this.undoFunction));
        return returningHashCode;

    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that
     *         URI
     */
    public byte[] getDocumentAsPdf(URI uri) {
        HashTableImpl hti = this.hashTableImpl;
        DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
        if (newDoc == null) {
            return null;
        } else {
            byte[] returnByteArray = newDoc.getDocumentAsPdf();
            return returnByteArray;
        }

    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as TXT, i.e. a String, or null if no document
     *         exists with that URI
     */
    public String getDocumentAsTxt(URI uri) {
        if (uri != null) {
            HashTableImpl hti = this.hashTableImpl;
            DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
            if (newDoc == null) {
                return null;
            } else {
                String newString = newDoc.getDocumentAsTxt();
                return newString;
            }

        } else {
            return null;
        }
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with
     *         that URI
     */
    public boolean deleteDocument(URI uri) {
        HashTableImpl hti = this.hashTableImpl;
        boolean deleted = true;
        DocumentImpl deletedDoc = (DocumentImpl) this.hashTableImpl.get(uri);
        if ((hti.put((uri), null)) == null) {
            deleted = false;
        } else {
            this.garabageCollector.put(deletedDoc.hashCode, deletedDoc);
        }
        return deleted;
    }

    private byte[] convertISToByteArray(InputStream is) {
        byte[] byteArray = new byte[2048];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int readN;
        try {
            while ((readN = is.read(byteArray, 0, byteArray.length)) != -1) {
                baos.write(byteArray, 0, readN);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private String convertTXTToString(byte[] is) {
        return new String(is);
    }

    private String convertPDFISToString(byte[] is) {
        String pdfString = "";
        try {
            PDDocument doc = PDDocument.load(is);
            pdfString = new PDFTextStripper().getText(doc).trim();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfString;
    }

    /**
     * undo the last put or delete command
     * 
     * @throws IllegalStateException if there are no actions to be undone, i.e. the
     *                               command stack is empty
     */
    // This is going to
    public void undo() throws IllegalStateException {
        Command currentCommand = this.stackImpl.peek();
        if (currentCommand == null) {
            throw new IllegalStateException();
        } else {
            currentCommand.undo();
            this.stackImpl.pop();
        } 
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * 
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack
     *                               for the given URI
     */
    public void undo(URI uri) throws IllegalStateException {
        StackImpl<Command> tempStack = new StackImpl<>();
        Command currentCommand = this.stackImpl.peek();
        // Moving over all the commands into the temporary stack until reaching the one
        // with the correct URI
        while (currentCommand != null && !(currentCommand.getUri().equals(uri))) {
            tempStack.push(this.stackImpl.pop());
            currentCommand = this.stackImpl.peek();
        }
        // If the URI never matched up (aka the command doesn't exist)
        if (currentCommand == null) {
            throw new IllegalStateException();
        } else
        // Undo the next node
        {
            this.undo();
        }
        // Push the commands back from the tempStack to the stackImpl
        while (tempStack.peek() != null) {
            this.stackImpl.push(tempStack.pop());
        }
    }

    // NEED TO TEST FOR THIS
    /**
     ** @return the Document object stored at that URI, or null if there is no such
     *         Document
     */
    protected Document getDocument(URI uri) {
        HashTableImpl hti = this.hashTableImpl;
        DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
        return newDoc;
    }

}