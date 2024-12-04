package com.example.planetze;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class LoginPresenter {

    private final LoginView view;
    private final LoginModel model;

    public LoginPresenter(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    public void checkUserLoggedIn() {
        FirebaseUser currentUser = model.getCurrentUser();
        if (currentUser != null) {
            view.navigateToMain();
        }
    }

    public void performLogin(String email, String password) {
        view.showProgress();

        // Sign in the user
        model.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideProgress();
                    if (task.isSuccessful()) {
                        checkEmailVerification();
                    } else {
                        view.showToast("Login failed: " + task.getException().getMessage());
                    }
                });
    }

    private void checkEmailVerification() {
        FirebaseUser user = model.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                checkFirstSurveyStatus(user);
            } else {
                model.signOut();
                view.showToast("Please verify your email.");
            }
        }
    }

    private void checkFirstSurveyStatus(FirebaseUser user) {
        model.checkFirstSurveyStatus(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String value = String.valueOf(dataSnapshot.getValue());
                        if ("false".equals(value)) {
                            model.updateFirstSurveyStatus(user)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            view.navigateToQuiz();
                                        } else {
                                            view.showToast("Error updating survey status: " + updateTask.getException().getMessage());
                                        }
                                    });
                        } else {
                            view.navigateToMain();
                        }
                    } else {
                        view.showToast("Error checking survey status: " + task.getException().getMessage());
                    }
                });
    }
}
