package myPhotoApp;

import myPhotoApp.db.DBPrepared;
import myPhotoApp.db.DataBaseException;
import myPhotoApp.dbService.PhotoDAO;
import myPhotoApp.gui.JavaFXApplication;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws DataBaseException {
        PhotoDAO    photoDAO    = null;
        try (DBPrepared db = new DBPrepared()) {
            db.prepareTables();
            photoDAO = new PhotoDAO(db);
            System.out.println("-" + photoDAO.get_name_photos());
        } catch (Exception e) {
            DataBaseException se = new DataBaseException("main error");
            se.initCause(e);
            throw se;
        }
    }
}
