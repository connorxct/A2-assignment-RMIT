package MiRde;

/*
 * Class: InvalidToken
 * Description:
 * will be thrown when user input invalid string
 * author��Connor
 */
public class InvalidToken extends Exception {

    public InvalidToken(String info) {
        super(info);
    }
}
