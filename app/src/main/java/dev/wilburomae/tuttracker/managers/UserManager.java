package dev.wilburomae.tuttracker.managers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserManager {
    public static DatabaseReference FetchUser(String id) {
        return FirebaseDatabase.getInstance().getReference().child("users").child(id);
    }
}
