package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

    private String baseDir;
    public DocumentPersistenceManager(File baseDir){
        this.baseDir = baseDir.getAbsolutePath();
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        // This method gets involved whenever the parser encounters the Dog
		// object (for which this serializer is registered)
		JsonObject object = new JsonObject();
		String uriString = uri.getPath();
        object.addProperty("uri", uriString);
        object.addProperty("Document As Txt", val.getDocumentAsTxt());
        object.addProperty("Document as PDF", val.getDocumentAsPdf().toString());
		// we create the json object for the dog and send it back to the
        // Gson serializer
        String newPath = this.baseDir + "edu.yu.cs" + uriString;
        newPath += ".json";
        File newFile = new File(newPath);
        newFile.createNewFile();
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        return null;
    }
}
