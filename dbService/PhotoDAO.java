package myPhotoApp.dbService;


import myPhotoApp.app.Photo;
import myPhotoApp.db.DBPrepared;
import myPhotoApp.db.DataBaseException;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhotoDAO {
    DBPrepared dbPrepared;

    public PhotoDAO() throws DataBaseException, SQLException {
        DBPrepared dbPrepared = new DBPrepared();
        dbPrepared.prepareTables();
    }

    public PhotoDAO(DBPrepared dbPrepared) {
        this.dbPrepared = dbPrepared;
    }

    public void add_picture(Photo myPhoto) throws DataBaseException {
     String name;
     InputStream photo;
        if (myPhoto != null) {
            name    =   myPhoto.getName();
            photo   =   myPhoto.getPhoto();
            if (dbPrepared.load_photo(name) == Boolean.FALSE) {
                dbPrepared.add_picture(name, photo);
            }
        }
    }
    public ArrayList<Photo> get_name_photos() throws DataBaseException {
        ArrayList<String> list_photos   =   dbPrepared.load_name();
        ArrayList<Photo> list_object    =   new ArrayList<>();
        for (String list:
             list_photos) {
            list_object.add(new Photo(list,null));
        }
        return  list_object;

    }
    public InputStream get_one_photo(String name) throws DataBaseException {
        return dbPrepared.load_one_photo(name);
    }
    public void close_connect() throws Exception {
        dbPrepared.close();
    }
}
