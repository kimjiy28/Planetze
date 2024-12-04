package com.example.planetze;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.DoNotMock;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

/**
 * Instrumented test, which will execute on an Android device.
 *
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleInstrumentedTest {
    @Mock
    private LoginView mockView;

    @Mock
    private LoginModel mockModel;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private FirebaseUser mockUser;

    @Mock
    private DataSnapshot mockDataSnapshot;

    @Mock
    Task<DataSnapshot> mockDataSnapshotTask;

    @Mock
    private LoginPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        presenter = new LoginPresenter(mockView, mockModel);
    }

    @Test
    public void checkUserLoggedIn_userExistsAndNavToMain() {
        when(mockModel.getCurrentUser()).thenReturn(mockUser);
        presenter.checkUserLoggedIn();
        verify(mockView).navigateToMain();
    }

    @Test
    public void checkUserLoggedIn_userDontExists() {
        when(mockModel.getCurrentUser()).thenReturn(null);
        presenter.checkUserLoggedIn();
        verify(mockView, never()).navigateToMain();
    }

    @Test
    public void performLogin_successNavtoQuiz() {
        when(mockModel.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.isEmailVerified()).thenReturn(true);

        // successful login mock
        when(mockVoidTask.isSuccessful()).thenReturn(true);
        when(mockModel.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mockVoidTask);

        // Mock survey status check task
        when(mockDataSnapshotTask.isSuccessful()).thenReturn(true); //task successful
        when(mockDataSnapshotTask.getResult()).thenReturn(mockDataSnapshot); // when successful return a snapshot
        when(mockDataSnapshot.getValue()).thenReturn("false"); // get snapshot to return false
        when(mockModel.checkFirstSurveyStatus(mockUser)).thenReturn(mockDataSnapshotTask); //survey check

        // success mock for survey status update
        when(mockVoidTask.isSuccessful()).thenReturn(true);
        when(mockModel.updateFirstSurveyStatus(mockUser)).thenReturn(mockVoidTask);

        // get presenter to do mocks
        presenter.performLogin("test@example.com", "password");

        // verify behaviour
        verify(mockView).showProgress();
        verify(mockView).hideProgress();
        verify(mockView).navigateToQuiz();
    }

    @Test
    public void performLogin_failShowToast() {
        // Arrange
        when(mockVoidTask.isSuccessful()).thenReturn(false);
        when(mockVoidTask.getException()).thenReturn(new Exception("Login error"));
        when(mockModel.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mockVoidTask);

        // get presenter to run mocks
        presenter.performLogin("test@example.com", "passwor3");

        // verify behaviour
        verify(mockView).showProgress();
        verify(mockView).hideProgress();
        verify(mockView).showToast("Login failed: Login error");
    }

    @Test
    public void performLogin_emailNotVerifiedToast(){
        // user exists but the email is not verified
        when(mockModel.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.isEmailVerified()).thenReturn(false);

        // success login mock
        when(mockVoidTask.isSuccessful()).thenReturn(true);
        when(mockModel.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mockVoidTask);

        // get presenter to run mocks
        presenter.performLogin("test@example.com", "password");

        // verify behaviour
        verify(mockView).showProgress();
        verify(mockView).hideProgress();
        verify(mockModel).signOut();
        verify(mockView).showToast("Please verify your email.");
    }


}