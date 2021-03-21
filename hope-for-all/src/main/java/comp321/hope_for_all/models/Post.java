package comp321.hope_for_all.models;

import com.google.type.DateTime;

public class Post {

    private String id;
    private String content;


    public Post() {
        //default constructor is needed
    }

    public Post(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
