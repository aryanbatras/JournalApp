import java.sql.*;

interface SQLCommands{

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS JOURNAL ( id INTEGER PRIMARY KEY, content LONGTEXT );";

    String INSERT_VALUE = "INSERT INTO JOURNAL (id, content) VALUES (?, ?);";

    String SELECT_ALL = "SELECT * FROM JOURNAL;";

    String SELECT_VALUE = "SELECT content FROM JOURNAL WHERE id = ?;";

    String SELECT_COUNT = "SELECT COUNT(id) AS Total FROM JOURNAL;";

    String DELETE_VALUE = "DELETE FROM JOURNAL WHERE id = ?;";

    String UPDATE_VALUE = "UPDATE JOURNAL SET content = ? WHERE id = ?;";

    String UPDATE_IDS = "UPDATE JOURNAL SET id = id - 1 WHERE id > ?;";
}

public class DatabaseManager implements SQLCommands{

    private final String URL;

     DatabaseManager(String username) throws SQLException {
        URL = "jdbc:sqlite:" + username + ".db";

        Connection c = DriverManager.getConnection(URL);
        Statement s = c.createStatement();
        s.execute(CREATE_TABLE);
        c.close();
        s.close();
    }

    public void InsertValue(int id, String content) throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(INSERT_VALUE);
        s.setInt(1, id);
        s.setString(2, content);
        s.executeUpdate();
        System.out.println("Inserted : " + id + " " + content);
        c.close();
        s.close();
    }

    public String SelectData(int e) throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(SELECT_VALUE);
        s.setInt(1, e);
        ResultSet r = s.executeQuery();
        String getContent = "";
        if(r.next()){
           getContent = r.getString("content");
        }
        c.close();
        s.close();
        return getContent;
    }

    public void SelectAll() throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        Statement s = c.createStatement();
        ResultSet r = s.executeQuery(SELECT_ALL);
        System.out.println("Selecting all table ...");
        while(r.next()){
           int currentID =  r.getInt("id");
           String currentContent = r.getString("content");
           System.out.println("Selected : " + currentID + " " + currentContent);
        }
        c.close();
        s.close();
    }

    public void DeleteData(int e) throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(DELETE_VALUE);
        s.setInt(1, e);
        s.executeUpdate();
        System.out.println("Successfully deleted value at " + e);
        c.close();
        s.close();
        UpdateIDs(e);
    }

    public void UpdateIDs(int e) throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(UPDATE_IDS);
        s.setInt(1, e);
        s.executeUpdate();
        System.out.println("Successfully changed IDs after deleting id " + e);
        c.close();
        s.close();
    }

    public void UpdateData(int e, String content) throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        PreparedStatement s = c.prepareStatement(UPDATE_VALUE);
        s.setString(1, content);
        s.setInt(2, e);
        s.executeUpdate();
        System.out.println("Successfully update value at + " + e + " as " + content);
        c.close();
        s.close();
    }

    public int CountTotal() throws SQLException{
        Connection c = DriverManager.getConnection(URL);
        Statement s = c.createStatement();
        ResultSet r = s.executeQuery(SELECT_COUNT);
        int count = 0;
        if(r.next()){
            count = r.getInt("Total");
        }
        c.close();
        s.close();
        return count;
    }

}
