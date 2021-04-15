package comp321.hope_for_all;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comp321.hope_for_all.activities.LoginUser;

public class DisplayLoginTest extends TestCase {

    @Rule
    public ActivityTestRule<LoginUser> loginUserActivityTestRule = new ActivityTestRule<LoginUser>(LoginUser.class);
    private LoginUser logUser = null;

    @Before
    public void setUp() throws Exception {
        logUser = loginUserActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = logUser.findViewById(R.id.userLoginLayout);
        assertNotNull(view);
    }

    public void tearDown() throws Exception {
        logUser = null;
    }
}