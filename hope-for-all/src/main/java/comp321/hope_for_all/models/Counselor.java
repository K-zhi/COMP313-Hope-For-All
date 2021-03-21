package comp321.hope_for_all.models;

public class Counselor {

    public String c_userName, c_name, c_bio, c_email, c_website, c_location;

    public Counselor() {
    }

    public Counselor(String str_cUserName, String str_cName, String str_bio, String str_cEmail,
                     String str_cWebsite, String str_cLocation) {
        this.c_userName = str_cUserName;
        this.c_name = str_cName;
        this.c_bio = str_bio;
        this.c_email = str_cEmail;
        this.c_website = str_cWebsite;
        this.c_location = str_cLocation;

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
