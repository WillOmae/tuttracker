package dev.wilburomae.tuttracker.managers;

import android.net.Uri;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;

public class AssignmentManager {
    private static final StorageReference FOLDER_ROOT = FirebaseStorage.getInstance().getReference();
    private static final StorageReference FOLDER_ASSIGNED = FOLDER_ROOT.child("assigned");
    private static final StorageReference FOLDER_SUBMITTED = FOLDER_ROOT.child("submitted");
    private static final StorageReference FOLDER_GRADED = FOLDER_ROOT.child("graded");
    private static final DatabaseReference DB_ROOT = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference DB_ASSIGNMENTS = DB_ROOT.child("assignments");

    public static UploadTask upload(Assignment assignment, Uri uri, AssignmentStage stage) {
        DB_ASSIGNMENTS.child(assignment.getId()).setValue(assignment);

        switch (stage) {
            case TO_ASSIGN:
                return FOLDER_ASSIGNED.child(assignment.getId()).putFile(uri);
            case TO_SUBMIT:
                return FOLDER_SUBMITTED.child(assignment.getId()).putFile(uri);
            case TO_GRADE:
                return FOLDER_GRADED.child(assignment.getId()).putFile(uri);
        }
        return null;
    }

    public static void fetch(String email, ChildEventListener listener) {
        DB_ASSIGNMENTS.orderByChild("tutorEmail").equalTo(email).addChildEventListener(listener);
        DB_ASSIGNMENTS.orderByChild("studentEmail").equalTo(email).addChildEventListener(listener);
    }

    public static void open(Assignment assignment) {

    }
}
