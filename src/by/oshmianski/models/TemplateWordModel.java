package by.oshmianski.models;

import by.oshmianski.objects.TemplateWord;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:34 AM
 */
public class TemplateWordModel implements WritableTableFormat<TemplateWord> {
    private String[] colsTitle = {"Название", "Описание"};

    private FilterList<TemplateWord> items;

    public TemplateWordModel(FilterList<TemplateWord> filteredEntries) {
        this.items = filteredEntries;
    }

    @Override
    public int getColumnCount() {
        return colsTitle.length;
    }

    @Override
    public String getColumnName(int i) {
        return colsTitle[i];
    }

    @Override
    public Object getColumnValue(TemplateWord item, int i) {
        switch (i) {
            case 0:
                return item.getTitle();
            case 1:
                return item.getDescription();
            default:
                return null;
        }
    }


    @Override
    public boolean isEditable(TemplateWord item, int i) {
        return false;
    }

    @Override
    public TemplateWord setColumnValue(TemplateWord item, Object o, int i) {
        return null;
    }
}
