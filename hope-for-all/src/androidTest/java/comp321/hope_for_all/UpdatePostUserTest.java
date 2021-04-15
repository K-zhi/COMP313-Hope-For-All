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

@RunWith(AndroidJUnit4.class)
public class UpdatePostUserTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void Prepare() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("mkpacleb@mail.com", "123456")
            .addOnCompleteListener(mainActivityActivityTestRule.getActivity(), new OnCompleteListener<AuthResult>() {
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
    public void EditPost() {

        ViewAction ediPost = new ViewAction() {
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
                View btnPost = view.findViewById(R.id.et_post);
                btnPost.performClick();
            }
        };
        ViewAction editPost = new ViewAction() {
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
                EditText edit = view.findViewById(R.id.edit_post);
                edit.setText("Updating my post from User");
            }
        };
        ViewInteraction recyclerViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.recycler_view));
    }
}
