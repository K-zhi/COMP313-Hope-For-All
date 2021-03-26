package comp321.hope_for_all.models;

import android.net.Uri;

public class Counselor {

    public String c_userName, c_name, c_bio, c_email, c_website, c_location;

    public Counselor(){

    }

    public Counselor(String c_userName, String c_name, String c_bio, String c_email, String c_website, String c_location) {
        this.c_userName = c_userName;
        this.c_name = c_name;
        this.c_bio = c_bio;
        this.c_email = c_email;
        this.c_website = c_website;
        this.c_location = c_location;
    }


    public String getC_userName() {
        return c_userName;
    }

    public String getC_name() {
        return c_name;
    }

    public String getC_bio() {
        return c_bio;
    }

    public String getC_email() {
        return c_email;
    }

    public String getC_website() {
        return c_website;
    }

    public String getC_location() {
        return c_location;
    }
}
