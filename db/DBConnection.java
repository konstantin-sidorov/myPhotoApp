package myPhotoApp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final Connection connection;
    public static final String DRV_NAME     =   "com.mysql.cj.jdbc.Driver";
    public static final String CONN_STRING  =
            "jdbc:mysql://localhost:3306/?user=root&password=root"+
                    "&serverTimezone=UTC";
    public DBConnection() throws DataBaseException {
        try {
            Class.forName(DRV_NAME);
        } catch (ClassNotFoundException e) {
            DataBaseException se = new DataBaseException("Can not find MySQL driver!");
            se.initCause(e);
            throw se;
        }
        try {
            connection = DriverManager.getConnection(CONN_STRING);
            //connection.setAutoCommit(false);
        } catch (SQLException e) {
            DataBaseException se = new DataBaseException("connection error");
            se.initCause(e);
            throw se;
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    public void close() throws DataBaseException {
        try {
            connection.close();
        } catch (SQLException e) {
            DataBaseException se = new DataBaseException("close error");
            se.initCause(e);
            throw se;
        }
    }
}
