package kristine.pacleb.hope_for_all.activity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public int userId;
    public String userName;
    public String userEmail;
    public String userPassword;
    public String userType;

    public User() {}

    public User(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }

    public User(String email, String password, String userType) {
        this.userEmail = email;
        this.userPassword = password;
        this.userType = userType;
    }

    public User(String userName, String userEmail, String userPassword, String userType) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userType = userType;
    }

    public User(int userId, String userName, String userEmail, String userPassword, String userType) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userType = userType;
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

    public String getUserEmail() {
        return  userEmail;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public String getUserPassword() {
        return  userPassword;
    }

    public void setUserPassword(String password) {
        this.userPassword = password;
    }

    public String getUserType() {
        return  userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName='" + userName + '\'' +
                ", email='" + userEmail + '\'' +
                "}";
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("userPassword", userPassword);
        result.put("userType", userType);

        return result;
    }
}
