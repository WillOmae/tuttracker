package dev.wilburomae.tuttracker.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;

public class ArchiveAdapter extends AssignmentsAdapter {
    public ArchiveAdapter(Fragment fragment, List<Assignment> assignments) {
        super(fragment, assignments);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignment assignment = getAssignments().get(position);
        holder.mTitle.setText(assignment.getTitle());
        holder.mDescription.setText(assignment.getDescription());
        holder.mGradeMax.setText(String.valueOf(assignment.getGradeMax()));
        holder.mGradeValue.setText(String.valueOf(assignment.getGradeScored()));
        holder.mDateLabel.setText(R.string.dialog_upload_date_graded);
        holder.mDateValue.setText(assignment.getDateGraded());
    }
}