package edu.yu.cs.com1320.project.stage4.impl;

import java.io.ByteArrayOutputStream;
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

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;

import edu.yu.cs.com1320.project.stage4.Document;

public class DocumentStoreImpl implements edu.yu.cs.com1320.project.stage4.DocumentStore {
    HashTableImpl hashTableImpl;
    HashTableImpl garabageCollector;
    StackImpl<Undoable> stackImpl;
    StackImpl<Undoable> garbageCollectorStackImpl;
    Function undoFunction = this.getUndoFunction();
    TrieImpl searchTrie;
    MinHeapImpl memoryCollector;
    MinHeapImpl tempMemory;
    Map<DocumentImpl,Integer> documentToBytes;
    int maxDocCount = 1000;
    int maxByteCount = 200000;
    int currentByteCountInDocStore;
    long startTime;

    public DocumentStoreImpl() {
        this.hashTableImpl = new HashTableImpl<>();
        this.garabageCollector = new HashTableImpl<>();
        this.stackImpl = new StackImpl<>();
        this.searchTrie = new TrieImpl<>();
        this.garbageCollectorStackImpl = new StackImpl<>();
        this.memoryCollector = new MinHeapImpl<>();
        this.tempMemory = new MinHeapImpl<>();
        this.startTime = System.nanoTime();
        this.documentToBytes = new HashMap<DocumentImpl,Integer>();
    }

    StackImpl<Undoable> getStackImpl() {
        return this.stackImpl;
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
                this.putInTwoHashTables(deletedDoc.getHashCode(), deletedDoc, deletedDoc.getHashCode(), null);
                for (Object word : deletedDoc.getMappedWordToCount().keySet()) {
                    String wordString = (String) word;
                    this.searchTrie.put(wordString, deletedDoc);
                }
                deletedDoc.setLastUseTime(System.nanoTime() - this.startTime);
                this.memoryCollector.insert(deletedDoc);
                this.memoryCollector.reHeapify(deletedDoc);
                
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
                    this.putInTwoHashTables(replacedDoc.getHashCode(), replacedDoc, replacingDoc.getHashCode(), replacingDoc);
                    replacingDoc.setLastUseTime(System.nanoTime() - this.startTime);
                    DocumentImpl temp = (DocumentImpl) this.memoryCollector.removeMin();
                    this.memoryCollector.insert(temp);
                    this.memoryCollector.reHeapify(temp);
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
        String wholeText = returnAsString(bytesArray, format);
        DocumentImpl lastDoc = null;
        if (input == null) {
            lastDoc = (DocumentImpl) this.hashTableImpl.put(hashCode, null);
            if(lastDoc == null) return 0;
            Set<String> keySet = lastDoc.getMappedWordToCount().keySet();
            for (String string : keySet) {
                this.searchTrie.delete(string, lastDoc);
            }
        } else {
            DocumentImpl newDoc = createNewDocument(uri, wholeText, hashCode, bytesArray);
            lastDoc = (DocumentImpl) this.hashTableImpl.put(hashCode, newDoc);
            if(lastDoc != null && (newDoc.getWholeText().equals(lastDoc.getWholeText())))
            {
                GenericCommand newCommand = new GenericCommand((newDoc.getKey()), null);
                this.stackImpl.push(newCommand);
                return lastDoc.getHashCode();
            }
            addWordsToSearchTrieAndAddDocToMinHeapANDdealWithMaximumStorage(newDoc);
        }
        if (lastDoc != null) {
            returningHashCode = lastDoc.getWholeText().hashCode();
            this.garabageCollector.put(lastDoc.getHashCode(), lastDoc);
        }
        addCommandToStack(uri);
        return returningHashCode;
    }

    private void addWordsToSearchTrieAndAddDocToMinHeapANDdealWithMaximumStorage(DocumentImpl newDoc) {
        addWordsToSearchTrieAndAddDocToMinHeap(newDoc);
        dealWithMaximumStorage(newDoc);
    }

    private void dealWithMaximumStorage(DocumentImpl newDoc) {
        if(this.currentByteCountInDocStore + newDoc.getTotalByteMemory() > this.maxByteCount) 
        dealWithMaxBytes();
        if(this.documentToBytes.size() + 1 > this.maxDocCount)
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
        this.memoryCollector.insert(newDoc);
    }

    private void addWordsToSearchTrie(DocumentImpl lastDoc) {
        Set<String> set = lastDoc.getMappedWordToCount().keySet();
        for (String word : set) {
            this.searchTrie.put(word, lastDoc);
        }
    }

    private void addCommandToStack(URI uri) {
        GenericCommand gC = new GenericCommand(uri, this.undoFunction);
        this.stackImpl.push(gC);
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
        if ((hti.put(uri, null)) == null) {
            deleted = false;
        } else {
            this.garabageCollector.put(deletedDoc.getHashCode(), deletedDoc);
            Set<String> keySet = deletedDoc.getMappedWordToCount().keySet();
            for (String string : keySet) {
                this.searchTrie.delete(string, deletedDoc);
            }
            deletedDoc.setLastUseTime(System.nanoTime());
            DocumentImpl temp = (DocumentImpl) this.memoryCollector.removeMin();
            this.memoryCollector.insert(temp);
            this.memoryCollector.reHeapify(temp);
        }
        addCommandToStack(uri);
        return deleted;
    }

    

    private byte[] convertISToByteArray(InputStream is) {
        byte[] byteArray = new byte[2048];
        if(is == null) return byteArray;
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
            for (int i = 0; i < cCommand.size()+1; i++) {
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
        HashTableImpl hti = this.hashTableImpl;
        DocumentImpl newDoc = (DocumentImpl) (hti.get(uri));
        return newDoc;
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
        Comparator<DocumentImpl> docComparator = createTheComparatorBasedOffAString(allCaps);
        List<DocumentImpl> listOfDocs = this.searchTrie.getAllSorted(allCaps, docComparator);
        if (listOfDocs == null) {
            return null;
        }
        List<String> mList = new ArrayList<>();
        long newTime = System.nanoTime();
        for (DocumentImpl doc : listOfDocs) {
            mList.add(doc.getWholeText());
            doc.setLastUseTime(newTime);
            this.memoryCollector.reHeapify(doc);
        }
        return mList;
    }

    /**
     * same logic as search, but returns the docs as PDFs instead of as Strings
     */
    public List<byte[]> searchPDFs(String keyword) {
        String allCaps = keyword.toUpperCase();
        Comparator<DocumentImpl> docComparator = createTheComparatorBasedOffAString(allCaps);
        List<DocumentImpl> listOfDocs = this.searchTrie.getAllSorted(keyword, docComparator);
        if (listOfDocs == null) {
            return null;
        }
        List<byte[]> mList = new ArrayList<>();
        long newTime = System.nanoTime();
        for (DocumentImpl doc : listOfDocs) {
            try {
                mList.add(textToPdfData(doc.getWholeText()));
                doc.setLastUseTime(newTime);
                this.memoryCollector.reHeapify(doc);
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
        Comparator<DocumentImpl> docComparator = createTheComparatorBasedOffAString(allCaps);
        List<DocumentImpl> listOfDocs = this.searchTrie.getAllWithPrefixSorted(allCaps, docComparator);
        List<String> mList = new ArrayList<>();
        if (listOfDocs != null) {
            long newTime = System.nanoTime();
            for (DocumentImpl doc : listOfDocs) {
                mList.add(doc.getWholeText());
                doc.setLastUseTime(newTime);
                this.memoryCollector.reHeapify(doc);
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
        Comparator<DocumentImpl> docComparator = createTheComparatorBasedOffAString(allCaps);
        List<DocumentImpl> listOfDocs = this.searchTrie.getAllWithPrefixSorted(allCaps, docComparator);
        List<byte[]> mList = new ArrayList<>();
        if (listOfDocs != null) {
            long newTime = System.nanoTime();
            for (DocumentImpl doc : listOfDocs) {

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
            this.hashTableImpl.put(doc.getKey(), null);
            this.garabageCollector.put(doc.getKey(), doc);
            addCommandSetToStack(doc.getKey(), cS);
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

    private void addCommandSetToStack(URI uri, CommandSet cS) {
        GenericCommand gC = new GenericCommand(uri, this.undoFunction);
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

    private void dealWithMaxDocs() 
    {
        while(this.documentToBytes.size() > this.maxDocCount)
        {
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

    private void dealWithMaxBytes()
    {
        for(this.currentByteCountInDocStore = countAllTheBytes(); currentByteCountInDocStore > this.maxByteCount;this.currentByteCountInDocStore = countAllTheBytes())
        {
            obliterateTheDocuments();
        }
    }
    private int countAllTheBytes() {
        this.currentByteCountInDocStore = 0;
        for(Integer byteCount: documentToBytes.values())
        {
            this.currentByteCountInDocStore += byteCount;
        }
        return this.currentByteCountInDocStore;
    }

    private void obliterateTheDocuments() {
        //Get rid from Heap & now have the doc to use for all Obliteration
        DocumentImpl docToDelete = (DocumentImpl) this.memoryCollector.removeMin();
        //Get rid from HashTables
        this.hashTableImpl.put(docToDelete.getKey(), null);
        this.garabageCollector.put(docToDelete.getKey(), null);
        //Get rid from Trie
        for (Object word : docToDelete.getMappedWordToCount().keySet().toArray()) {
            this.searchTrie.delete((String) word, docToDelete);
        }
        //Get rid from Stacks of Commands
        Boolean bools = true;
        while(bools == true) bools = getRidOfCommandWithoutUndoingIt(docToDelete);
        //Get rid from Map of doc to ByteSize
        this.documentToBytes.remove(docToDelete);
    }

    private boolean getRidOfCommandWithoutUndoingIt(DocumentImpl docToDelete) 
    {
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

}