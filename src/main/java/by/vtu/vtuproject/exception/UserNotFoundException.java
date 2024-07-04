package by.vtu.vtuproject.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userName) {
        super("could not find user '" + userName + "'.");
    }

}
