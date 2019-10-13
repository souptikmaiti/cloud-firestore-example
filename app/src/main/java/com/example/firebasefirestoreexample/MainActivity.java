package com.example.firebasefirestoreexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etDescription;
    private Button btnAdd;
    private TextView tvData;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private ListenerRegistration noteListener;

    public static final String NOTE_TITLE = "title";
    public static final String NOTE_DESCRIPTION = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        btnAdd = findViewById(R.id.btn_add);
        tvData = findViewById(R.id.tv_data);

        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("notes");
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteListener = collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(MainActivity.this, "Loading Failed", Toast.LENGTH_LONG).show();
                    return;
                }
                String data = "";
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    data += "Document Id: "+documentSnapshot.getId() + "\nTitle: " + note.getTitle()
                            +"\nDescription: " + note.getDescription() +"\n\n";
                }
                tvData.setText(data);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteListener.remove();
    }

    public void addNote(View v){
        Note note = new Note(etTitle.getText().toString(),etDescription.getText().toString());
        collectionReference.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "Note added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
