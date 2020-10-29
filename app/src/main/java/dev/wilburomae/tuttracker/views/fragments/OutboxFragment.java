package dev.wilburomae.tuttracker.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.managers.AssignmentManager;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.adapters.OutboxAdapter;
import dev.wilburomae.tuttracker.views.dialogs.UploadDialog;
import dev.wilburomae.tuttracker.views.listeners.IUploadListener;

public class OutboxFragment extends Fragment implements IUploadListener {
    private Context mContext;
    private FirebaseUser mUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_outbox, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fragment_outbox_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assignment assignment = new Assignment();
                assignment.setTutorEmail(mUser.getEmail());
                assignment.setTutorId(mUser.getUid());
                assignment.setTutorName(mUser.getDisplayName());

                Bundle args = new Bundle();
                args.putSerializable("assignment", assignment);
                args.putSerializable("stage", AssignmentStage.TO_ASSIGN);

                UploadDialog dialogFragment = new UploadDialog();
                dialogFragment.setArguments(args);
                dialogFragment.show(getChildFragmentManager(), UploadDialog.class.getName());
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.fragment_outbox_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        final OutboxAdapter adapter = new OutboxAdapter(this, new ArrayList<Assignment>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        List<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < 6; i++) assignments.add(new Assignment());
        adapter.setAssignments(assignments);

        return root;
    }

    @Override
    public void upload(Assignment assignment, Uri fileUri) {
        Toast.makeText(mContext, "Upload started...", Toast.LENGTH_SHORT).show();
        UploadTask task = AssignmentManager.upload(assignment, fileUri, AssignmentStage.TO_ASSIGN);
        if (task != null) {
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(mContext, "Upload complete!", Toast.LENGTH_SHORT).show();
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
