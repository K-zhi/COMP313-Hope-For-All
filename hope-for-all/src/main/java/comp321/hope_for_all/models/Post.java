package comp321.hope_for_all.models;

import com.google.type.DateTime;

public class Post {
    private String userName;
    private String content;
    private DateTime date;


    public Post() {
        //default constructor is needed
    }

    public Post(String userName, String content, DateTime date) {
        this.userName = userName;
        this.content = content;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public DateTime getDate() {
        return date;
    }
}
