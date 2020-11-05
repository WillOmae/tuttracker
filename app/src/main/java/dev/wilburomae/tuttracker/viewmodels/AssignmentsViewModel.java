package dev.wilburomae.tuttracker.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.managers.AssignmentManager;
import dev.wilburomae.tuttracker.models.Assignment;

public class AssignmentsViewModel extends ViewModel {
    private final MutableLiveData<List<Assignment>> mOutboxLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Assignment>> mInboxLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Assignment>> mArchivesLiveData = new MutableLiveData<>();

    public AssignmentsViewModel() {
        reset();
    }

    public LiveData<List<Assignment>> getOutboxData() {
        return mOutboxLiveData;
    }

    public LiveData<List<Assignment>> getInboxData() {
        return mInboxLiveData;
    }

    public LiveData<List<Assignment>> getArchivesData() {
        return mArchivesLiveData;
    }

    public void setup(final FirebaseUser user) {
        AssignmentManager.fetch(user.getUid(), new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                if (assignment != null) {
                    if (Constants.isDateSet(assignment.getDateGraded())) {
                        addToLiveData(mArchivesLiveData, assignment);
                    } else if (Constants.isDateSet(assignment.getDateSubmitted())) {
                        addToLiveData(assignment.getTutorId().equals(user.getUid()) ? mInboxLiveData : mOutboxLiveData, assignment);
                    } else if (Constants.isDateSet(assignment.getDateAssigned())) {
                        addToLiveData(assignment.getTutorId().equals(user.getUid()) ? mOutboxLiveData : mInboxLiveData, assignment);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                if (assignment != null) {
                    purgeFromAllLiveData(assignment);

                    if (Constants.isDateSet(assignment.getDateGraded())) {
                        addToLiveData(mArchivesLiveData, assignment);
                    } else if (Constants.isDateSet(assignment.getDateSubmitted())) {
                        addToLiveData(assignment.getTutorId().equals(user.getUid()) ? mInboxLiveData : mOutboxLiveData, assignment);
                    } else if (Constants.isDateSet(assignment.getDateAssigned())) {
                        addToLiveData(assignment.getTutorId().equals(user.getUid()) ? mOutboxLiveData : mInboxLiveData, assignment);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                if (assignment != null) {
                    purgeFromAllLiveData(assignment);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void reset() {
        mOutboxLiveData.setValue(new ArrayList<Assignment>());
        mInboxLiveData.setValue(new ArrayList<Assignment>());
        mArchivesLiveData.setValue(new ArrayList<Assignment>());
    }

    private void addToLiveData(MutableLiveData<List<Assignment>> liveData, Assignment assignment) {
        List<Assignment> assignments = liveData.getValue();
        if (assignments == null) {
            assignments = new ArrayList<>();
        }
        assignments.add(assignment);
        liveData.setValue(assignments);
    }

    private void purgeFromAllLiveData(Assignment assignment) {
        removeFromLiveData(mOutboxLiveData, assignment);
        removeFromLiveData(mInboxLiveData, assignment);
        removeFromLiveData(mArchivesLiveData, assignment);
    }

    private void removeFromLiveData(MutableLiveData<List<Assignment>> liveData, Assignment assignment) {
        List<Assignment> assignments = liveData.getValue();
        if (assignments != null) {
            assignments.remove(assignment);
            liveData.setValue(assignments);
        }
    }
}
