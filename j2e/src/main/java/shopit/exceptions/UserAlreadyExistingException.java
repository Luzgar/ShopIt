package shopit.exceptions;

/**
 * Created by Charly on 30/05/2017.
 */
public class UserAlreadyExistingException extends Exception
{
    public UserAlreadyExistingException(String message)
    {
        super(message);
    }

    @Override
    public String toString()
    {
        return "UserAlreadyExistingException : " + getMessage();
    }
}
