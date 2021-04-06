package comp321.hope_for_all;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import comp321.hope_for_all.models.ChatData;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ChatRoomKeyValidatorTest {
    private FirebaseDatabase database;
    private List<ChatData> listChatRooms;
    private String chatGroupKey;

    @Before
    public void setDatabase() {
        database = FirebaseDatabase.getInstance();

    }

    @Test
    public void test() {

    }
}
