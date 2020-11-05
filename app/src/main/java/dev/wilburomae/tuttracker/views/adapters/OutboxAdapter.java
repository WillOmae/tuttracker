package dev.wilburomae.tuttracker.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;

public class OutboxAdapter extends AssignmentsAdapter {
    public OutboxAdapter(Fragment fragment, List<Assignment> assignments) {
        super(fragment, assignments);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignment assignment = getAssignments().get(position);
        holder.mTitle.setText(assignment.getTitle());
        holder.mDescription.setText(assignment.getDescription());
        holder.mGradeMax.setText(String.valueOf(assignment.getGradeMax()));
        holder.mGradeValue.setText("N/A");
        if (Constants.isDateSet(assignment.getDateSubmitted())) {
            holder.mDateLabel.setText(R.string.dialog_upload_date_submitted);
            holder.mDateValue.setText(assignment.getDateSubmitted());
        } else {
            holder.mDateLabel.setText(R.string.dialog_upload_date_due);
            holder.mDateValue.setText(assignment.getDateDue());
        }
    }
}
