package by.oshmianski.objects;

/**
 * Created by oshmianski on 09.02.14.
 */
public class TemplateWord {
    String unid;
    String title;
    String description;

    public TemplateWord(String unid, String title, String description) {
        this.unid = unid;
        this.title = title;
        this.description = description;
    }

    public String getUnid() {
        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title;
    }
}
