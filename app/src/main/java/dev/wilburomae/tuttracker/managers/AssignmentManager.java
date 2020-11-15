package dev.wilburomae.tuttracker.managers;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;

@SuppressWarnings("ALL")
public class AssignmentManager {
    private static final StorageReference FOLDER_ROOT = FirebaseStorage.getInstance().getReference();
    private static final StorageReference FOLDER_ASSIGNED = FOLDER_ROOT.child("assigned");
    private static final StorageReference FOLDER_SUBMITTED = FOLDER_ROOT.child("submitted");
    private static final StorageReference FOLDER_GRADED = FOLDER_ROOT.child("graded");
    private static final DatabaseReference DB_ROOT = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference DB_ASSIGNMENTS = DB_ROOT.child("assignments");

    public static UploadTask upload(Assignment assignment, Uri uri, String mimeType, AssignmentStage stage) {
        UploadTask task = null;
        switch (stage) {
            case TO_ASSIGN:
                assignment.setMimeAssigned(mimeType);
                task = FOLDER_ASSIGNED.child(assignment.getId()).putFile(uri);
                break;
            case TO_SUBMIT:
                assignment.setMimeSubmitted(mimeType);
                task = FOLDER_SUBMITTED.child(assignment.getId()).putFile(uri);
                break;
            case TO_GRADE:
                assignment.setMimeGraded(mimeType);
                task = FOLDER_GRADED.child(assignment.getId()).putFile(uri);
                break;
        }
        DB_ASSIGNMENTS.child(assignment.getId()).setValue(assignment);
        return task;
    }

    public static void fetch(String email, ChildEventListener listener) {
        DB_ASSIGNMENTS.orderByChild("tutorEmail").equalTo(email).addChildEventListener(listener);
        DB_ASSIGNMENTS.orderByChild("studentEmail").equalTo(email).addChildEventListener(listener);
    }

    public static Object[] open(Context context, Assignment assignment, AssignmentStage stage) {
        if (isExternalStorageWritable()) {
            File cacheDir = context.getCacheDir();
            Object[] array = new Object[3];
            switch (stage) {
                case TO_ASSIGN:
                    File assignedDir = new File(cacheDir, "assigned");
                    if (!assignedDir.exists()) assignedDir.mkdir();
                    File assignedFile = new File(assignedDir, assignment.getTitle());
                    array[0] = assignedFile;
                    if (!assignedFile.exists())
                        array[1] = FOLDER_ASSIGNED.child(assignment.getId()).getFile(assignedFile);
                    array[2] = assignment.getMimeAssigned();
                    break;
                case TO_SUBMIT:
                    File submittedDir = new File(cacheDir, "submitted");
                    if (!submittedDir.exists()) submittedDir.mkdir();
                    File submittedFile = new File(submittedDir, assignment.getTitle());
                    array[0] = submittedFile;
                    if (!submittedFile.exists())
                        array[1] = FOLDER_SUBMITTED.child(assignment.getId()).getFile(submittedFile);
                    array[2] = assignment.getMimeSubmitted();
                    break;
                case TO_GRADE:
                    File gradedDir = new File(cacheDir, "graded");
                    if (!gradedDir.exists()) gradedDir.mkdir();
                    File gradedFile = new File(gradedDir, assignment.getTitle());
                    array[0] = gradedFile;
                    if (!gradedFile.exists())
                        array[1] = FOLDER_GRADED.child(assignment.getId()).getFile(gradedFile);
                    array[2] = assignment.getMimeGraded();
                    break;
            }
            return array;
        } else {
            return null;
        }
    }

    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
