package myPhotoApp.gui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import myPhotoApp.app.Photo;
import myPhotoApp.db.DBPrepared;
import myPhotoApp.db.DataBaseException;
import myPhotoApp.dbService.PhotoDAO;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class mainController implements Initializable {
    private static ObservableList<Photo> items = FXCollections.observableArrayList();
    @FXML private TableView<Photo> tablePhotos;
    @FXML private TableColumn<Photo,String> listId;
    @FXML private ImageView boximage;
    private PhotoDAO photoDAO;
    private DBPrepared db;

    public mainController() throws SQLException, DataBaseException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initData();
        listId.setCellValueFactory(new PropertyValueFactory<Photo, String>("name"));
        //tablePhotos.setItems(items);
        items.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change obj) {
                tablePhotos.setItems(items);
            }
        });

    }
    @FXML
    protected void btnCloseClick(ActionEvent event) throws Exception {
        photoDAO.close_connect();


    }
    @FXML
    protected void btnFromDBClick(ActionEvent event) throws Exception {
        initData();
    }
    @FXML
    protected void btnSaveClick(ActionEvent event) throws IOException, DataBaseException {
        FileChooser fileChooser =   new FileChooser();
        fileChooser.setTitle("Open photo");
        fileChooser.getExtensionFilters().addAll(
                //new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
                //new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            //System.out.println(""+file.getAbsolutePath());
            InputStream input   =   new FileInputStream(file);
            Photo   newPhoto    =   new Photo(file.getName(),input);
            items.add(newPhoto);
            if (photoDAO!=null){
                photoDAO.add_picture(newPhoto);
            }
        }
    }
    @FXML
    protected void btnOpenClick(ActionEvent event) throws IOException, DataBaseException {
            // convert byte array back to BufferedImage
            Photo row = tablePhotos.getSelectionModel().getSelectedItem();
            if (row == null) return;
            InputStream input   =   row.getPhoto();
            if (input ==  null && photoDAO!=null) {
                //из Базы получаю картинку
                input =   photoDAO.get_one_photo(row.getName());
            }
            Image image = new Image(input);
            boximage.setImage(image);
    }
    private void initData() throws SQLException, DataBaseException {
        //items.add(new Photo("Sochi.txt", new byte[]{}));
        items.clear();
        boximage.setImage(null);
        db = new DBPrepared();
        photoDAO    =   new PhotoDAO(db);
        items.addAll(photoDAO.get_name_photos());
    }
}
