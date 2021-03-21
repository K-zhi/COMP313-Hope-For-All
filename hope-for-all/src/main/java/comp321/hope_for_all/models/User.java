package comp321.hope_for_all.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String uid, userName, name, email;

    public User(){

    }

    public User( String userName, String name, String email) {
        this.userName = userName;
        this.name = name;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("name", name);
        result.put("email", email);

        return result;

    }

}
