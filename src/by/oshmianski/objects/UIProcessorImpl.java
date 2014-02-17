package by.oshmianski.objects;

import by.oshmianski.docks.*;
import by.oshmianski.ui.edt.UIProcessor;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 2:35 PM
 */
public class UIProcessorImpl implements UIProcessor {
    private DockInfo dockInfo;
    private DockDataMainFilter dockDataMainFilter;
    private DockDataMain dockDataMain;
    private DockReestrs dockReestrs;

    public UIProcessorImpl(DockInfo dockInfo, DockDataMainFilter dockDataMainFilter, DockDataMain dockDataMain, DockReestrs dockReestrs) {
        this.dockInfo = dockInfo;
        this.dockDataMainFilter = dockDataMainFilter;
        this.dockDataMain = dockDataMain;
        this.dockReestrs = dockReestrs;
    }

    @Override
    public void setFilteredCount() {
        dockDataMain.setDockTitle();
        setInfoData(dockDataMain.getFilteredEntries());
    }

    @Override
    public void appendDataMainItem(DataMainItem item) {
        dockDataMain.appendDataMainItem(item);
    }

    @Override
    public void setProgressLabelText(String text) {
        dockInfo.setProgressLabelText(text);
    }

    @Override
    public void setProgressValue(int count) {
        dockInfo.setProgressValue(count);
    }

    @Override
    public void setProgressMaximum(int maximum) {
        dockInfo.setProgressMaximum(maximum);
    }

    @Override
    public void setInfoData(FilterList<DataMainItem> itemsFilter) {
        dockInfo.setInfoData(itemsFilter);
    }

    @Override
    public void setInfoDataGeneral(int count, BigDecimal sumDebt, BigDecimal sumSalary) {
        dockInfo.setInfoDataGeneral(count, sumDebt, sumSalary);
    }

    @Override
    public void appendReestr(Reestr reestr) {
        dockReestrs.appendReestr(reestr);
    }

    @Override
    public void clearDataMainItems() {
        dockDataMain.clearDataMain();
    }

    @Override
    public void clearReestrItems() {
        dockReestrs.clearItems();
    }

    @Override
    public void setDataDockReestr(String reestrTitle) {
        dockDataMain.setReestr(reestrTitle);
    }

    @Override
    public void setStartEnable(boolean enable) {
        dockReestrs.setStartEnable(enable);
    }

    @Override
    public int getSumType() {
        return dockReestrs.getSumType();
    }

    @Override
    public void setButtonLoadReestrsEnable(boolean enable) {
        dockReestrs.setButtonLoadReestrsEnable(enable);
    }
}
