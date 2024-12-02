package com.example.planetze;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginModel {

    private final FirebaseAuth mAuth;
    private final DatabaseReference databaseReference;

    public LoginModel() {
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Task<DataSnapshot> checkFirstSurveyStatus(FirebaseUser user) {
        return databaseReference.child(user.getUid()).child("doneFirstSurvey").get();
    }

    public Task<Void> updateFirstSurveyStatus(FirebaseUser user) {
        return databaseReference.child(user.getUid()).child("doneFirstSurvey").setValue("true");
    }

    public void signOut() {
        mAuth.signOut();
    }
}

