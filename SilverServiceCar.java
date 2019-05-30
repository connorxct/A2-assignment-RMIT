package MiRde;

/*
 * Class: SilverServiceCar
 * Description: represent the sub class of Car
 * author: Connor
 */
public class SilverServiceCar extends Car 
{

    double bookingFee;
    String[] refreshments;

    public SilverServiceCar(String regNo, String make, String model,
            String driverName, int passengerCapacity, double bookingFee,
            String[] refreshments) throws InvalidRefreshements 
    {
        super(regNo, make, model, driverName, passengerCapacity);

        // providing less than three items in the refreshment list.
        if (refreshments.length < 3) 
        {
            throw new InvalidRefreshements("three items neeeded!");
        }

        // supplying a list of refreshments that contains duplicate items
        for (int i = 0; i < refreshments.length; i++) 
        {
            for (int j = i + 1; j < refreshments.length; j++) 
            {
                if (refreshments[i].equals(refreshments[j])) 
                {
                    throw new InvalidRefreshements("duplicate items!");
                }
            }
        }
        this.bookingFee = bookingFee;
        this.refreshments = refreshments;
    }

    @Override
    public boolean book(String firstName, String lastName, DateTime required, 
            int numPassengers) throws InvalidBooking 
    {

        // SilverService cars can ONLY be booked up to 3 days in advance.
        if (DateTime.diffDays(new DateTime(), required) < 3) {
            throw new InvalidBooking("can be booked for almost 3 days");
        } 
        else 
        {
            return super.book(firstName, lastName, required, numPassengers);
        }
    }

    // set plenty of variables to build the information for one specific car
    public double getBookingFeeRatio() 
    {
        return 0.4;
    }

    public double getBookingFee() 
    {
        return this.bookingFee;
    }

    // convert the refreshment to string
    protected String getRefreshmentsDetail() 
    {
        String info = "";
        info += "\nRefreshments Available\n";
        for (int i = 0; i < this.refreshments.length; i++) 
        {
            info += String.format("Item%d %s\n", i + 1, refreshments[i]);
        }
        return info;
    }

    public String toString() 
    {
        String refreshmentInfo = "";
        for (int i = 0; i < this.refreshments.length; i++) 
        {
            refreshmentInfo += String.format(":Item%d %s", i + 1, refreshments[i]);
        }
        // get the last booking to export
        Booking lastBooking;
        lastBooking = this.getlastBooking();
        if (lastBooking == null) 
        {
            lastBooking = this.getlastPassBooking();
        }
        if (lastBooking == null) 
        {
            return toCarInfoString() + refreshmentInfo;
        } 
        else 
        {
            return toCarInfoString() + refreshmentInfo + "|" + lastBooking ;
        }
    }
}
