package comp321.hope_for_all;

import androidx.annotation.NonNull;
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

import comp321.hope_for_all.activities.MainActivity;
import comp321.hope_for_all.activities.MainGuest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class GuestPostTest {
    @Rule
    public ActivityTestRule<MainGuest> mActivityTestRule =
            new ActivityTestRule<>(MainGuest.class);



    @Test
    public void ViewGuestPosts() {
        onView(withId(R.id.recycler_view));
    }
}