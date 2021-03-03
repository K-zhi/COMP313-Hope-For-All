package hope_for_all_data;

public class GuestUser {
    public int userId;
    public String userName;

    public  GuestUser() {}

    public GuestUser(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return  userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
