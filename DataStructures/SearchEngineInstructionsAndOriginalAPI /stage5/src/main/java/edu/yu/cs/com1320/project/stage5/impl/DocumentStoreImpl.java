package edu.yu.cs.com1320.project.stage5.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.net.URI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Document;

import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore;

public class DocumentStoreImpl implements DocumentStore {
    protected BTreeImpl<URI, Document> btree;
    private StackImpl<Undoable> stackImpl;
    private TrieImpl<URI> searchTrie;
    private int documentBytes;
    private int documentCount;
    private Integer maxDocCount;
    private Integer maxByteCount;
    private MinHeapImpl<HeapElement> memoryCollector;
    private Set<URI> urisInMemory;
    

    public DocumentStoreImpl() {
        this.btree = new BTreeImpl<>();    
        btree.setPersistenceManager(new DocumentPersistenceManager(null));
        setTheRestOfTheInstanceVariables();
    }

    public DocumentStoreImpl(File baseDir) {
        this.btree = new BTreeImpl<>();
        btree.setPersistenceManager(new DocumentPersistenceManager(baseDir));
        setTheRestOfTheInstanceVariables();
    }

    private void setTheRestOfTheInstanceVariables()
    {
        this.stackImpl = new StackImpl<>();
        this.searchTrie = new TrieImpl<>();
        this.maxByteCount = null;
        this.maxDocCount = null;
        this.memoryCollector = new MinHeapImpl<>();
        this.documentBytes = 0;
        this.documentCount = 0;
        this.urisInMemory = new HashSet<>();
        this.btree.put(URI.create(""), null);
    }

    private Comparator<DocumentImpl> createTheComparatorBasedOffAString(String string) {
        Comparator<DocumentImpl> comparator = (DocumentImpl firstDoc, DocumentImpl secondDoc) -> {
            if ((!firstDoc.getMappedWordToCount().containsKey(string))
                    || (secondDoc.getMappedWordToCount().containsKey(string))) {
                return 0;
            } else {
                int firstDocAmount = (int) firstDoc.getMappedWordToCount().get(string);
                int secondDocAmount = (int) secondDoc.getMappedWordToCount().get(string);
                if (firstDocAmount < secondDocAmount) {
                    return -1;
                } else if (firstDocAmount > secondDocAmount) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        return comparator;
    }
    private Comparator<URI> createURIComparatorBasedOffAString(String string)
    {
        Comparator<URI> comparator = (URI firstURI, URI secondURI) -> {
            if (firstURI.hashCode() < secondURI.hashCode()) {
                return -1;
            } else if (firstURI.hashCode() > secondURI.hashCode()) {
                return 0;
            } else {
                return 1;
            }
        };
        return comparator;
    }

    private int StamHashFunction(URI myURI) {
        return myURI.hashCode();
    }

    private void putInTwoHashTables(int uri1, DocumentImpl doc1, int uri2, DocumentImpl doc2) {
        this.hashTableImpl.put(uri1, doc1);
        this.garabageCollector.put(uri2, doc2);
    }

    Function<URI, Boolean> undoPutNew (URI uri) -> {
        Document doc = getFromBTree(uri,false);
        if(doc == null)
        {
            return false;
        } 
        removeAll(doc);
        return true;
        };

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
        Document doc;
        String txt;
        if (format == DocumentFormat.PDF) 
        {
            txt = pdfBytesToString(bytesArray).trim();
            doc = new DocumentImpl(uri,txt, txt.hashCode());
        }
        setDocumentTime(doc);
        if(this.btree.get(uri)==null){
            return putNew(doc);
        } else {
            return putReplace(doc);
        }
    }
    private int putNew(Document doc){
        makeRoom(doc);
        insertAll(doc);
        addCommandToStack(doc.getKey(), undoPutNew);
        return 0;
    }
    private int putDelete(URI uri) {
        Document doc = getFromBTree(uri, false);
        if(doc == null) {
            addCommandToStack(uri, undoNoOp);
            return 0;
        }
    }

    private void addWordsToSearchTrieAndAddDocToMinHeapANDdealWithMaximumStorage(DocumentImpl newDoc) {
        addWordsToSearchTrieAndAddDocToMinHeap(newDoc);
        dealWithMaximumStorage(newDoc);
    }

    private void dealWithMaximumStorage(DocumentImpl newDoc) {
        if (this.currentByteCountInDocStore + newDoc.getTotalByteMemory() > this.maxByteCount)
            dealWithMaxBytes();
        if (this.documentToBytes.size() + 1 > this.maxDocCount)
            dealWithMaxDocs();
    }

    private DocumentImpl createNewDocument(URI uri, String wholeText, int hashCode, byte[] bytesArray) {
        DocumentImpl newDoc = new DocumentImpl(uri, wholeText, hashCode);
        newDoc.setByteArray(bytesArray);
        newDoc.setTotalByteMemory();
        newDoc.setLastUseTime(System.nanoTime());
        this.documentToBytes.put(newDoc, newDoc.getTotalByteMemory());
        this.currentByteCountInDocStore = countAllTheBytes();
        return newDoc;
    }

    private void addWordsToSearchTrieAndAddDocToMinHeap(DocumentImpl newDoc) {
        addWordsToSearchTrie(newDoc);
        addDocToMinHeap(newDoc);
    }

    private void addDocToMinHeap(DocumentImpl newDoc) {
        this.memoryCollector.insert(newDoc.getKey());
    }

    private void addWordsToSearchTrie(DocumentImpl lastDoc) {
        Set<String> set = lastDoc.getMappedWordToCount().keySet();
        for (String word : set) {
            this.searchTrie.put(word, lastDoc.getKey());
        }
    }

    private void addCommandToStack(URI uri, DocumentImpl doc) {
        GenericCommand gC = new GenericCommand(uri, this.getUndoFunction());
        this.stackImpl.push(gC);
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that
     *         URI
     */
    public byte[] getDocumentAsPdf(URI uri) {
        //HashTableImpl hti = this.hashTableImpl;
        //DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
        DocumentImpl newDoc = (DocumentImpl) this.btree.get(uri);
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
            // HashTableImpl hti = this.hashTableImpl;
            // DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
            DocumentImpl newDoc = (DocumentImpl) this.btree.get(uri);
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
        //HashTableImpl hti = this.hashTableImpl;
        boolean deleted = true;
        //DocumentImpl deletedDoc = (DocumentImpl) this.hashTableImpl.get(uri);
        DocumentImpl deletedDoc = (DocumentImpl) this.btree.get(uri);
        if (btree.put(uri,null) == null) {
            deleted = false;
            return deleted;
        } else {
            //COME BACK TO THIS SOON
            Set<String> keySet = deletedDoc.getMappedWordToCount().keySet();
            for (String string : keySet) {
                this.searchTrie.delete(string, deletedDoc);
            }
            deletedDoc.setLastUseTime(0);
            URI temp = (URI) this.memoryCollector.removeMin();
            this.memoryCollector.insert(temp);
            this.memoryCollector.reHeapify(temp);
            this.memoryCollector.removeMin();
        }
        GenericCommand gC = new GenericCommand(deletedDoc.getKey(), this.getUndoFunction());
        this.stackImpl.push(gC);
        return deleted;
    }

    private byte[] convertISToByteArray(InputStream is) {
        byte[] byteArray = new byte[2048];
        if(is == null) return null;
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
        if(is == null) return null;
        return new String(is);
    }

    private String convertPDFISToString(byte[] is) {
        if(is == null) return null;
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

    public void undo() throws IllegalStateException {
        Undoable currentCommand = this.stackImpl.peek();
        if (currentCommand == null) {
            throw new IllegalStateException();
        }
        if (currentCommand instanceof GenericCommand) {
            GenericCommand gc = (GenericCommand) currentCommand;
            gc.undo();
        }
        if (currentCommand instanceof CommandSet) {
            CommandSet cCommand = (CommandSet) currentCommand;
            GenericCommand<Target>[] commandArray = new GenericCommand[1];
            commandArray = (GenericCommand<Target>[]) cCommand.toArray(commandArray);
            for (int i = 0; i < cCommand.size(); i++) {
                GenericCommand gc = commandArray[i];
                gc.undo();
                cCommand.remove(gc);
            }
        }
        this.stackImpl.pop();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * 
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack
     *                               for the given URI
     *
     *
     *                               public void undo(URI uri) throws
     *                               IllegalStateException { StackImpl<CommandSet>
     *                               tempStack = new StackImpl<>(); CommandSet
     *                               currentCommand = tempStack.peek(); // Moving
     *                               over all the commands into the temporary stack
     *                               until reaching the one // with the correct URI
     *                               while (currentCommand != null &&
     *                               !(currentCommand.getUri().equals(uri))) {
     *                               tempStack.push(this.stackImpl.pop());
     *                               currentCommand = this.stackImpl.peek(); } // If
     *                               the URI never matched up (aka the command
     *                               doesn't exist) if (currentCommand == null) {
     *                               throw new IllegalStateException(); } else //
     *                               Undo the next node { this.undo(); } // Push the
     *                               commands back from the tempStack to the
     *                               stackImpl while (tempStack.peek() != null) {
     *                               this.stackImpl.push(tempStack.pop()); } }
     */

    /**
     ** @return the Document object stored at that URI, or null if there is no such
     *         Document
     */
    protected Document getDocument(URI uri) {
        //HashTableImpl hti = this.hashTableImpl;
        //DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
        return (DocumentImpl) btree.get(uri);
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * 
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack
     *                               for the given URI
     */
    @Override
    public void undo(URI uri) throws IllegalStateException {
        if (this.stackImpl.peek() == null) {
            throw new IllegalStateException();
        }
        Boolean foundIt = false;
        while (foundIt == false) {
            // if (this.stackImpl.size() > 0) {
            // break;
            // }
            Undoable nextCommand = this.stackImpl.pop();
            if (nextCommand == null)
                break;
            if (nextCommand instanceof GenericCommand) {
                GenericCommand command = (GenericCommand) nextCommand;
                URI docUri = (URI) command.getTarget();
                if (docUri.equals(uri)) {
                    command.undo();
                    foundIt = true;
                } else {
                    this.garbageCollectorStackImpl.push(command);
                }
            } else if (nextCommand instanceof CommandSet) {
                CommandSet<GenericCommand> setOfCommands = (CommandSet) nextCommand;
                for (GenericCommand command : setOfCommands) {
                    URI thisURI = (URI) command.getTarget();
                    if (thisURI.equals(uri)) {
                        command.undo();
                        setOfCommands.remove(command);
                        foundIt = true;
                        break;
                    }
                }
                if (!setOfCommands.isEmpty()) {
                    this.garbageCollectorStackImpl.push(setOfCommands);
                }
            }
        }
        int sizeOfGarbageCollector = garbageCollectorStackImpl.size();
        for (int i = 0; i < sizeOfGarbageCollector; i++) {
            this.stackImpl.push(garbageCollectorStackImpl.pop());
        }
        if (foundIt == false) {
            throw new IllegalStateException();
        }
    }

    /*
     * protected class DocumentComparator<Value> implements Comparator<DocumentImpl>
     * {
     * 
     * // public int compare(DocumentImpl o1, DocumentImpl o2) { // int
     * comparatorReturnValue = 0;
     * 
     * // return comparatorReturnValue; // } }
     */

    /**
     * converts a string to a PDF doc written out to a byte[]
     */
    private static byte[] textToPdfData(String text) throws IOException {
        // setup document and page
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        content.beginText();
        PDFont font = PDType1Font.TIMES_ROMAN;
        content.setFont(font, 10);
        content.newLineAtOffset(20, 20);
        // add text
        content.showText(text);
        content.endText();
        content.close();
        // save to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    // NEW METHODS FOR STAGE3
    /**
     * Retrieve all documents whose text contains the given keyword. Documents are
     * returned in sorted, descending order, sorted by the number of times the
     * keyword appears in the document. Search is CASE INSENSITIVE.
     * 
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<String> search(String keyword) {
        String allCaps = keyword.toUpperCase();
        Comparator<URI> docComparator = createURIComparatorBasedOffAString(allCaps);
        List<URI> listOfDocs = this.searchTrie.getAllSorted(allCaps, docComparator);
        if (listOfDocs == null) {
            return null;
        }
        List<String> mList = new ArrayList<>();
        long newTime = System.nanoTime();
        for (URI uri : listOfDocs) {
            mList.add(((DocumentImpl) btree.get(uri)).getWholeText());
            ((DocumentImpl) btree.get(uri)).setLastUseTime(newTime);
            this.memoryCollector.reHeapify(((DocumentImpl) btree.get(uri)).getKey());
        }
        return mList;
    }

    /**
     * same logic as search, but returns the docs as PDFs instead of as Strings
     */
    public List<byte[]> searchPDFs(String keyword) {
        String allCaps = keyword.toUpperCase();
        Comparator<URI> docComparator = createURIComparatorBasedOffAString(allCaps);
        List<URI> listOfDocs = this.searchTrie.getAllSorted(keyword, docComparator);
        if (listOfDocs == null) {
            return null;
        }
        List<byte[]> mList = new ArrayList<>();
        long newTime = System.nanoTime();
        for (URI uri : listOfDocs) {
            try {
                mList.add(textToPdfData(((DocumentImpl) btree.get(uri)).getWholeText()));
                ((DocumentImpl) btree.get(uri)).setLastUseTime(newTime);
                this.memoryCollector.reHeapify(((DocumentImpl) btree.get(uri)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    /**
     * Retrieve all documents whose text starts with the given prefix Documents are
     * returned in sorted, descending order, sorted by the number of times the
     * prefix appears in the document. Search is CASE INSENSITIVE.
     * 
     * @param prefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<String> searchByPrefix(String prefix) {
        String allCaps = prefix.toUpperCase();
        Comparator<URI> docComparator = createURIComparatorBasedOffAString(allCaps);
        List<URI> listOfDocs = this.searchTrie.getAllWithPrefixSorted(allCaps, docComparator);
        List<String> mList = new ArrayList<>();
        if (listOfDocs != null) {
            long newTime = System.nanoTime();
            for (URI uri : listOfDocs) {
                mList.add(((DocumentImpl) btree.get(uri)).getWholeText());
                ((DocumentImpl) btree.get(uri)).setLastUseTime(newTime);
                this.memoryCollector.reHeapify(((DocumentImpl) btree.get(uri)));
            }
        }
        return mList;
    }

    /**
     * same logic as searchByPrefix, but returns the docs as PDFs instead of as
     * Strings
     */
    public List<byte[]> searchPDFsByPrefix(String prefix) {
        String allCaps = prefix.toUpperCase();
        Comparator<URI> docComparator = createURIComparatorBasedOffAString(allCaps);
        List<URI> listOfDocs = this.searchTrie.getAllWithPrefixSorted(allCaps, docComparator);
        List<byte[]> mList = new ArrayList<>();
        if (listOfDocs != null) {
            long newTime = System.nanoTime();
            for (URI uri : listOfDocs) {
                DocumentImpl doc = (DocumentImpl) btree.get(uri);
                mList.add(doc.getDocumentAsPdf());
                doc.setLastUseTime(newTime);
                this.memoryCollector.reHeapify(doc);

            }
        }
        return mList;
    }

    /**
     * delete ALL exact matches for the given key
     * 
     * @param key
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String key) {
        Set<URI> returnSet = new HashSet<>();
        if (key == null || this.stackImpl.peek() == null)
            return returnSet;
        Set<DocumentImpl> bigSet = new HashSet<>();
        // Get the set of documents from the Trie
        String allCaps = key.toUpperCase();
        bigSet = this.searchTrie.deleteAll(allCaps);
        returnSet = deleteAllDocumentsAndAddCommandSetToStack(bigSet);
        return returnSet;
    }

    private Set<URI> deleteAllDocumentsAndAddCommandSetToStack(Set<DocumentImpl> bigSet) {
        // Delete each document by matching its uri
        // Add delete commands to commandSet at each deletion
        CommandSet cS = new CommandSet<>();
        Set<URI> uriSet = new HashSet<>();
        for (DocumentImpl doc : bigSet) {
            //this.hashTableImpl.put(doc.getKey(), null);
            btree.put(doc.getKey(), null);
            addCommandSetToStack(doc.getKey(), cS, doc);
            uriSet.add(doc.getKey());
            doc.setLastUseTime(0);
            DocumentImpl temp = (DocumentImpl) this.memoryCollector.removeMin();
            this.memoryCollector.insert(temp);
            this.memoryCollector.reHeapify(temp);
            this.memoryCollector.removeMin();
        }
        this.stackImpl.push(cS);
        return uriSet;
    }

    private void addCommandSetToStack(URI uri, CommandSet cS, DocumentImpl doc) {
        GenericCommand gC = new GenericCommand(uri, this.getUndoFunction());
        cS.addCommand(gC);
    }

    /**
     * Delete all matches that contain a String with the given prefix. Search is
     * CASE INSENSITIVE.
     * 
     * @param prefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String prefix) {
        Set<URI> returnSet = new HashSet<>();
        if (prefix == null || this.stackImpl.peek() == null)
            return returnSet;
        Set<DocumentImpl> bigSet = new HashSet<DocumentImpl>();
        String allCaps = prefix.toUpperCase();
        bigSet = this.searchTrie.deleteAllWithPrefix(allCaps);
        returnSet = deleteAllDocumentsAndAddCommandSetToStack(bigSet);
        return returnSet;
    }

    // NEW METHODS FOR STAGE 4

    /**
     * set maximum number of documents that may be stored
     * 
     * @param limit
     */
    public void setMaxDocumentCount(int limit) {
        this.maxDocCount = limit;
        dealWithMaxDocs();
    }

    private void dealWithMaxDocs() {
        while (this.documentToBytes.size() > this.maxDocCount) {
            obliterateTheDocuments();
        }
    }

    /**
     * set maximum number of bytes of memory that may be used by all the compressed
     * documents in memory combined
     * 
     * @param limit
     */
    public void setMaxDocumentBytes(int limit) {
        this.maxByteCount = limit;
        dealWithMaxBytes();
    }

    private void dealWithMaxBytes() {
        for (this.currentByteCountInDocStore = countAllTheBytes(); currentByteCountInDocStore > this.maxByteCount; this.currentByteCountInDocStore = countAllTheBytes()) {
            obliterateTheDocuments();
        }
    }

    private int countAllTheBytes() {
        this.currentByteCountInDocStore = 0;
        for (Integer byteCount : documentToBytes.values()) {
            this.currentByteCountInDocStore += byteCount;
        }
        return this.currentByteCountInDocStore;
    }

    private void obliterateTheDocuments() {
        // Get rid from Heap & now have the doc to use for all Obliteration
        URI uriToDelete = (URI) this.memoryCollector.removeMin();
        // Get rid from HashTables
        //this.hashTableImpl.put(docToDelete.getKey(), null);
        //this.garabageCollector.put(docToDelete.getKey(), null);
        // Deal in BTree
        DocumentImpl docToDelete = (DocumentImpl) this.btree.get(uriToDelete);
        this.btree.put(uriToDelete, null);
        // Get rid from Trie
        
        for (Object word : docToDelete.getMappedWordToCount().keySet().toArray()) {
            this.searchTrie.delete((String) word, docToDelete);
        }
        // Get rid from Stacks of Commands
        Boolean bools = true;
        while (bools == true)
            bools = getRidOfCommandWithoutUndoingIt(docToDelete);
        // Get rid from Map of doc to ByteSize
        this.documentToBytes.remove(docToDelete);
    }

    private boolean getRidOfCommandWithoutUndoingIt(DocumentImpl docToDelete) {
        URI uri = docToDelete.getKey();
        Boolean foundIt = false;
        if (this.stackImpl.peek() == null) {
            return foundIt;
        }
        while (foundIt == false) {
            Undoable nextCommand = this.stackImpl.pop();
            if (nextCommand == null)
                break;
            if (nextCommand instanceof GenericCommand) {
                GenericCommand command = (GenericCommand) nextCommand;
                URI docUri = (URI) command.getTarget();
                if (docUri.equals(uri)) {
                    foundIt = true;
                } else {
                    this.garbageCollectorStackImpl.push(command);
                }
            } else if (nextCommand instanceof CommandSet) {
                CommandSet<GenericCommand> setOfCommands = (CommandSet) nextCommand;
                for (GenericCommand command : setOfCommands) {
                    URI thisURI = (URI) command.getTarget();
                    if (thisURI.equals(uri)) {
                        setOfCommands.remove(command);
                        foundIt = true;
                        break;
                    }
                }
                if (!setOfCommands.isEmpty()) {
                    this.garbageCollectorStackImpl.push(setOfCommands);
                }
            }
        }
        int sizeOfGarbageCollector = garbageCollectorStackImpl.size();
        for (int i = 0; i < sizeOfGarbageCollector; i++) {
            this.stackImpl.push(garbageCollectorStackImpl.pop());
        }
        return foundIt;
    }

    private class HeapElement implements Comparable<HeapElement>
    {
        private URI uri;
        public HeapElement(Document doc)
        {
            this.uri = doc.getKey();
        }
        public URI getUri()
        {
            return this.uri;
        }
        @Override
        public int compareTo(HeapElement o)
        {
            long one = btree.get(this.getUri()).getLastUseTime();
            long two = btree.get(o.getUri()).getLastUseTime();
            if(one < two){//older
                return -1;
            } else if(one > two){//newer
                return 1;
            } else {//same
                return 0;
            }
        }
        @Override
        public boolean equals(Object obj) {
            if(obj == this){
                return true;
            }
            if(!(obj instanceof HeapElement)){
                return false;
            }
            return this.getUri().equals(((HeapElement)obj).getUri());
        }
    }
}