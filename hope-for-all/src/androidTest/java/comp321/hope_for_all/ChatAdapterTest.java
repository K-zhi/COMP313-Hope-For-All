package comp321.hope_for_all;

import android.app.Activity;
import android.content.Context;

import androidx.constraintlayout.utils.widget.MockView;
import androidx.test.core.app.ApplicationProvider;

import com.google.errorprone.annotations.DoNotMock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp321.hope_for_all.activities.Chat;
import comp321.hope_for_all.adapter.ChatAdapter;
import comp321.hope_for_all.models.ChatData;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.*;

public class ChatAdapterTest {
    private static final String TAG = "CHAT TEST";
    @Mock
    private Context context;
    private ChatAdapter chatAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addGridItemsToViewNotifiesParentAndAddsItemToList() {
        chatAdapter = new ChatAdapter((Activity) context);

        //doNothing().when(chatAdapter).internalNotifyItemInserted(anyInt());
        ChatData tempData = new ChatData();
        tempData.setMsg("TEST MESSAGE");
        tempData.setUid("TEST USER ID");
        tempData.setOpponentId("TEST OPP ID");

        doNothing().when(chatAdapter).notifyItemInserted(anyInt());
        chatAdapter.addChat(tempData);
        verify(chatAdapter).notifyItemInserted(0);
    }

    @After
    public void tearDown() throws Exception {
    }
}