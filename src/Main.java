import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class Main {
    public static void main(String args[]) throws SQLException {
        System.out.println("Hello World");

        //url might need to change based on what you named your db
        String url = "jdbc:mysql://localhost:3306/lab4";
        //username might change but root is default
        String username = "root";
        //password will change since it is your password for your SQL and not mine
        String password = "";

        SQLConnector sql = new SQLConnector(url, username, password);
    }
}