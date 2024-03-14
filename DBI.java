import java.io.*;
import java.util.*;
import java.sql.*;

class DBI {
    // Method to save download history to database
    public static void saveDownloadHistory(String url, String fileName, long size, java.util.Date startTime, java.util.Date endTime, boolean success) {
        Connection con = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/idm?characterEncoding=utf8", "root", "");
            st = con.createStatement();

            // Create the SQL query with proper formatting of values
            String sql = "INSERT INTO download (url, file_name, size, start_time, end_time, success) VALUES (" +
                    "'" + url + "', '" + fileName + "', " + size + ", '" +
                    new java.sql.Timestamp(startTime.getTime()) + "', '" +
                    new java.sql.Timestamp(endTime.getTime()) + "', " + (success ? 1 : 0) + ")";

            // Execute the SQL query
            int rowsAffected = st.executeUpdate(sql);

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("\nDownload history stored successfully.");
            } else {
                System.out.println("Failed to store download history. No rows affected.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to store download history due to exception: " + e.getMessage());
        } finally {
            // Close the connection and statement
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getHistory() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/idm?characterEncoding=utf8", "root", "");
            st = con.createStatement();
            String str = "select * from download";
            rs = st.executeQuery(str);
            System.out.println("URL                           | File Name          | Size      | Start Time              | End Time                | Success");
            System.out.println("------------------------------+--------------------+-----------+-------------------------+-------------------------+--------");
            while (rs.next()) {
                System.out.println(rs.getString("url") + " |  " + rs.getString("file_name") + " | " + rs.getLong("size") +
                        "  " + rs.getTimestamp("start_time") + " | " + rs.getTimestamp("end_time") + " | " + rs.getBoolean("success"));
                        System.out.println("------------------------------+--------------------+-----------+-------------------------+-------------------------+--------");
                    }   
        } catch (Exception e) {
            System.out.println("Error " + e);
        } finally {
            // Close the connection, statement, and result set
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
