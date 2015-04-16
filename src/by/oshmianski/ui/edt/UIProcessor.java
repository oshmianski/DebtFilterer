package by.oshmianski.ui.edt;

import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.Reestr;
import ca.odell.glazedlists.FilterList;

import java.math.BigDecimal;

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
    void setInfoData(FilterList<DataMainItem> itemsFilter);

    @RequiresEDT
    void setInfoDataGeneral(int count, BigDecimal sumDebt, BigDecimal sumSalary, BigDecimal sumDebtBegin, BigDecimal sumSalaryBegin);

    @RequiresEDT
    void clearDataMainItems();

    @RequiresEDT
    void clearReestrItems();

    @RequiresEDT
    void setDataDockReestr(String reestrTitle);

    @RequiresEDT
    void setStartEnable(boolean enable);

    @RequiresEDT
    void setButtonLoadReestrsEnable(boolean enable);

    int getSumType();
}
