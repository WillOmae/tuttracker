package dev.wilburomae.tuttracker.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.viewmodels.AssignmentsViewModel;
import dev.wilburomae.tuttracker.views.MainActivity;
import dev.wilburomae.tuttracker.views.adapters.OutboxAdapter;
import dev.wilburomae.tuttracker.views.dialogs.UploadDialog;

public class OutboxFragment extends Fragment {
    private Context mContext;
    private AssignmentsViewModel mAssignmentsViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
        MainActivity mainActivity = (MainActivity) getActivity();
        mAssignmentsViewModel = mainActivity.getAssignmentsViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_outbox, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fragment_outbox_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(mContext, "Not logged in.", Toast.LENGTH_SHORT).show();
                } else {
                    Assignment assignment = new Assignment();
                    assignment.setId(UUID.randomUUID().toString());
                    assignment.setTutorEmail(user.getEmail());
                    assignment.setTutorId(user.getUid());
                    assignment.setTutorName(user.getDisplayName());
                    assignment.setDateAssigned(Constants.getFormattedDate());

                    Bundle args = new Bundle();
                    args.putSerializable("assignment", assignment);
                    args.putSerializable("stage", AssignmentStage.TO_ASSIGN);

                    UploadDialog dialogFragment = new UploadDialog();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getChildFragmentManager(), UploadDialog.class.getName());
                }
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.fragment_outbox_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        final OutboxAdapter adapter = new OutboxAdapter(this, new ArrayList<Assignment>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (mAssignmentsViewModel != null) {
            mAssignmentsViewModel.getOutboxData().observe(getViewLifecycleOwner(), new Observer<List<Assignment>>() {
                @Override
                public void onChanged(List<Assignment> assignments) {
                    adapter.setAssignments(assignments);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        return root;
    }
}
