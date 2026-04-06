package Model;

import java.sql.*;
import java.time.LocalDate;

public class Database {
    private String username;
    private String password;

    public static Connection getDatabaseConnection() {
        String url = System.getenv("url_db");
        String user = System.getenv("user_db");
        String password = System.getenv("password_db");

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");
            return con;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println("con failed");
            return null;
        }
    }

    public static void addUser(String name, String password) throws Exception {
        Connection con = getDatabaseConnection();
        try {
            String INSERT = "INSERT INTO appuser (username, pass_word) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            System.out.println("User added!");
            pstmt.close();
            con.close();
        } catch (Exception e){
            if (con!=null){
                con.close();
            }
        }
    }

    public boolean logIn(String username, String password) throws Exception{
        Connection con = getDatabaseConnection();
        this.username = username;
        this.password = password;
        int count = 0;
        try {
            String QUERY = "SELECT COUNT(*) FROM appuser WHERE username = ? AND pass_word = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            pstmt.close();
            con.close();
            return count > 0;
        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
        }
        return count > 0;
    }

    public boolean doesUsernameAlreadyExist(String username) throws Exception{
        Connection con = getDatabaseConnection();
        int count = 0;
        try {
            String QUERY = "SELECT COUNT(*) FROM appuser WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            pstmt.close();
            con.close();
            return count > 0;
        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
        }
        return count > 0;
    }

    public void addNewUserToDatabase(String username, String password) throws Exception {
        Connection con = getDatabaseConnection();
        try {
            String INSERT = "INSERT INTO appuser (username, pass_word) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(INSERT);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            System.out.println("You have been registered!");
            pstmt.close();
            con.close();
        }catch (Exception e){
            if (con!=null){
                con.close();
            }
        }
    }

}
