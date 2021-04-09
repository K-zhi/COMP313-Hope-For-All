package comp321.hope_for_all;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp321.hope_for_all.activities.MainCounselor;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CounselorDeletePostTest {
    @Rule
    public ActivityTestRule<MainCounselor> mActivityTestRule =
            new ActivityTestRule<>(MainCounselor.class);


    @Before
    public void Prepare() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("navjot@gmail.com", "Navjot00")
                .addOnCompleteListener(mActivityTestRule.getActivity(), new OnCompleteListener<AuthResult>() {
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
    public void DeleteCounselerPost(UiController uiController, View view)
    {

        View del = view.findViewById(R.id.delete_post);
        del.performClick();
    }


}