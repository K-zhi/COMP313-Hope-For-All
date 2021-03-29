package comp321.hope_for_all.models;

import java.io.Serializable;
import java.util.Date;

public class ChatData implements Serializable {
    private String msg;
    private String userName;
    private String uid;
    private int image;
    // Add
    private String rookKey;
    private String opponentName;
    private String opponentId;
    private String date;

    public ChatData() {}

    public ChatData(int image, String opponentName, String msg, String date) {
        this.image = image;
        this.opponentName = opponentName;
        this.msg = msg;
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
