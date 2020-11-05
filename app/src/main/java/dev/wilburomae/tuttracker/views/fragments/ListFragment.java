package dev.wilburomae.tuttracker.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;

import dev.wilburomae.tuttracker.BuildConfig;
import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.managers.AssignmentManager;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.listeners.IPickerDialogSelection;

public class ListFragment extends Fragment implements IPickerDialogSelection {
    private void openAssignment(Assignment assignment) {
        AssignmentStage stage;
        if (Constants.isDateSet(assignment.getDateGraded())) {
            stage = AssignmentStage.TO_GRADE;
        } else if (Constants.isDateSet(assignment.getDateSubmitted())) {
            stage = AssignmentStage.TO_SUBMIT;
        } else {
            stage = AssignmentStage.TO_ASSIGN;
        }

        final Object[] array = AssignmentManager.open(getContext(), assignment, stage);
        if (array == null || array[1] == null) {
            Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Fetching...", Toast.LENGTH_SHORT).show();

            FileDownloadTask task = (FileDownloadTask) array[1];
            task.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Opening...", Toast.LENGTH_SHORT).show();
                    openFile((File) array[0]);
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openFile(File file) {
        String[] mimeTypes = getContext().getResources().getStringArray(R.array.mime_types);

        String auth = BuildConfig.APPLICATION_ID + ".fileprovider";
        Uri uri = FileProvider.getUriForFile(getContext(), auth, file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        boolean openable = false;
        for (String mimeType : mimeTypes) {
            intent.setDataAndType(uri, mimeType);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                openable = true;
                startActivity(intent);
                break;
            }
        }

        if (!openable) {
            Toast.makeText(getContext(), "The file could not be opened", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selected(String selection, Assignment assignment) {
        switch (selection) {
            case Constants.OPT_OPEN:
                openAssignment(assignment);
                break;
            case Constants.OPT_TURNIN:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(getContext(), "Not logged in.", Toast.LENGTH_SHORT).show();
                } else {
                    assignment.setStudentEmail(user.getEmail());
                    assignment.setStudentId(user.getUid());
                    assignment.setStudentName(user.getDisplayName());
                    assignment.setDateSubmitted(Constants.getFormattedDate());

                    Constants.showUploadDialog(getChildFragmentManager(), assignment, AssignmentStage.TO_SUBMIT);
                }
                break;
            case Constants.OPT_GRADE:
                assignment.setDateGraded(Constants.getFormattedDate());
                Constants.showUploadDialog(getChildFragmentManager(), assignment, AssignmentStage.TO_GRADE);
                break;
            case Constants.OPT_ARCHIVE:
                break;
        }
    }
}
