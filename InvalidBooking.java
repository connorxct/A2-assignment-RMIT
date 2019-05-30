package MiRde;

/*
 * Class: InvalidBooking
 * Description:
 * represents an issue that occurs when a booking problem is found
 * -- trying to book on a date prior the current day. 
 * -- trying to book a car on a day that it is already booked. 
 * -- trying to book when five current bookings exist.
 * author£ºConnor
 */
public class InvalidBooking extends Exception 
{

    public InvalidBooking(String info) {
        super(info);
    }
}
