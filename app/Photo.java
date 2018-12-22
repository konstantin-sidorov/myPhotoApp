package myPhotoApp.app;

import java.io.InputStream;
import java.util.Objects;

public class Photo {
    private String name;
    private InputStream photo;

    public Photo(String name, InputStream photo) {
        this.name = name;
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public InputStream getPhoto() {
        return photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo1 = (Photo) o;
        return Objects.equals(name, photo1.name) &&
                Objects.equals(photo, photo1.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, photo);
    }
}
