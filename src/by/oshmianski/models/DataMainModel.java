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
    private String[] colsTitle = {"#", "ФИО", "Лицевые счета", "Сумма начального долга", "Сумма долга", "Сумма начального вознаг-я", "Сумма вознаграждения", "Черновик", "Исключить", "Нумерованная", "Номер ДП"};

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
                return dataMainItem.getSumDebtBegin();
            case 4:
                return dataMainItem.getSumDebt();
            case 5:
                return dataMainItem.getSumSalaryBegin();
            case 6:
                return dataMainItem.getSumSalary();
            case 7:
                return dataMainItem.isDraft();
            case 8:
                return dataMainItem.isExclude();
            case 9:
                return dataMainItem.isNumbered();
            case 10:
                return dataMainItem.getNumExport();
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
