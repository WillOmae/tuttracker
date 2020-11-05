package dev.wilburomae.tuttracker.views.adapters;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;

public class InboxAdapter extends AssignmentsAdapter {
    public InboxAdapter(Fragment fragment, List<Assignment> assignments) {
        super(fragment, assignments);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Assignment assignment = getAssignments().get(position);
        holder.mTitle.setText(assignment.getTitle());
        holder.mDescription.setText(assignment.getDescription());
        holder.mGradeMax.setText(String.valueOf(assignment.getGradeMax()));
        holder.mGradeValue.setText("N/A");
        if (Constants.isDateSet(assignment.getDateSubmitted())) {
            holder.mDateLabel.setText(R.string.dialog_upload_date_submitted);
            holder.mDateValue.setText(assignment.getDateSubmitted());

            holder.mHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Toast.makeText(getContext(), "Not logged in.", Toast.LENGTH_SHORT).show();
                    } else {
                        assignment.setDateGraded(Constants.getFormattedDate());

                        Constants.showUploadDialog(getFragmentManager(), assignment, AssignmentStage.TO_GRADE);
                    }
                }
            });
        } else {
            holder.mDateLabel.setText(R.string.dialog_upload_date_due);
            holder.mDateValue.setText(assignment.getDateDue());

            holder.mHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Toast.makeText(getContext(), "Not logged in.", Toast.LENGTH_SHORT).show();
                    } else {
                        assignment.setStudentEmail(user.getEmail());
                        assignment.setStudentId(user.getUid());
                        assignment.setStudentName(user.getDisplayName());
                        assignment.setDateSubmitted(Constants.getFormattedDate());

                        Constants.showUploadDialog(getFragmentManager(), assignment, AssignmentStage.TO_SUBMIT);
                    }
                }
            });
        }
    }
}
