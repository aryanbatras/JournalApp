import java.sql.*;

interface LoginCommands {

    String URL = "jdbc:sqlite:login.db";

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ACCOUNTS ( username TEXT NOT NULL UNIQUE PRIMARY KEY, password TEXT NOT NULL );";

    String INSERT_VALUE = "INSERT INTO ACCOUNTS (username, password) VALUES (?, ?);";

    String SELECT_ALL = "SELECT * FROM ACCOUNTS;";

    String SELECT_VALUE = "SELECT * FROM ACCOUNTS WHERE username = ?;";

    String CHECK_VALUE = "SELECT * FROM ACCOUNTS WHERE username = ? OR password = ?;";

    String DELETE_VALUE = "DELETE FROM ACCOUNTS WHERE password = ?;";

}


public class LoginDatabase implements LoginCommands {

    LoginDatabase() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        Statement s = c.createStatement();
        s.execute(CREATE_TABLE);
        s.close();
        c.close();
    }

    public void InsertValues(String username, String password) throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(INSERT_VALUE);
        s.setString(1, username);
        s.setString(2, password);
        s.executeUpdate();
        System.out.println("Database created " + username + " " + password);
        s.close();
        c.close();
    }



    public boolean authenticate(String username, String password) throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(SELECT_VALUE);
        s.setString(1, username);
        ResultSet r = s.executeQuery();
        boolean found = false;
        if(r.next()){
            String user = r.getString(1);
            String pass = r.getString(2);
            if(user.equals(username) && pass.equals(password)){
                found = true;
                System.out.println("Database found " + username);
            }
        }
        s.close();
        c.close();
        return found;
    }

    public String selectAll() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        Statement s = c.createStatement();
        ResultSet r = s.executeQuery(SELECT_ALL);
        StringBuilder allUsers = new StringBuilder();
        while(r.next()){
            allUsers.append(r.getString("username") + "\n");
        }
        s.close();
        c.close();
        return allUsers.toString();
    }

    public boolean deleteData(String password) throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(DELETE_VALUE);
        s.setString(1, password);
        int affected = s.executeUpdate();
        boolean deleted = false;
        String deletedUsername = new String();
        if(affected == 1){
            deleted = true;
        }
        s.close();
        c.close();
       return deleted;
    }

    public boolean checkDuplicates(String username, String password) throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(CHECK_VALUE);
        s.setString(1, username);
        s.setString(2, password);
        ResultSet r = s.executeQuery();
        boolean notfound = true;
        if(r.next()){
            notfound = false;
        }
        s.close();
        c.close();
        return notfound;
    }
}
