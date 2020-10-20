package dev.wilburomae.tuttracker.models;

public class User {
    private String mUid;
    private String mName;
    private String mEmail;

    public User() {
    }

    public User(String uid, String name, String email) {
        mUid = uid;
        mName = name;
        mEmail = email;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}
