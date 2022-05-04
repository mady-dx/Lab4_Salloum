
import java.sql.Connection ;
import java.sql.DriverManager ;

/**
 * This class is used to access the SQL database that is hosted locally.
 */
public class DatabaseAccessor {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseUser = "root";
        String databasePassword = "d4rkw01f";
        String url = "jdbc:mysql://localhost:3306/lab4";

        try{
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);


        } catch (Exception e) {
            System.out.println("Could not retrieve database records.");
            e.printStackTrace();
        }

        return databaseLink;
    }
}