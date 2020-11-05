package dev.wilburomae.tuttracker.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;

public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<Assignment> mAssignments;

    public AssignmentsAdapter(Fragment fragment, List<Assignment> assignments) {
        Context context = fragment.getContext();
        mInflater = LayoutInflater.from(context);
        mAssignments = assignments;
    }

    public List<Assignment> getAssignments() {
        return mAssignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        mAssignments = assignments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.entry_lists, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mAssignments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mHolder;
        TextView mTitle;
        TextView mDescription;
        TextView mDateLabel;
        TextView mDateValue;
        LinearLayout mGradeHolder;
        TextView mGradeValue;
        TextView mGradeMax;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHolder = itemView.findViewById(R.id.entry_lists_holder);
            mTitle = itemView.findViewById(R.id.entry_lists_title);
            mDescription = itemView.findViewById(R.id.entry_lists_description);
            mDateLabel = itemView.findViewById(R.id.entry_lists_date_label);
            mDateValue = itemView.findViewById(R.id.entry_lists_date_value);
            mGradeHolder = itemView.findViewById(R.id.entry_lists_grade);
            mGradeValue = itemView.findViewById(R.id.entry_lists_grade_value);
            mGradeMax = itemView.findViewById(R.id.entry_lists_grade_max);
        }
    }
}
