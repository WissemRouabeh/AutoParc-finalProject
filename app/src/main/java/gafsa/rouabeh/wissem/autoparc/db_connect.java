package gafsa.rouabeh.wissem.autoparc;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db_connect {
    String classs = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://192.168.1.5:3306/parcauto";
    String un = "aaa";
    String password = "aaa";
    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            DriverManager.setLoginTimeout(3);
            conn = DriverManager.getConnection(url, un, password);
            //conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("SQL", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("JDBC indisponible ", e.getMessage());
        } catch (Exception e) {
            Log.e("Autre exception", e.getMessage());
        }
        return conn;
    }
}
