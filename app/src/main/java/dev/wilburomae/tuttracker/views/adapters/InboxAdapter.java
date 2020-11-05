package dev.wilburomae.tuttracker.views.adapters;

import android.os.Bundle;
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
import dev.wilburomae.tuttracker.views.dialogs.UploadDialog;

public class InboxAdapter extends AssignmentsAdapter {
    public InboxAdapter(Fragment fragment, List<Assignment> assignments) {
        super(fragment, assignments);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Assignment assignment = getAssignments().get(position);
        holder.mTitle.setText(assignment.getTitle());
        holder.mDescription.setText(assignment.getDescription());
        holder.mGradeHolder.setVisibility(View.GONE);
        if (Constants.isDateSet(assignment.getDateSubmitted())) {
            holder.mDateLabel.setText(R.string.date_submitted);
            holder.mDateValue.setText(assignment.getDateSubmitted());

            holder.mHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Toast.makeText(getContext(), "Not logged in.", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle args = new Bundle();
                        args.putSerializable("assignment", assignment);
                        args.putSerializable("stage", AssignmentStage.TO_GRADE);

                        UploadDialog dialogFragment = new UploadDialog();
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentManager(), UploadDialog.class.getName());
                    }
                }
            });
        } else {
            holder.mDateLabel.setText(R.string.date_due);
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

                        Bundle args = new Bundle();
                        args.putSerializable("assignment", assignment);
                        args.putSerializable("stage", AssignmentStage.TO_SUBMIT);

                        UploadDialog dialogFragment = new UploadDialog();
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentManager(), UploadDialog.class.getName());
                    }
                }
            });
        }
    }
}
