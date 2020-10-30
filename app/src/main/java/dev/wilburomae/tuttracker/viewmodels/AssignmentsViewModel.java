package dev.wilburomae.tuttracker.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import dev.wilburomae.tuttracker.managers.AssignmentManager;
import dev.wilburomae.tuttracker.models.Assignment;

public class AssignmentsViewModel extends ViewModel {
    private final MutableLiveData<List<Assignment>> mAssignmentsLiveData = new MutableLiveData<>();

    public AssignmentsViewModel() {
        mAssignmentsLiveData.setValue(new ArrayList<Assignment>());

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                if (assignment != null) {
                    List<Assignment> assignments = mAssignmentsLiveData.getValue();
                    if (assignments == null) {
                        assignments = new ArrayList<>();
                    }
                    assignments.add(assignment);
                    mAssignmentsLiveData.setValue(assignments);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                if (assignment != null) {
                    List<Assignment> assignments = mAssignmentsLiveData.getValue();
                    if (assignments == null) {
                        assignments = new ArrayList<>();
                    } else {
                        for (int i = 0, booksSize = assignments.size(); i < booksSize; i++) {
                            Assignment b = assignments.get(i);
                            if (b.getId().equals(assignment.getId())) {
                                assignments.set(i, assignment);
                                break;
                            }
                        }
                    }
                    mAssignmentsLiveData.setValue(assignments);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                if (assignment != null) {
                    List<Assignment> assignments = mAssignmentsLiveData.getValue();
                    if (assignments != null) {
                        assignments.remove(assignment);
                        mAssignmentsLiveData.setValue(assignments);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AssignmentManager.fetch(user.getUid(), listener);
        }
    }

    public LiveData<List<Assignment>> getOutboxData() {
        return mAssignmentsLiveData;
    }

    public LiveData<List<Assignment>> getInboxData() {
        return mAssignmentsLiveData;
    }

    public LiveData<List<Assignment>> getArchivesData() {
        return mAssignmentsLiveData;
    }
}
