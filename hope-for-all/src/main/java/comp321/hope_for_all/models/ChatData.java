package comp321.hope_for_all.models;

import java.io.Serializable;

public class ChatData implements Serializable {
    private String msg;
    private String nickname;
    // Add
    private int image;
    private String opponentName;
    private String time;
    private String id;

    public ChatData() {}

    public ChatData(int image, String opponentName, String msg) {
        this.image = image;
        this.opponentName = opponentName;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String nickname) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
