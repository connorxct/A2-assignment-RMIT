 
package MiRde;

/*
 * Class: InvalidRefreshements
 * Description:
 * represents an issue that occurs when attempting create refreshments that are invalid
 * author£ºConnor 
 */
public class InvalidRefreshements  extends Exception {

    public InvalidRefreshements(String info) {
        super(info);
    }
}

