package by.oshmianski.models;

import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:34 AM
 */
public class DataMainModel implements WritableTableFormat<DataMainItem> {
    private String[] colsTitle = {"#", "ФИО", "Лицевые счета", "Сумма долга", "Сумма вознаграждения", "Черновик", "Исключить"};

    private FilterList<DataMainItem> filteredEntries;

    public DataMainModel(FilterList<DataMainItem> filteredEntries) {
        this.filteredEntries = filteredEntries;
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
    public Object getColumnValue(DataMainItem dataMainItem, int i) {
        switch (i) {
            case 0:
                return filteredEntries.indexOf(dataMainItem) + 1;
            case 1:
                return dataMainItem.getFio();
            case 2:
                return dataMainItem.getAccounts();
            case 3:
                return dataMainItem.getSumDebt();
            case 4:
                return dataMainItem.getSumSalary();
            case 5:
                return dataMainItem.isDraft();
            case 6:
                return dataMainItem.isExclude();
            default:
                return null;
        }
    }


    @Override
    public boolean isEditable(DataMainItem dataMainItem, int i) {
        return false;
    }

    @Override
    public DataMainItem setColumnValue(DataMainItem dataMainItem, Object o, int i) {
        return null;
    }
}
