package kristine.pacleb.hope_for_all.models;

public class User {

    public String userName, name, email;

    public User(){

    }

    public User(String userName, String name, String email) {
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

}
