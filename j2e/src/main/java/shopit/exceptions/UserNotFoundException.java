package shopit.exceptions;

/**
 * Created by Charly on 06/06/2017.
 */
public class UserNotFoundException extends Exception
{
    public UserNotFoundException(String message) {super(message);}

    @Override
    public String toString()
    {
        return "UserNotFound : " + getMessage();
    }
}
