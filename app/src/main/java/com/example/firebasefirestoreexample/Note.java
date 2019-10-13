package com.example.firebasefirestoreexample;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentId;
    private String title;
    private String description;

    public Note(){} // needed for Firestore

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Exclude                        //don't want documentId to appear in the document
    public String getDocumentId() {
        return documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
