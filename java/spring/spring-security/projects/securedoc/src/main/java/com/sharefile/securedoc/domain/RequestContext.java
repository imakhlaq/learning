package com.sharefile.securedoc.domain;

/*
This stores the current logged-in user id (because while creating a user we can specify who create/update the account)
 */
public class RequestContext {
    // CREATE NEW USERID FOR EVERY THREAD-LOCAL
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private RequestContext() {

    }
    /*Remove values associated with the USER_ID variable from the thread object,
    this ensures that any previous user context is clear and new user context
    can be created
     */
    public static void start() {
        USER_ID.remove();
    } //Allows us to initialise everything.

    //SETTER AND GETTER FOR THE USERID
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        //if no user is logged in then 0 represent the system created the user 0
        return USER_ID.get() == null ? 0 : USER_ID.get();
    }
}