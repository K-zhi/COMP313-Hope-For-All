package comp321.hope_for_all;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
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

import comp321.hope_for_all.activities.MainCounselor;

@RunWith(AndroidJUnit4.class)
public class CommentTestCounselor {
    @Rule
    public ActivityTestRule<MainCounselor> mainActivityActivityTestRule = new ActivityTestRule(MainCounselor.class);

    @Before
    public void Prepare() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("counselor@unit.test", "unittest")
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
    public void CommentTest() {
        ViewAction postComment = new ViewAction() {
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
                View btnPost = view.findViewById(R.id.comment_post);
                btnPost.performClick();
            }
        };
        ViewAction cancelComment = new ViewAction() {
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
                View btncancel = view.findViewById(R.id.cancel_comment);
                btncancel.performClick();
            }
        };
        ViewAction confirmComment = new ViewAction() {
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
                View btnConfirm = view.findViewById(R.id.confirm_comment);
                btnConfirm.performClick();
            }
        };
        ViewAction editComment = new ViewAction() {
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
                EditText edit = view.findViewById(R.id.edittext_comment);
                edit.setText("CommentTest!@#$%^&*()");
            }
        };

        ViewInteraction recyclerViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.recycler_view));
        recyclerViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(0, postComment));
        recyclerViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(0, cancelComment));
        recyclerViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(0, postComment));
        recyclerViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(0, editComment));
        recyclerViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(0, confirmComment));
    }
}
