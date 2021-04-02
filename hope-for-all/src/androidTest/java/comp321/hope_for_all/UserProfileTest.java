package comp321.hope_for_all;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp321.hope_for_all.activities.UserProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UserProfileTest {

    @Rule
    public ActivityTestRule<UserProfile> mActivityTestRule =
            new ActivityTestRule<>(UserProfile.class);

    @Test
    public void deleteButtonClick(){

        onView(withId(R.id.btnDeleteProfile)).check(matches(isCompletelyDisplayed()));

        int titleId = mActivityTestRule.getActivity()
                .getResources()
                .getIdentifier("alertTitle", "id", "android");

        onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .check(matches(withText(R.string.dialog_no)))
                .check(matches(isCompletelyDisplayed()));

        onView(withId(android.R.id.button2))
                .perform(click());
    }
}
