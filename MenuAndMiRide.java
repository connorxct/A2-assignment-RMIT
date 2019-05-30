package MiRde;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Class:			Menu & MiRideApp
 * Description:		The class represents the whole BookingApp function
 * Author:			ConnorXu - s3748848
 */
public class MenuAndMiRide 
{

    private Car[] cars;// the cars in the application
    private Booking[] book;// booking in the application
    private int carNum;// the number of cars in the application
    private int bookNum;// the number of bookings in the application
    private Menu me;// use the menu class

    public MenuAndMiRide()// initial the application
    {
        cars = new Car[5];
        book = new Booking[10];
        carNum = 0;
        bookNum = 0;
        me = new Menu();
    }

    public void menuDisplay()// the main function of application and the menu of this application
    {
        loadFile();
        while (true) 
        {
            me.menu();
            System.out.print("Please enter your option: ");
            String option = me.getoption();
            if ("EX".equals(option)) 
            {
                System.out.println("System Close.");
                break;
            }
            switch (option) 
            {
                case "CC": createCar();
                    break;
                case "BC": bookCar();
                    break;
                case "CB":
                    completebooking();
                    break;
                case "DA":
                    displayallcars();
                    break;
                case "SS":
                    searchSpecialCar();
                    break;
                case "SA":
                    searchAvailableCars();
                    break;
                case "SD":
                    seedData();
                    break;
                default:
                    System.out.println("Wrong! You enter wrong option.");
                    break;
            }
        }
        me.close();
        saveFile();
    }

    private void loadFile() // load the car data from file
    {
        try 
        {
            // First check for the presence of a backup file and if found use
            // this backup file for loading the data
            loadFile("db.txt");
        } 
        catch (Exception e1) 
        {
            try 
            {
                // If loading from the backup file, display a message indicating
                // that the data was loaded from a backup file.
                loadFile("db_backup.txt");
                System.out.println("e data was loaded from a backup file.");
                return;
            } 
            catch (Exception e2) 
            {
                // If no backup file is found, display a message indicating that 
                // no Booking data was loaded
                System.out.println("no Booking data was loaded");
            }
        }
    }

    private void saveFile() 
    {
        // This feature should write the data out to two files. One is the main data file, 
        // the other will be a duplicate copy to act as a backup of the data.
        saveFile("db.txt");
        saveFile("db_backup.txt");
    }

    private void loadFile(String path) throws FileNotFoundException // load the data from file
    {
        carNum = 0;
        Scanner sc = new Scanner(new File(path));
        while (sc.hasNextLine()) 
        {
            String regNo;
            String make;
            String model;
            String driverName;
            String passengerCapacity;
            String available;
            String bookingFee;

            String carInfo = sc.nextLine().split("\\|")[0];
            
            // split the line by :
            String[] word = carInfo.split(":");
            regNo = word[0];
            make = word[1];
            model = word[2];
            driverName = word[3];
            passengerCapacity = word[4];
            available = word[5];
            bookingFee = word[6];
            
            // if have refreshment, the car is SilverServiceCar
            if (word.length > 7) 
            {
                String[] freshments = new String[word.length - 7];
                for (int i = 7; i < word.length; i++) 
                {
                    String[] item = word[i].split(" ");
                    freshments[i - 7] = item[item.length - 1];
                }
                try 
                {
                    addnewCar(new SilverServiceCar(regNo, make, model, driverName, 
                            Integer.parseInt(passengerCapacity), 
                            Double.parseDouble(bookingFee), freshments));
                } 
                catch (InvalidRefreshements ex) 
                {
                    ex.printStackTrace();
                }
            } 
            else 
            {
                addnewCar(new Car(regNo, make, model, driverName, 
                        Integer.parseInt(passengerCapacity)));
            }
        }        
        sc.close();

    }

    private void saveFile(String path) //save each car into file
    {
        try 
        {
            PrintWriter printer = new PrintWriter(path);
            for (int i = 0; i < carNum; i++) 
            {
                printer.append(cars[i].toString() + "\n");
            }
            printer.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }

    private void createCar()//  define the case create car
    {
        Car newCar = null;
        try 
        {
            newCar = new_Car();
        } 
        catch (InvalidRefreshements e) 
        {
            // if refreshments illegal
            System.out.println(e);
            newCar = null;
        }

        if (newCar != null) 
        {
            addnewCar(newCar);
            System.out.println(
                    "New Car added successfully and the registration number is" 
                            + newCar.getregNo() + "\n");
        }
    }

    private void addnewCar(Car newCar)// add new car into our application
    {
        CarArray(carNum + 1);
        cars[carNum] = newCar;
        carNum++;
    }

    private void CarArray(int capacity)// define the car array
    {
        if (capacity >= cars.length) 
        {
            Car[] newARR = new Car[cars.length * 2];
            System.arraycopy(cars, 0, newARR, 0, cars.length);

            cars = newARR;
        }
    }

    private Car new_Car() throws InvalidRefreshements// the steps of add new car
    {
        String regNo = me.enter(String.format("%-30s", "Enter Registration No:"));
        if (existcarregnum(regNo)) 
        {
            System.out.println("Wrong! This Car already exist in the system.");
        }
        String make = me.enter(String.format("%-30s", "Please Make: "));
        String model = me.enter(String.format("%-30s", "Please Model: "));
        String driverName = me.enter(String.format("%-30s", "Please Driver's Name: "));
        int passengerCapacity = me.enterint(String.format("%-30s", "Please Passengers Ca[acity "), 1, 9);
        String serviceType = me.enter(String.format("%-30s", "Enter Service Type (SD/SS)   ")).toUpperCase();
        if (serviceType.trim().equals("SD")) 
        {
            return new Car(regNo, make, model, driverName, passengerCapacity);
        } 
        else 
        {
            double stardardFee = me.enterdou(String.format("%-30s", "Enter Standard Fee:      "));
            String refresh = me.enter(String.format("%-30s", "Enter List of Refreshments:  "));
            String[] refreshments = refresh.split(",");
            return new SilverServiceCar(regNo, make, model, driverName, passengerCapacity, stardardFee, refreshments);
        }
    }

    private boolean existcarregnum(String regNo)// return true when the registration number is exist in the application
    {
        for (int i = 0; i < carNum; i++) 
        {
            Car car = cars[i];

            if (car.getregNo().equals(regNo)) 
            {
                return true;
            }
        }

        return false;
    }

    private void bookCar()// start to book a ride
    {
        Booking newbooking=null;
        try 
        {
            newbooking = new_Booking();// check for invalid input
        } 
        catch (InvalidBooking ex) 
        {
            System.out.println("Error: "+ex.toString());
            return;
        }

        if (newbooking != null) 
        {
            addBooking(newbooking);
            System.out.printf("Thanks for your booking and %s will pick you up on %s.\n", 
                    newbooking.getcar().getdriverName(), newbooking.getpickupDateTime().getFormattedDate());
            System.out.println("Then this is your booking ID: " + newbooking.getid());
        }
        else
        {
            System.out.println("Error: Can not book");
        }
    
    }

    private Booking new_Booking() throws InvalidBooking// the steps of booking
    {
        String date = me.enter("Please enter your reuired time:       ");
        DateTime required;
        try// make sure user could try again when they enter wrong time
        {
            required = parseDate(date);
        } 
        catch (Exception ex) 
        {
            System.out.println("Wrong! Please enter new date.");
            return null;
        }
        Car[] availableCars = searchAvailableCars(required);
        if (availableCars.length == 0) 
        {
            System.out.println("Sorry we have no car for this date.");
            return null;
        }

        System.out.println("These following cars are our application have now.");
        for (int b = 0; b < availableCars.length; b++) 
        {
            System.out.printf("%d.  %s\n", b + 1, availableCars[b].getregNo());
        }

        int bookingIndex = me.enterint("Please select the number of our available car that you want to book: ", 1, availableCars.length);
        Car bookingCar = availableCars[bookingIndex - 1];

        String firstName = me.enter("Please enter your first name:      ");
        if (firstName.length() < 3) 
        {
            System.out.println("Wrong! You entered the wrong first name.");
            return null;
        }

        String lastName = me.enter("Please enter your last name:             ");
        if (lastName.length() < 3) 
        {
            System.out.println("Wrong! You entered the wrong last name.");
            return null;
        }

        int numPassengers = me.enterint("Please enter the number of passengers:   ", 1, 9);
        if (numPassengers > bookingCar.getpassengerCapacity()) 
        {
            System.out.println("Wrong! This car does not enough passenger capacity.");
            return null;
        }

        boolean booking = bookingCar.book(firstName, lastName, required, numPassengers);
        if (booking) 
        {
            return bookingCar.getlastBooking();
        }

        return null;
    }

    private DateTime parseDate(String date)// give the date time
    {
        String[] newdate = date.split("/");
        int day = Integer.parseInt(newdate[0]);
        int month = Integer.parseInt(newdate[1]);
        int year = Integer.parseInt(newdate[2]);
        return new DateTime(day, month, year);
    }

    private void addBooking(Booking newBooking)//add new booking into application
    {
        bookingArray(bookNum + 1);
        book[bookNum] = newBooking;
        bookNum++;
    }

    private void bookingArray(int capacity)// make sure about the booking array capacity
    {
        if (capacity >= book.length) 
        {
            Booking[] newarray = new Booking[cars.length * 2];
            System.arraycopy( book, 0, newarray, 0,capacity);
            book = newarray;
        }
    }

    private Car[] searchAvailableCars(DateTime required, String type)// search the available car in the application for certain type
    {
        int availableCarNum = 0;
        for (int i = 0; i < carNum; i++) 
        {
            if (cars[i].carforoneday(required)
                    && (type.toUpperCase().equals("SS") 
                    && cars[i].getClass().equals(SilverServiceCar.class) 
                    || type.toUpperCase().equals("SD") 
                    && cars[i].getClass().equals(Car.class))) 
            {
                availableCarNum++;
            }
        }

        Car[] availableCar = new Car[availableCarNum];
        
        int index = 0;
        for (int i = 0; i < carNum; i++) 
        {
            if (cars[i].carforoneday(required)
                    && (type.toUpperCase().equals("SS")
                    && cars[i].getClass().equals(SilverServiceCar.class) 
                    || type.toUpperCase().equals("SD") 
                    && cars[i].getClass().equals(Car.class))) 
            {
                availableCar[index++] = cars[i];
            }
        }

        return availableCar;
    }
    private Car[] searchAvailableCars(DateTime required)// search the available car in the application
    {
        int availableCarNum = 0;
        for (int i = 0; i < carNum; i++) {
            if (cars[i].carforoneday(required)) 
            {
                availableCarNum++;
            }
        }

        Car[] availableCar = new Car[availableCarNum];
        int index = 0;
        for (int i = 0; i < carNum; i++) 
        {
            if (cars[i].carforoneday(required)) 
            {
                availableCar[index++] = cars[i];
            }
        }

        return availableCar;
    }

    private void completebooking()// complete the booking
    {
        String bookingInfo = me.enter("Please enter your registration number or your Booking Date:      ");
        String firstName = me.enter("Please enter your first name:     ");
        String lastName = me.enter("Please enter your last name:     ");

        Booking booking = null;
        for (int b = 0; b < bookNum; b++) 
        {
            Booking specific = book[b];

            if (specific.getcar().getregNo().equals(bookingInfo) 
                    && specific.getpickupDateTime().getFormattedDate().equals(bookingInfo)) 
            {
                if (specific.getfirstName().equals(firstName) && specific.getlastName().equals(lastName)) 
                {
                    booking = specific;
                    break;
                }
            }
        }

        if (booking == null) 
        {
            System.out.println("Wrong! This booking could not be found in our application.");
            return;
        }

        double kilometersTravelled = me.enterdou("Enter your travelled kilometers:  ");
        completeBooking(booking, kilometersTravelled);
        System.out.println("Thanks for using MiRide and our team hope you have a good trip.");
        System.out.printf("$%.4f have been transfromed from your account.\n", 
                booking.getbookingFee() + booking.gettripFee());
    }

    private void completeBooking(Booking booking, double kilometersTravelled) 
    {
        Car bookingCar = booking.getcar();
        bookingCar.completeBook(booking.getid(),kilometersTravelled);
    }

    private void displayallcars()// display all cars' inforamtion in the application
    {
        if (carNum == 0) 
        {
            System.out.println("Sorry, there is no car avaliable in the appliacation now.");
            return;
        }

        String type = me.enter("Enter Type (SD/SS):      ").toUpperCase();
        String AscDes = me.enter("Enter Sort Order (A/D):  ").toUpperCase();
        displayallcars(type, AscDes);

    }

    private void displayallcars(String type, String AscDes)// display all cars' information in the application
    {
        if (carNum == 0) 
        {
            System.out.println("Sorry, there is no car avaliable in the appliacation now.");
            return;
        }

        // calculate the total number of certain cars
        Car[] typeCars = new Car[carNum];
        int typeCarsNum = 0;
        for (int i = 0; i < carNum; i++) 
        {
            if (cars[i].getClass().equals(Car.class) && type.toUpperCase().equals("SD")) 
            {
                typeCars[typeCarsNum++] = cars[i];
            }
            if (cars[i].getClass().equals(SilverServiceCar.class) && !type.toUpperCase().equals("SD")) {
                typeCars[typeCarsNum++] = cars[i];
            }
        }

        if (typeCarsNum == 0) 
        {
            System.out.println("Error - No standard cars were found on this date");
            return;
        }

        // display all the certain type of car
        System.out.println("All cars' information:\n");
        if (AscDes.toUpperCase().equals("A")) 
        {
            
            // bubble sort
            for (int i = 0; i < typeCarsNum; i++) 
            {
                for (int j = 0; j < typeCarsNum - 1 - i; j++) 
                {
                    if (typeCars[j].getregNo().compareTo(typeCars[j + 1].getregNo()) > 1) 
                    {
                        Car c = typeCars[j];
                        typeCars[j] = typeCars[j + 1];
                        typeCars[j + 1] = c;
                    }
                }
            }
        } else {
            // bubble sort
            for (int i = 0; i < typeCarsNum; i++) {
                for (int j = 0; j < typeCarsNum - 1 - i; j++) {
                    if (typeCars[j].getregNo().compareTo(typeCars[j + 1].getregNo()) < 1) {
                        Car c = typeCars[j];
                        typeCars[j] = typeCars[j + 1];
                        typeCars[j + 1] = c;
                    }
                }
            }
        }

        // display the detail of each car
        for (int i = 0; i < typeCarsNum; i++) 
        {
            System.out.println(typeCars[i].getDetail());
            //System.out.println();
        }
    }

    private void searchSpecialCar()// search one specific car in the application
    {
        String regNo = me.enter("Please enter the registration number:    ");
        boolean found = false;

        for (int c = 0; c < carNum; c++) 
        {
            Car car = cars[c];

            if (car.getregNo().equals(regNo)) 
            {
                found = true;

                System.out.println();
                System.out.println(car.getDetail());
                System.out.println();
                break;
            }
        }

        if (!found) 
        {
            System.out.println("Srroy, we cannot find the car that you want.");
        }
    }

    private void searchAvailableCars()// search the available cars in the application
    {
        String type = me.enter("Enter Type (SD/SS):     ");
        String date = me.enter("Date:         ");
        DateTime required;
        try 
        {
            required = parseDate(date);
        } 
        catch (Exception ex) 
        {
            System.out.println("Wrong! Please enter new date.");
            return;
        }

        DateTime now = new DateTime();
        int days = DateTime.diffDays(required, now);
        if (days < 0 || days > 7) 
        {
            System.out.println("Sorry we have no car that is available for this date.");
            return;
        }

        // search for certain type
        Car[] availableCars = searchAvailableCars(required, type.toUpperCase());
        if (availableCars.length == 0) 
        {
            System.out.println("Error - No cars we found on this date");
        } else {
            System.out.println("These following cars are available in our application now.");
            for (Car car : availableCars) 
            {
                System.out.println(car.getDetail());
                System.out.println();
            }
        }
    }

    private void seedData()// establish the initial data
    {
        if (carNum != 0) 
        {
            System.out.println("Wrong! These cars already had their data.");
            return;
        }
        Car[] seedCars = null;
        try {
            seedCars = new Car[] // initial 6 cars in the system
            {
                new Car("CON123", "BENZ", "ModelG", "LeBron James", 5),
                new Car("CON234", "HONDA", "ModelA", "Lionel Messi", 8),
                new Car("CON345", "TESLA", "ModelS", "Michael Jordan", 6),
                new Car("CON456", "BUICK", "ModelB", "Lady Gaga", 6),
                new Car("CON567", "BMW", "ModelQ", "Kobe Bryant", 4),
                new Car("CON678", "FERRARI", "ModelS", "James Harden", 6),
                // initial 6 silverService cars in the system
                new SilverServiceCar("SSD123", "AEFR", "Mode4G", "Smith James", 5, 4, new String[]{"Apple", "Bar", "Peach"}),
                new SilverServiceCar("SSD234", "HONDA", "Mode4A", "Lionel Kay", 8, 5, new String[]{"Cream", "Orange", "Banana"}),
                new SilverServiceCar("SSD345", "TESLA", "Mode4S", "Jarry Jordan", 6, 6, new String[]{"HotDog", "Banana", "Peach"}),
                new SilverServiceCar("SSD456", "ALNLE", "Mode4B", "Lady Gaga", 6, 7, new String[]{"HotDog", "Banana", "Oval"}),
                new SilverServiceCar("SSD567", "PPAR", "Model4Q", "Jane Bryant", 4, 8, new String[]{"Apple", "Oval", "Banana"}),
                new SilverServiceCar("SSD678", "FERRARI", "Model8S", "James Bob", 2, 9, new String[]{"Cream", "Bar", "Peach"})
            };
        } 
        catch (Exception e) 
        {
        }

        for (Car seedCar : seedCars)// initial 12 bookings in the system
        {
            addnewCar(seedCar);
        }

        boolean success;

        // Two silver service cars that HAVE BEEN booked, and the bookings have been completed
        success = cars[6].book("Ammm", "June", new DateTime(-8), 2, true);
        addBooking(cars[6].getlastBooking());
        cars[6].completeBook(cars[6].getregNo(), 10);
        success = cars[6].book("Davide", "Chen", new DateTime(-5), 2, true);
        addBooking(cars[6].getlastBooking());
        cars[6].completeBook(cars[6].getregNo(), 100);
        //success = cars[6].book("Madam", "Cdfa", new DateTime(-3), 2, true);
        //addBooking(cars[6].getlastBooking());
        //cars[6].completeBook(cars[6].getregNo(), 50);

        success = cars[7].book("James", "Chen", new DateTime(-15), 2, true);
        addBooking(cars[7].getlastBooking());
        cars[7].completeBook(cars[7].getregNo(), 70);        
        success = cars[7].book("Namj", "James", new DateTime(-13), 2, true);
        addBooking(cars[7].getlastBooking());
        cars[7].completeBook(cars[7].getregNo(), 60);

        // Two silver service cars that HAVE BEEN been booked, but the bookings have not been completed
        success=cars[8].book("Tiffany", "Xuu", new DateTime(-1), 2, true);
        Booking car8 = cars[8].getlastBooking();
        addBooking(car8);

        success=cars[9].book("Dennis", "Zhao", new DateTime(-2), 4, true);
        Booking car9 = cars[9].getlastBooking();
        addBooking(car9);
        
        // Two silver service cars that HAVE NOT been booked

        System.out.println("Seeding data are success!");
    }

    public Car[] getcar() 
    {
        return cars;// make sure get parameter
    }

    public Booking[] getbooking() 
    {
        return book;// make sure  get parameter
    }

    public int getcarNum() 
    {
        return carNum;// make sure get parameter
    }

    public int getbookNum() 
    {
        return bookNum;// make sure get parameter
    }

    public static void main(String[] args) throws FileNotFoundException // the main method of this application, start whole function
    {
        //System.setIn(new FileInputStream("a"));
        MenuAndMiRide APP = new MenuAndMiRide();
        APP.menuDisplay();
    }

}
