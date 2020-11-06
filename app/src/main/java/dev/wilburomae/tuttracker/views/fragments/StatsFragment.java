package dev.wilburomae.tuttracker.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.viewmodels.AssignmentsViewModel;
import dev.wilburomae.tuttracker.views.MainActivity;

public class StatsFragment extends Fragment {
    private AssignmentsViewModel mAssignmentsViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        MainActivity mainActivity = (MainActivity) getActivity();
        mAssignmentsViewModel = mainActivity.getAssignmentsViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        final TextView assigned1 = root.findViewById(R.id.fragment_stats_assigned_1);
        final TextView graded1 = root.findViewById(R.id.fragment_stats_graded_1);
        final TextView assigned2 = root.findViewById(R.id.fragment_stats_assigned_2);
        final TextView graded2 = root.findViewById(R.id.fragment_stats_graded_2);
        final TextView submitted2 = root.findViewById(R.id.fragment_stats_submitted_2);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (mAssignmentsViewModel != null && user != null) {
            mAssignmentsViewModel.getCompleteData().observe(getViewLifecycleOwner(), new Observer<List<Assignment>>() {
                @Override
                public void onChanged(List<Assignment> assignments) {
                    int iAssigned1 = 0, iAssigned2 = 0, iGraded1 = 0, iGraded2 = 0, iSubmitted2 = 0;
                    for (Assignment assignment : assignments) {
                        if (assignment.getTutorEmail().equals(user.getEmail()) && Constants.isDateSet(assignment.getDateGraded())) {
                            iGraded1++;
                        }
                        if (assignment.getStudentEmail().equals(user.getEmail()) && Constants.isDateSet(assignment.getDateGraded())) {
                            iGraded2++;
                        }
                        if (assignment.getStudentEmail().equals(user.getEmail()) && Constants.isDateSet(assignment.getDateSubmitted())) {
                            iSubmitted2++;
                        }
                        if (assignment.getTutorEmail().equals(user.getEmail()) && Constants.isDateSet(assignment.getDateAssigned())) {
                            iAssigned1++;
                        }
                        if (assignment.getStudentEmail().equals(user.getEmail()) && Constants.isDateSet(assignment.getDateAssigned())) {
                            iAssigned2++;
                        }
                    }
                    setData(assigned1, iAssigned1);
                    setData(assigned2, iAssigned2);
                    setData(graded1, iGraded1);
                    setData(graded2, iGraded2);
                    setData(submitted2, iSubmitted2);
                }
            });
        }

        return root;
    }

    private void setData(TextView view, int data) {
        view.setText(String.valueOf(data));
    }
}
