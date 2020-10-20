package dev.wilburomae.tuttracker.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.views.adapters.InboxAdapter;

public class InboxFragment extends Fragment {
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inbox, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.fragment_inbox_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        final InboxAdapter adapter = new InboxAdapter(this, new ArrayList<Assignment>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        List<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < 20; i++) assignments.add(new Assignment());
        adapter.setAssignments(assignments);

        return root;
    }
}
