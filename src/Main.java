import java.sql.*;
import java.util.*;


public class Main {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the number for the action you would like to take: ");
        System.out.println("1. Display schedule for trips\n2. Edit the schedule" +
                "\n3. Display stops for a given trip\n4. Display the schedule for a driver" +
                "\n5. Add a driver\n6. Add a bus\n7. Delete a bus" +
                "\n8. Record the actual data of a given trip offering");
        System.out.print("Enter the menu option: ");
        int menuChoice = scan.nextInt();

        switch (menuChoice) {
            case 1:
                displaySchedule();
            case 2:
                editSchedule();
            case 3:
                displayStops();
            case 4:
                displayWeeklySchedule();
            case 5:
                addDriver();
            case 6:
                addBus();
            case 7:
                deleteBus();
            case 8:
                recordActualData();
        }
    }

    private static void displaySchedule() throws SQLException {
        System.out.println("\n");
        Scanner scan = new Scanner(System.in);

        System.out.println("\nEnter the following info to see scheduled trips: ");

        System.out.print("Start Location: ");
        String startLocation = scan.nextLine();

        System.out.print("Destination: ");
        String destination = scan.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scan.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "SELECT T1.StartLocationName, T1.DestinationName,T0.ScheduledStartTime, T0.ScheduledArrivalTime, T0.DriverName, T0.BusID, T0.Date  " +
                "FROM TripOffering T0, Trip T1 " +
                "WHERE T1.StartLocationName LIKE '" + startLocation + "' AND " +
                "T1.DestinationName LIKE '" + destination + "' AND " +
                "T0.Date = '" + date + "' AND " +
                "T1.TripNumber = T0.TripNumber " +
                "Order by ScheduledStartTime";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(connectQuery);

            System.out.println("-------------------------------------------------------------------------" +
                    "-----------------------------------------------------------------------------------------");
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            for (int i = 1; i <= colNum; i++) {
                String line = String.format("|%22s", rsmd.getColumnName(i));
                System.out.print(line);
            }
            System.out.println("|");

            System.out.println("-------------------------------------------------------------------------" +
                    "-----------------------------------------------------------------------------------------");

            while (rs.next()) {
                for (int i = 1; i <= colNum; i++) {
                    String l2 = String.format("|%22s", rs.getString(i));
                    System.out.print(l2);
                }
                System.out.println("|");
            }
            rs.close();
            connectDB.close();
            System.out.println("-------------------------------------------------------------------------" +
                    "-----------------------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void editSchedule() {
        System.out.println("\n");
        System.out.println("How would you like to edit the schedule: ");
        System.out.println("1. Delete a trip offering\n2. Add a set of trip offerings\n3. Change the driver " +
                "for a given trip offering\n4. Change the bus for a given trip offering");
        System.out.print("Enter a menu option: ");
        Scanner scan = new Scanner(System.in);

        int editMenuChoice = scan.nextInt();

        switch (editMenuChoice) {
            case 1:
                deleteTripOffering();
            case 2:
                addTripOffering();
            case 3:
                changeDriver();
            case 4:
                changeBus();
        }

    }

    private static void deleteTripOffering() {
        Scanner scan = new Scanner(System.in);
        Scanner intScan = new Scanner(System.in);

        System.out.println("\nEnter the following info for the trip offering you would like to delete: ");
        System.out.print("Trip #: ");
        int tripNum = intScan.nextInt();

        System.out.println("Date (YYYY-MM-DD): ");
        String date = scan.nextLine().trim();

        System.out.println("Scheduled start time (hh:mm:ss): ");
        String scheduledStartTime = scan.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "DELETE FROM TripOffering WHERE TripNumber = '" + tripNum +
                "' AND Date LIKE '" + date + "' AND ScheduledStartTime LIKE '" + scheduledStartTime + "'";

        try {

            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);

            System.out.println("Trip Offering deleted successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void addTripOffering() {
        Scanner intScan = new Scanner(System.in);
        Scanner scan = new Scanner(System.in);

        String addAnotherTrip = "y";
        while (addAnotherTrip.equals("y")) {
            System.out.println("\nEnter the information for the trip offering you would like to add: ");
            System.out.print("Trip #: ");
            int tripNum = intScan.nextInt();

            System.out.print("Date (YYYY-MM-DD): ");
            String date = scan.nextLine().trim();

            System.out.print("Scheduled start time (hh:mm:ss): ");
            String scheduledStartTime = scan.nextLine().trim();

            System.out.print("Scheduled arrival time (hh:mm:ss): ");
            String scheduledArrivalTime = scan.nextLine().trim();

            System.out.print("Driver name: ");
            String driverName = scan.nextLine().trim();

            System.out.print("BusID: ");
            String busID = scan.nextLine().trim();

            DatabaseAccessor connectNow = new DatabaseAccessor();
            Connection connectDB = connectNow.getConnection();

            String connectQuery = "INSERT INTO TripOffering(TripNumber, Date, ScheduledStartTime," +
                    "ScheduledArrivalTime, DriverName, BusID) values (?, ?, ?, ?, ?, ?)";


            try {

                PreparedStatement preparedStmt = connectDB.prepareStatement(connectQuery);

                preparedStmt.setInt(1, tripNum);
                preparedStmt.setString(2, date);
                preparedStmt.setString(3, scheduledStartTime);
                preparedStmt.setString(4, scheduledArrivalTime);
                preparedStmt.setString(5, driverName);
                preparedStmt.setString(6, busID);

                preparedStmt.execute();

                System.out.println("Trip Offering added successfully");

                connectDB.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.print("Add another trip? (y/n) : ");
            addAnotherTrip = scan.nextLine();
        }
        System.exit(0);
    }

    private static void changeDriver() {
        Scanner scan = new Scanner(System.in);
        Scanner intScan = new Scanner(System.in);

        System.out.println("\nEnter the trip info for the driver you want to change: ");
        System.out.print("Trip #: ");
        int tripNum = intScan.nextInt();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scan.nextLine().trim();

        System.out.print("Scheduled start time (hh:mm:ss): ");
        String scheduledStartTime = scan.nextLine().trim();

        System.out.print("Name of new driver: ");
        String newDriverName = scan.nextLine();
        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "UPDATE TripOffering set DriverName = '" + newDriverName + "' WHERE TripNumber = '" + tripNum +
                "' AND Date LIKE '" + date + "' AND ScheduledStartTime LIKE '" + scheduledStartTime + "'";

        try {

            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);

            System.out.println("Driver changed successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void changeBus() {
        Scanner scan = new Scanner(System.in);
        Scanner intScan = new Scanner(System.in);

        System.out.println("\nEnter the trip info for the bus you want to change: ");
        System.out.print("Trip #: ");
        int tripNum = intScan.nextInt();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scan.nextLine().trim();

        System.out.print("Scheduled start time (hh:mm:ss): ");
        String scheduledStartTime = scan.nextLine().trim();

        System.out.print("ID of new bus: ");
        String newBus = scan.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "UPDATE TripOffering set BusID = '" + newBus + "' WHERE TripNumber = '" + tripNum +
                "' AND Date LIKE '" + date + "' AND ScheduledStartTime LIKE '" + scheduledStartTime + "'";

        try {

            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);

            System.out.println("Bus changed successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void displayStops() {
        System.out.println("\n");
        Scanner scan = new Scanner(System.in);
        Scanner intScan = new Scanner(System.in);

        System.out.println("\nEnter the following info to see stops of a given trip: ");

        System.out.print("Trip #: ");
        int tripNum = intScan.nextInt();



        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = ("SELECT * FROM TripStopInfo " +
                "WHERE TripNumber = '" + tripNum + "' " +
                "Order By SequenceNumber ");

        try {

            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(connectQuery);
            System.out.println("-------------------------------------------------------------------------" +
                    "--------------------");
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            for (int i = 1; i <= colNum; i++) {
                String line = String.format("|%22s", rsmd.getColumnName(i));
                System.out.print(line);
            }
            System.out.println("|");

            System.out.println("-------------------------------------------------------------------------" +
                    "--------------------");

            while (rs.next()) {
                for (int i = 1; i <= colNum; i++) {
                    String line = String.format("|%22s", rs.getString(i));
                    System.out.print(line);
                }
                System.out.println("|");
            }
            rs.close();
            connectDB.close();
            System.out.println("-------------------------------------------------------------------------" +
                    "--------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void displayWeeklySchedule() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Driver name: ");
        String driverName = sc.nextLine().trim();

        System.out.print("Date: ");
        String date = sc.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = ("SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID " +
                "FROM TripOffering " +
                "WHERE DriverName LIKE '" + driverName + "' " +
                "AND Date = '" + date + "' " +
                "Order By ScheduledStartTime ");

        try{
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(connectQuery);

            System.out.println("-------------------------------------------------------------------------" +
                    "--------------------------------------------");

            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            for (int i = 1; i <= colNum; i++) {
                String line = String.format("|%22s", rsmd.getColumnName(i));
                System.out.print(line);
            }
            System.out.println("|");

            System.out.println("-------------------------------------------------------------------------" +
                    "--------------------------------------------");

            while (rs.next()) {
                for (int i = 1; i <= colNum; i++){
                    String line = String.format("|%22s", rs.getString(i));
                    System.out.print(line);
                }
                System.out.println("|");
            }
            rs.close();
            connectDB.close();
            System.out.println("-------------------------------------------------------------------------" +
                    "--------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    private static void addDriver() {
        Scanner scan = new Scanner(System.in);

        System.out.println("\nEnter the info for the driver you would like to add: ");

        System.out.print("Driver name: ");
        String driverName = scan.nextLine().trim();

        System.out.print("Driver phone #: ");
        String driverPhone = scan.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "INSERT INTO driver(DriverName, DriverTelephoneNumber) " +
                "values (?, ?)";

        try {

            PreparedStatement preparedStmt = connectDB.prepareStatement(connectQuery);

            preparedStmt.setString(1, driverName);
            preparedStmt.setString(2, driverPhone);

            preparedStmt.execute();

            System.out.println("Driver added successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private static void addBus() {
        Scanner scan = new Scanner(System.in);
        Scanner intScan = new Scanner(System.in);

        System.out.println("\nEnter the info for the Bus you would like to add: ");

        System.out.print("Bus ID: ");
        String busID = scan.nextLine().trim();

        System.out.print("Bus Model: ");
        String busModel = scan.nextLine().trim();

        System.out.print("Bus Model: ");
        int busYear = intScan.nextInt();


        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "INSERT INTO bus(BusID, Model, Year) " +
                "values (?, ?, ?)";

        try {

            PreparedStatement preparedStmt = connectDB.prepareStatement(connectQuery);

            preparedStmt.setString(1, busID);
            preparedStmt.setString(2, busModel);
            preparedStmt.setInt(3, busYear);

            preparedStmt.execute();

            System.out.println("Bus added successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private static void deleteBus() throws ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        System.out.println("\nEnter the following info to delete a bus: ");

        System.out.print("Bus ID: ");
        String busID = scan.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "DELETE FROM Bus WHERE BusID = '" + busID + "'";

        try {

            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);

            System.out.println("Bus deleted successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void recordActualData() {
        Scanner scan = new Scanner(System.in);
        Scanner intScan = new Scanner(System.in);

        System.out.println("\nEnter the actual data for the trip: ");

        System.out.print("Trip #: ");
        int tripNum = intScan.nextInt();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scan.nextLine().trim();

        System.out.print("Scheduled start time (hh:mm:ss): ");
        String scheduledStartTime = scan.nextLine().trim();

        System.out.print("Stop #: ");
        int stopNum = intScan.nextInt();

        System.out.print("Scheduled arrival time (hh:mm:ss): ");
        String scheduledArrivalTime = scan.nextLine().trim();

        System.out.print("Actual start time (hh:mm:ss): ");
        String actualStartTime = scan.nextLine().trim();

        System.out.print("Actual arrival time (hh:mm:ss): ");
        String actualArrivalTime = scan.nextLine().trim();

        System.out.print("Number of passengers in: ");
        String numPassIn = scan.nextLine().trim();

        System.out.print("Number of passengers out: ");
        String numPassOut = scan.nextLine().trim();

        DatabaseAccessor connectNow = new DatabaseAccessor();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "INSERT INTO ActualTripStopInfo(TripNumber, Date, ScheduledStartTime, " +
                "StopNumber, ScheduledArrivalTime, ActualStartTime, ActualArrivalTime," +
                "NumberOfPassengerIn, NumberOfPassengerOut)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            PreparedStatement preparedStmt = connectDB.prepareStatement(connectQuery);

            preparedStmt.setInt(1, tripNum);
            preparedStmt.setString(2, date);
            preparedStmt.setString(3, scheduledStartTime);
            preparedStmt.setInt(4, stopNum);
            preparedStmt.setString(5, scheduledArrivalTime);
            preparedStmt.setString(6, actualStartTime);
            preparedStmt.setString(7, actualArrivalTime);
            preparedStmt.setString(8, numPassIn);
            preparedStmt.setString(9, numPassOut);


            preparedStmt.execute();

            System.out.println("Actual data recorded successfully");

            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

}