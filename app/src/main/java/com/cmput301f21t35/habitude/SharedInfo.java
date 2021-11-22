package com.cmput301f21t35.habitude;



/**
 * SharedInfo is a singleton class which will give all allow all the activities to retrieve
 * and set common application information
 */
public class SharedInfo {
    private Users currentUser;

    // private constructor: no one from outside is allowed to invoke this
    private SharedInfo() { }

    // enforce singleton pattern
    private static SharedInfo sharedInfo;

    /**
     * Gets the SharedInfo instance
     * @return      {@code SharedInfo} singleton shared info instance
     */
    public static SharedInfo getInstance() {
        if (sharedInfo == null) {
            sharedInfo = new SharedInfo();
        }
        return sharedInfo;
    }

    /**
     * Sets the current user for the application
     * @param user      {@code User} the current user
     */
    public void setCurrentUser(Users user) {
        currentUser = user;
    }

    /**
     * Gets the current user for the application
     * @return          {@code User} the current user
     */
    public Users getCurrentUser() {
        return currentUser;
    }

    public void clearCurrentUser() {
        currentUser.clearUsername();
    }
}
