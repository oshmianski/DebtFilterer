package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.utils.IconContainer;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockInfo extends DockSimple {
    private JProgressBar progress;
    private JLabel allRows;
    private JLabel allRowsFilter;
    private JLabel allSumDebtFilter;
    private JLabel allSumDSalaryFilter;
    private JLabel allSumDebtFilterMid;
    private JLabel allSumDSalaryFilterMid;
    private JLabel progressLabel;

    private DecimalFormat formatNumber = new DecimalFormat("###,##0");

    public DockInfo() {
        super("DockInfo", IconContainer.getInstance().loadImage("info.png"), "Информация");

        allRows = new JLabel("0");
        allRowsFilter = new JLabel("0");
        allSumDebtFilter = new JLabel("0");
        allSumDSalaryFilter = new JLabel("0");
        allSumDebtFilterMid = new JLabel("0");
        allSumDSalaryFilterMid = new JLabel("0");
        progress = new JProgressBar();
        progress.setStringPainted(true);
        progressLabel = new JLabel("Описание процесса");
        progressLabel.setFont(new Font("Tahoma", Font.PLAIN, 9));
        progressLabel.setForeground(Color.DARK_GRAY);

        FormLayout layout = new FormLayout(
                "5px, right:200px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 10px, 30px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Количество претензий", cc.xyw(1, 1, 3));
        builder.add(allRows, cc.xy(2, 2));
        builder.addSeparator("[ф] Количество претензий", cc.xyw(1, 3, 3));
        builder.add(allRowsFilter, cc.xy(2, 4));
        builder.addSeparator("[ф] Сумма долга", cc.xyw(1, 5, 3));
        builder.add(allSumDebtFilter, cc.xy(2, 6));
        builder.addSeparator("[ф] Сумма вознаграждения", cc.xyw(1, 7, 3));
        builder.add(allSumDSalaryFilter, cc.xy(2, 8));
        builder.addSeparator("[ф] Сумма долга ср.", cc.xyw(1, 9, 3));
        builder.add(allSumDebtFilterMid, cc.xy(2, 10));
        builder.addSeparator("[ф] Сумма вознаграждения ср.", cc.xyw(1, 11, 3));
        builder.add(allSumDSalaryFilterMid, cc.xy(2, 12));
        builder.addSeparator("Процесс", cc.xyw(1, 13, 3));
        builder.add(progressLabel, cc.xyw(1, 14, 3));
        builder.add(progress, cc.xyw(1, 15, 3));

        panel.add(builder.getPanel());
    }

    public void setProgressMaximum(int count) {
        progress.setMaximum(count);
    }

    public void setProgressValue(int value) {
        progress.setValue(value);
    }

    public void setProgressLabelText(String text) {
        progressLabel.setText(text);
    }

    public void dispose() {

    }

    public void setInfoData(EventList<DataMainItem> items, FilterList<DataMainItem> itemsFilter) {
        BigDecimal sumDebtAll = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumSalaryAll = new BigDecimal(BigInteger.ZERO);

        BigDecimal count = new BigDecimal(itemsFilter.size());

        for (DataMainItem item : itemsFilter) {
            sumDebtAll = sumDebtAll.add(item.getSumDebt());
            sumSalaryAll = sumSalaryAll.add(item.getSumSalary());
        }

        allRows.setText(formatNumber.format(items.size()));
        allRowsFilter.setText(formatNumber.format(itemsFilter.size()));
        allSumDebtFilter.setText(formatNumber.format(sumDebtAll));
        allSumDSalaryFilter.setText(formatNumber.format(sumSalaryAll));

        if (itemsFilter.size() > 0) {
            allSumDebtFilterMid.setText(formatNumber.format(sumDebtAll.divide(count, 2, RoundingMode.HALF_UP)));
            allSumDSalaryFilterMid.setText(formatNumber.format(sumSalaryAll.divide(count, 2, RoundingMode.HALF_UP)));
        }
    }
}
