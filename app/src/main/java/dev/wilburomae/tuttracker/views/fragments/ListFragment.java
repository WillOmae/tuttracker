package dev.wilburomae.tuttracker.views.fragments;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.managers.AssignmentManager;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.listeners.IPickerDialogSelection;

public class ListFragment extends Fragment implements IPickerDialogSelection {
    private void openAssignment(Assignment assignment) {
        AssignmentManager.open(assignment);
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
