package MiRde;

/*
 * Class:			Booking
 * Description:		The class represents a specific Booking
 * Author:			ConnorXu - s3748848
 */
public class Booking 
{

    private String id;// the id of one booking

    private DateTime pickupDateTime;// the pick up time of one car in one booking
    private String firstName;// the first name of user
    private String lastName;// the last name of user
    private int numPassengers;// the number of passenger that they have
    private double kilometersTravelled = 0;// the kilometers that the car traveled when they complete this booking
    private double tripFee = 0;// = kilometersTravelled * (bookingFee * 0.3);// the trip fee is the number of kilometer timed 30 percent of booking fee
    private Car car;

    void setKilometersTravelled(double val) 
    {
        kilometersTravelled = val;
        tripFee = kilometersTravelled * (car.getBookingFee() * car.getBookingFeeRatio());// the trip fee is the number of kilometer timed 30 percent of booking fee
    }

    // set plenty of variables to build the information for one specific booking
    public Booking(String firstName, String lastName, DateTime required, int numPassengers, Car car) 
    {
        if (firstName.substring(0, 3).matches("[a-zA-Z].*+")) 
        {
            this.firstName = firstName;
        }
        else 
        {
            System.out.println("Wrong First Name!");
        }
        //-System.out.println(firstName);//first name must have minimum of 3 chars

        if (lastName.substring(0, 3).matches("[a-zA-Z].*+")) 
        {
            this.lastName = lastName;
        } 
        else 
        {
            System.out.println("Wrong Last Name!");
        }
        //-System.out.println(lastName);//last name must have minimum of 3 chars

        this.pickupDateTime = required;// the required time is the pick up date
        this.numPassengers = numPassengers;
        this.car = car;
        this.id = car.getregNo() + "_" + firstName.substring(0, 3).toUpperCase().concat(lastName.substring(0, 3).toUpperCase()) + "_" + required.getEightDigitDate();//set the format of the book id

    }

    public String getid() 
    {
        return id;// make sure other class can get parameter
    }

    public double getbookingFee() 
    {
        return car.getBookingFee();
    }

    public DateTime getpickupDateTime() 
    {
        return pickupDateTime;// make sure other class can get parameter
    }

    public String getfirstName() 
    {
        return firstName;// make sure other class can get parameter
    }

    public String getlastName() 
    {
        return lastName;// make sure other class can get parameter
    }

    public int getnumPassengers() 
    {
        return numPassengers;// make sure other class can get parameter
    }

    public double getkilometersTravelled() 
    {
        return kilometersTravelled;// make sure other class can get parameter
    }

    public double gettripFee() 
    {
        return tripFee;// make sure other class can get parameter
    }

    public Car getcar() 
    {
        return car;// make sure other class can get parameter
    }

    public String getDetail() 
    {
        if (kilometersTravelled == 0) 
        {
            return "             " + "id:" + "           " + id + "\n"
                    + "             " + "Booking Fee:" + "  " + "$" + String.format("%.1f", car.getBookingFee()) + "\n"
                    + "             " + "Pick up Date:" + " " + pickupDateTime.getFormattedDate() + "\n"
                    + "             " + "Name:" + "         " + numPassengers + "\n"
                    + "             " + "Travelled:" + "    " + "N/A" + "\n"
                    + "             " + "Trip Fee:" + "     " + "N/A" + "\n"
                    + "             " + "Car Id:" + "       " + id;
        } 
        else 
        {
            return "             " + "id:" + "           " + id + "\n"
                    + "             " + "Booking Fee:" + "  " + "$" + String.format("%.1f", car.getBookingFee()) + "\n"
                    + "             " + "Pick up Date:" + " " + pickupDateTime.getFormattedDate() + "\n"
                    + "             " + "Name:" + "         " + numPassengers + "\n"
                    + "             " + "Travelled:" + "    " + String.format("%.1f", kilometersTravelled) + "\n"
                    + "             " + "Trip Fee:" + "     " + String.format("%.1f", tripFee) + "\n"
                    + "             " + "Car Id:" + "       " + id;
        }

        // get the detail of one booking
    }

    public String toString() 
    {
        return id + ":" + car.getBookingFee() + ":" + pickupDateTime + ":"
                + firstName + " " + lastName + ":" + numPassengers + ":"
                + String.format("%.1f", kilometersTravelled) + ":"
                + String.format("%.1f", tripFee) + ":" + id;//get the information of booking

    }
}
