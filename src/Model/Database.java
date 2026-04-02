package Model;

import java.sql.*;

public class Database {

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


}
