package com.example.planetze;

public interface LoginView {
    void showProgress();

    void hideProgress();

    void showToast(String message);

    void navigateToMain();

    void navigateToQuiz();
}
