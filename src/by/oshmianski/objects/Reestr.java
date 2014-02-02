package by.oshmianski.objects;

/**
 * Created by oshmianski on 01.02.14.
 */
public class Reestr {
    private String title;

    public Reestr(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
