package comp321.hope_for_all;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.DoNotMock;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp321.hope_for_all.activities.Chat;
import comp321.hope_for_all.activities.UpdateCounselorProfile;
import comp321.hope_for_all.adapter.ChatAdapter;
import comp321.hope_for_all.models.ChatData;
import comp321.hope_for_all.models.User;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ChatAdapterTest {
    @Rule
    public ActivityTestRule<Chat> ChatActivityTestRule = new ActivityTestRule<>(Chat.class);
    public User testUser;
    private String uid;
    private String uName;
    private String oppId;
    private String oppName;
    private String chatKey;
    private List<ChatData> testList;
    String message;

    @Before
    public void Prepare() {
        uid = "TESTUSER1";
        uName = "TestCHRIS";
        oppId = "TESTUSER2";
        oppName = "TestKim";
        chatKey = "TESTUSER1TESTUSER2";
        testList = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("kim@gmail.com", "123456")
                .addOnCompleteListener(ChatActivityTestRule.getActivity(), new OnCompleteListener<AuthResult>() {
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
    public void chatCounsellor() throws Throwable{
        message = "First Testing chat with counselor";
        onView(withId(R.id.editTxtChat)).perform(typeText(message));
        onView(withId(R.id.editTxtChat)).perform(closeSoftKeyboard());
        onView(withId(R.id.btnSendChat)).perform(click());

        onView(withId(R.id.editTxtChat)).perform(typeText(""));
        onView(withId(R.id.editTxtChat)).perform(closeSoftKeyboard());

        ViewInteraction recyclerViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.chatRoomRecyclerView));
    }

    public void sendChat(String message) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        String strNow = sdfNow.format(date);

        ChatData chat = new ChatData();
        chat.setUid(uid);
        chat.setUserName(uName);
        chat.setOpponentId(oppId);
        chat.setOpponentName(oppName);
        chat.setMsg(message);
        chat.setDate(strNow);

        if(chatKey != null)
            FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(chatKey).push().setValue(chat);
    }
}