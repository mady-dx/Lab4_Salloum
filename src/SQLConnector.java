import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class SQLConnector {
    private String dbURL;
    private String user;
    private String pass;

    public SQLConnector(String url, String username, String password){
        this.dbURL = url;
        this.user= username;
        this.pass = password;
    }

    private Connection connect(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(dbURL, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public CachedRowSet query(String query) throws SQLException{
        Connection conn = connect();
        ResultSet rs = null;

        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(query);

        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowset = factory.createCachedRowSet();
        rowset.populate(rs);
        if(rs != null)
            try {rs.close();} catch (SQLException e){e.printStackTrace();}
        if(stmt!= null)
            try {stmt.close();} catch (SQLException e){e.printStackTrace();}
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rowset;
    }

    public int update(String query) throws SQLException{
        Connection conn = connect();
        int rowsUpdated = 0;

        Statement stmt = conn.createStatement();
        rowsUpdated = stmt.executeUpdate(query);

        if(stmt!= null)
            try {stmt.close();} catch (SQLException e){e.printStackTrace();}
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rowsUpdated;
    }

}
