package by.oshmianski.ui.edt;

import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.Reestr;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 05.04.13
 * Time: 9:59
 */
public interface UIProcessor {
    @RequiresEDT
    void setFilteredCount();

    @RequiresEDT
    void appendDataMainItem(DataMainItem item);

    @RequiresEDT
    void appendReestr(Reestr reestr);

    @RequiresEDT
    void setProgressLabelText(String text);

    @RequiresEDT
    void setProgressValue(int count);

    @RequiresEDT
    void setProgressMaximum(int maximum);

    @RequiresEDT
    void setInfoData(EventList<DataMainItem> items, FilterList<DataMainItem> itemsFilter);

    @RequiresEDT
    void clearDataMainItems();

    @RequiresEDT
    void clearReestrItems();

    @RequiresEDT
    void setDataDockReestr(String reestrTitle);

    @RequiresEDT
    void setStartEnable();

    @RequiresEDT
    void setButtonLoadReestrsEnable(boolean enable);

    int getSumType();
}
