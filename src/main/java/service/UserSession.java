package service;

import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

public class UserSession {

    private static UserSession instance; //create a variable with the type of the class

    private String userName; //variable for userName

    private String password; //variable for password
    private String privileges; //variable for privileges

    //assigns the user data to private members, and then stores them in preferences
    //ensures that this class only has one instance, singleton pattern.
    private UserSession(String userName, String password, String privileges) {
        this.userName = userName;
        this.password = password;
        this.privileges = privileges;
        //Preferences API provides a way to store and retrieve user and system preference data.  Commonly used to persist small amounts of data.
        //userRoot() returns the root preference node for the calling user
        //userPreferences now holds a reference to the root node of the user's prefernces.
        //userPreferences object can store preferences for the current user, such as their username, password.
        Preferences userPreferences = Preferences.userRoot();
        //creates a new key value pair if one does not exist, if one does exist, then it updates the value associated with the key
        userPreferences.put("USERNAME",userName); //assign userName to userPreferences object.  USERNAME is the key, and userName is the value
        userPreferences.put("PASSWORD",password); //assign password to userPreferences object. PASSWORD is the key, and password is the value
        userPreferences.put("PRIVILEGES",privileges); //assign privileges to userPreferences object. PRIVILEGES is the key, and privileges is the value
    }

    //the preferences are stored in the Windows registry

    //getInstance is a global access point to the one instance of this class. It is an overloaded method. This version is used if user has privileges
    public static UserSession getInstace(String userName,String password, String privileges) {
        if(instance == null) { //checks if an instance with userName, password, and privileges already exists
            instance = new UserSession(userName, password, privileges); //if an instance exists, then assign it to instance
        }
        return instance; //return the instance for the user
    }

    //getInstance is a global access point to the one instance of this class. It is an overloaded method. This version is used if user does not have privileges
    public static UserSession getInstace(String userName,String password) {
        if(instance == null) { //checks if an instance with userName, password, and privileges already exists
            instance = new UserSession(userName, password, "NONE"); //if an instance exists, then assign it to instance
        }
        return instance; //return the instance for the user
    }
    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPrivileges() {
        return this.privileges;
    }


    //clears the user session data by resetting the userName, password, and privileges
    //synchronized makes this method thread safe
    //this method can be used when the user logs out, or when the session needs to be reset, perhaps after a time of inactivity.
    public synchronized void cleanUserSession() {
        this.userName = "";// or null
        this.password = "";
        this.privileges = "";// or null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + this.userName + '\'' +
                ", privileges=" + this.privileges +
                '}';
    }
}
