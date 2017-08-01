package codepath.com.goingout.models;

import org.parceler.Parcel;

/**
 * Created by joh on 7/27/17.
 */

@Parcel
public class User {
    private String firstName;
    private String lastName;

    public User(){

    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
