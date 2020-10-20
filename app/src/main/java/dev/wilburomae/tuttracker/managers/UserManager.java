package dev.wilburomae.tuttracker.managers;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dev.wilburomae.tuttracker.models.User;

public class UserManager {
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference dbUsers = database.child("users");

    public static DatabaseReference FetchUser(String id) {
        return dbUsers.child(id);
    }

    public static void CreateUser(FirebaseUser firebaseUser) {
        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
        dbUsers.child(user.getUid()).setValue(user);
    }
}
