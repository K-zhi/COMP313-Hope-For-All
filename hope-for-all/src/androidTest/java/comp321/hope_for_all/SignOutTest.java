package comp321.hope_for_all;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp321.hope_for_all.activities.UserProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class SignOutTest {
    @Rule
    public ActivityTestRule<UserProfile> mActivityTestRule =
            new ActivityTestRule<>(UserProfile.class);


    @Test
    public void signoutButtonClick() {
        onView(withId(R.id.tvSignOut))
                .perform(click());
    }

}
