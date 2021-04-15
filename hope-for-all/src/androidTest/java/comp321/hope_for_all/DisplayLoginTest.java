package comp321.hope_for_all;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp321.hope_for_all.activities.LoginUser;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DisplayLoginTest {
    @Rule
    public ActivityTestRule<LoginUser> mActivityTestRule =
            new ActivityTestRule<>(LoginUser.class);



    @Test
    public void DisplayLoginTest() {
        onView(withId(R.id.userLoginLayout));
    }
}