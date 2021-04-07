package comp321.hope_for_all;

import android.view.View;

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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comp321.hope_for_all.activities.UpdateCounselorProfile;
import comp321.hope_for_all.activities.UpdateUserProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UpdateCounselorProfileTest {

    @Rule
    public ActivityTestRule<UpdateCounselorProfile> updateCounselorProfileActivityTestRule = new ActivityTestRule<>(UpdateCounselorProfile.class);

    @Before
    public void Prepare() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("maheshwari@gmail.com", "mahi@2012")
                .addOnCompleteListener(updateCounselorProfileActivityTestRule.getActivity(), new OnCompleteListener<AuthResult>() {
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
    public void updateCounsellor() throws Throwable{

        ViewAction cancelCounselorName = new ViewAction() {
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

        onView(withId(R.id.etName)).perform(typeText("MaheshwariCounselorTest"));
        onView(withId(R.id.etName)).perform(closeSoftKeyboard());

        onView(withId(R.id.etBio)).perform(typeText("Maheshwari Bio Test"));
        onView(withId(R.id.etBio)).perform(closeSoftKeyboard());
        onView(withId(R.id.etEmail)).perform(typeText("Maheshwari Email Test"));
        onView(withId(R.id.etEmail)).perform(closeSoftKeyboard());
        onView(withId(R.id.etWebsite)).perform(typeText("www.Maheshwari-Test.com"));
        onView(withId(R.id.etWebsite)).perform(closeSoftKeyboard());
        onView(withId(R.id.etLocation)).perform(typeText("Maheshwari Test Loctation"));

        onView(withId(R.id.etLocation)).perform(closeSoftKeyboard());

        onView(withId(R.id.btnSave)).perform(click());

    }


}