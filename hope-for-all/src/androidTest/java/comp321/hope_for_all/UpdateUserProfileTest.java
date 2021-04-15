package comp321.hope_for_all;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp321.hope_for_all.activities.MainActivity;


import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.TestCase;

import org.hamcrest.Matcher;

import comp321.hope_for_all.activities.MainActivity;
import comp321.hope_for_all.activities.UpdateUserProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UpdateUserProfileTest{

    @Rule
    public ActivityTestRule<UpdateUserProfile> updateUserProfileActivityTestRule = new ActivityTestRule<>(UpdateUserProfile.class);



    @Before
    public void Prepare() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("maheshwarivrana@gmail.com", "mahi@2012")
                .addOnCompleteListener(updateUserProfileActivityTestRule.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                        }
                        else {
                        }
                    }
                });
    }

    @Test
    public void updateUser() throws Throwable{




        ViewAction cancelUserName = new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View btncancel = view.findViewById(R.id.btnCancel);
                btncancel.performClick();
            }
        };



        onView(withId(R.id.update_userName)).perform(typeText("Maheshwari User - Test"));
        onView(withId(R.id.update_userName)).perform(closeSoftKeyboard());
        onView(withId(R.id.update_name)).perform(typeText("Maheshwari User Name-Test"));
        onView(withId(R.id.update_name)).perform(closeSoftKeyboard());
        onView(withId(R.id.update_email)).perform(typeText("Maheshwari User Email - Test"));

        onView(withId(R.id.update_email)).perform(closeSoftKeyboard());
        onView(withId(R.id.btnSave)).perform(click());


    }

}


