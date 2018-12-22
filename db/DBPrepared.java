package myPhotoApp.db;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBPrepared implements AutoCloseable {
    private final DBConnection connection;
    private static final String CHECK_TABLE =
            "select 1 from information_schema.tables where table_schema = 'db' and " +
                    "table_name = 'pictures';";
    private static final String CREATE_TABLE    =
            "CREATE TABLE db.pictures (name varchar(255) , photo LONGBLOB);";

    private static final String INSERT_PICTURE  =
            "INSERT INTO db.pictures (name,photo) values (?,?);";
    private static final String SELECT_PHOTO     =
            "SELECT 1 FROM db.pictures where name='%s';";
    private static final String SELECT_NAME     =
            "SELECT name FROM db.pictures;";
    private static final String SELECT_ONE_PHOTO     =
            "SELECT photo FROM db.pictures where name='%s';";

    public DBPrepared() throws DataBaseException {
        this.connection = new DBConnection();
    }

    public Connection getConnection() {
        return connection.getConnection();
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
    public InputStream load_one_photo(String name) throws DataBaseException {
        Executor exec = new Executor(getConnection());
        InputStream photo    =   exec.execQuery(String.format(SELECT_ONE_PHOTO, name),(result)->{
            try {
                if (result.next()){
                    return  result.getBinaryStream("photo");
                }
            }catch (SQLException e){
                DataBaseException se = new DataBaseException("error!!!");
                se.initCause(e);
                throw se;
            }
            return null;
        });
        return photo;
    }
    public ArrayList<String> load_name() throws DataBaseException {
        Executor exec = new Executor(getConnection());
        ArrayList<String> photos    =   exec.execQuery(SELECT_NAME,(result)->{
            ArrayList<String> list  =   new ArrayList<>();
            try {
                while (result.next()){
                    list.add(result.getString("name"));
                }
            }catch (SQLException e){
                DataBaseException se = new DataBaseException("error!!!");
                se.initCause(e);
                throw se;
            }
            return list;
        });
        return photos;
    }
    public void add_picture(String name, InputStream in) throws DataBaseException {
        Executor exec = new Executor(getConnection());
        exec.execUpdate(INSERT_PICTURE,(statement)->{
            try {
                statement.setString(1,name);
                statement.setBlob(2,in);
                statement.executeUpdate();
            } catch (SQLException e) {
                DataBaseException se = new DataBaseException("insert into table error");
                se.initCause(e);
                throw se;
            }
        });
    }
    public Boolean load_photo(String name) throws DataBaseException {
        Executor exec = new Executor(getConnection());
        return exec.execQuery(String.format(SELECT_PHOTO, name),(result)->{
            try {
                if (result.next()) return Boolean.TRUE;
                else return Boolean.FALSE;
            }catch (SQLException e){
                DataBaseException se = new DataBaseException("error request!");
                se.initCause(e);
                throw se;
                //return Boolean.FALSE;
            }
        }
        );
    }
    public void prepareTables() throws DataBaseException, SQLException {
        Executor exec = new Executor(getConnection());
        Boolean flag_table  =
                exec.execQuery(CHECK_TABLE,(result)->{
                    try {
                        if (result.next()) return Boolean.TRUE;
                        else return Boolean.FALSE;
                    }catch (SQLException e){
                        DataBaseException se = new DataBaseException("error request!");
                        se.initCause(e);
                        throw se;
                        //return Boolean.FALSE;
                    }
        }
        );
        if (flag_table  ==  Boolean.FALSE){
            Executor exec_upd = new Executor(getConnection());
            exec_upd.execUpdate(CREATE_TABLE,(statement)->{
                try {
                    statement.executeUpdate();
                } catch (SQLException e) {
                    DataBaseException se = new DataBaseException("Create table error");
                    se.initCause(e);
                    throw se;
                }
            });
        }
    }
}
