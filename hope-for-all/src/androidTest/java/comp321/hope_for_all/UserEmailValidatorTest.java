package comp321.hope_for_all;

import androidx.test.rule.ActivityTestRule;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Rule;
import org.junit.Test;

import comp321.hope_for_all.activities.LoginUser;


public class UserEmailValidatorTest {
    @Rule
    public ActivityTestRule<LoginUser> mainActivityActivityTestRule = new ActivityTestRule(LoginUser.class);

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat(LoginUser.isValidEmail("kev@gmail.com")).isTrue();
    }
}
