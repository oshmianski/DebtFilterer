package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.main.AppletWindowFrame;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.ui.utils.NumberLabel;
import by.oshmianski.utils.IconContainer;
import ca.odell.glazedlists.FilterList;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private JLabel progressLabel;

    private NumberLabel allRows;
    private NumberLabel allSumDebt;
    private NumberLabel allSumSalary;
    private NumberLabel allSumDebtMid;
    private NumberLabel allSumSalaryMid;
    private NumberLabel allSumDebtBegin;
    private NumberLabel allSumSalaryBegin;
    private NumberLabel allSumDebtMidBegin;
    private NumberLabel allSumSalaryMidBegin;

    private NumberLabel allRowsFilter;
    private NumberLabel allSumDebtFilter;
    private NumberLabel allSumSalaryFilter;
    private NumberLabel allSumDebtFilterMid;
    private NumberLabel allSumSalaryFilterMid;
    private NumberLabel allSumDebtFilterBegin;
    private NumberLabel allSumSalaryFilterBegin;
    private NumberLabel allSumDebtFilterMidBegin;
    private NumberLabel allSumSalaryFilterMidBegin;

    private NumberLabel sumExtDebt;
    private NumberLabel sumExtSalary;
    private NumberLabel sumExtDebtBegin;
    private NumberLabel sumExtSalaryBegin;

    BigDecimal sumDebt = new BigDecimal(BigInteger.ZERO);
    BigDecimal sumSalary = new BigDecimal(BigInteger.ZERO);
    BigDecimal sumDebtBegin = new BigDecimal(BigInteger.ZERO);
    BigDecimal sumSalaryBegin = new BigDecimal(BigInteger.ZERO);
    int count = 0;

    public DockInfo() {
        super("DockInfo", IconContainer.getInstance().loadImage("info.png"), "Информация");

        allRows = new NumberLabel();
        allSumDebt = new NumberLabel();
        allSumSalary = new NumberLabel();
        allSumDebtMid = new NumberLabel();
        allSumSalaryMid = new NumberLabel();
        allSumDebtBegin = new NumberLabel();
        allSumSalaryBegin = new NumberLabel();
        allSumDebtMidBegin = new NumberLabel();
        allSumSalaryMidBegin = new NumberLabel();

        allRowsFilter = new NumberLabel();
        allSumDebtFilter = new NumberLabel();
        allSumSalaryFilter = new NumberLabel();
        allSumDebtFilterMid = new NumberLabel();
        allSumSalaryFilterMid = new NumberLabel();
        allSumDebtFilterBegin = new NumberLabel();
        allSumSalaryFilterBegin = new NumberLabel();
        allSumDebtFilterMidBegin = new NumberLabel();
        allSumSalaryFilterMidBegin = new NumberLabel();

        sumExtDebt = new NumberLabel(new DecimalFormat("###,##0.###"));
        sumExtSalary = new NumberLabel(new DecimalFormat("###,##0.###"));
        sumExtDebtBegin = new NumberLabel(new DecimalFormat("###,##0.###"));
        sumExtSalaryBegin = new NumberLabel(new DecimalFormat("###,##0.###"));

        progress = new JProgressBar();
        progress.setStringPainted(true);
        progressLabel = new JLabel("Описание процесса");
        progressLabel.setFont(new Font("Tahoma", Font.PLAIN, 9));
        progressLabel.setForeground(Color.DARK_GRAY);

        JPanel panelInner = new JPanel(new BorderLayout());
        panelInner.setBackground(AppletWindowFrame.REPORT_PANEL_BACKGROUND);

        panelInner.add(getGeneralPanel(), BorderLayout.NORTH);
        panelInner.add(getFilterPanel(), BorderLayout.CENTER);
        panelInner.add(getExtPanel(), BorderLayout.SOUTH);

        panel.add(panelInner, BorderLayout.CENTER);
        panel.add(getProgressPanel(), BorderLayout.SOUTH);
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

    public void setInfoDataGeneral(int count, BigDecimal sumDebt, BigDecimal sumSalary, BigDecimal sumDebtBegin, BigDecimal sumSalaryBegin) {
        allRows.setText(count);
        allSumDebt.setText(sumDebt);
        allSumSalary.setText(sumSalary);
        allSumDebtBegin.setText(sumDebtBegin);
        allSumSalaryBegin.setText(sumSalaryBegin);

        this.count = count;
        this.sumDebt = sumDebt;
        this.sumSalary = sumSalary;
        this.sumDebtBegin = sumDebtBegin;
        this.sumSalaryBegin = sumSalaryBegin;

        if (count > 0) {
            allSumDebtMid.setText(sumDebt.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP));
            allSumSalaryMid.setText(sumSalary.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP));
            allSumDebtMidBegin.setText(sumDebtBegin.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP));
            allSumSalaryMidBegin.setText(sumSalaryBegin.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP));
        } else {
            allSumDebtMid.setText("0");
            allSumSalaryMid.setText("0");
            allSumDebtMidBegin.setText("0");
            allSumSalaryMidBegin.setText("0");
        }
    }

    public void setInfoData(FilterList<DataMainItem> itemsFilter) {
        BigDecimal sumDebtFilter = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumSalaryFilter = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumDebtFilterBegin = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumSalaryFilterBegin = new BigDecimal(BigInteger.ZERO);

        BigDecimal count = new BigDecimal(itemsFilter.size());

        for (DataMainItem item : itemsFilter) {
            sumDebtFilter = sumDebtFilter.add(item.getSumDebt());
            sumSalaryFilter = sumSalaryFilter.add(item.getSumSalary());
            sumDebtFilterBegin = sumDebtFilterBegin.add(item.getSumDebtBegin());
            sumSalaryFilterBegin = sumSalaryFilterBegin.add(item.getSumSalaryBegin());
        }

        allRowsFilter.setText(itemsFilter.size());
        allSumDebtFilter.setText(sumDebtFilter);
        allSumSalaryFilter.setText(sumSalaryFilter);
        allSumDebtFilterBegin.setText(sumDebtFilterBegin);
        allSumSalaryFilterBegin.setText(sumSalaryFilterBegin);

        if (itemsFilter.size() > 0) {
            allSumDebtFilterMid.setText(sumDebtFilter.divide(count, 4, RoundingMode.HALF_UP));
            allSumSalaryFilterMid.setText(sumSalaryFilter.divide(count, 4, RoundingMode.HALF_UP));
            allSumDebtFilterMidBegin.setText(sumDebtFilterBegin.divide(count, 4, RoundingMode.HALF_UP));
            allSumSalaryFilterMidBegin.setText(sumSalaryFilterBegin.divide(count, 4, RoundingMode.HALF_UP));
        } else {
            allSumDebtFilterMid.setText("0");
            allSumSalaryFilterMid.setText("0");
            allSumDebtFilterMidBegin.setText("0");
            allSumSalaryFilterMidBegin.setText("0");
        }

        if (!(sumDebt.compareTo(BigDecimal.ZERO) == 0)) {
            sumExtDebt.setText(sumDebtFilter.divide(sumDebt, 4, RoundingMode.HALF_UP));
        } else {
            sumExtDebt.setText("1");
        }

        if (!(sumDebtBegin.compareTo(BigDecimal.ZERO) == 0)) {
            sumExtDebtBegin.setText(sumDebtFilterBegin.divide(sumDebtBegin, 4, RoundingMode.HALF_UP));
        } else {
            sumExtDebtBegin.setText("1");
        }

        if (!(sumSalary.compareTo(BigDecimal.ZERO) == 0)) {
            sumExtSalary.setText(sumSalaryFilter.divide(sumSalary, 4, RoundingMode.HALF_UP));
        } else {
            sumExtSalary.setText("1");
        }
        if (!(sumSalaryBegin.compareTo(BigDecimal.ZERO) == 0)) {
            sumExtSalaryBegin.setText(sumSalaryFilterBegin.divide(sumSalaryBegin, 4, RoundingMode.HALF_UP));
        } else {
            sumExtSalaryBegin.setText("1");
        }
    }

    private JPanel getGeneralPanel() {
        FormLayout layout = new FormLayout(
                "2px, right:180px, 5px, left:90px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 10px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Количество претензий:", cc.xy(2, 1));
        builder.add(allRows, cc.xy(4, 1));
        builder.addLabel("Сумма долга:", cc.xy(2, 2));
        builder.add(allSumDebt, cc.xy(4, 2));
        builder.addLabel("Сумма вознаграждения:", cc.xy(2, 3));
        builder.add(allSumSalary, cc.xy(4, 3));
        builder.addLabel("Сумма долга ср.:", cc.xy(2, 4));
        builder.add(allSumDebtMid, cc.xy(4, 4));
        builder.addLabel("Сумма вознаграждения ср.:", cc.xy(2, 5));
        builder.add(allSumSalaryMid, cc.xy(4, 5));
        builder.addLabel("Сумма долга (Н):", cc.xy(2, 6));
        builder.add(allSumDebtBegin, cc.xy(4, 6));
        builder.addLabel("Сумма вознаграждения (Н):", cc.xy(2, 7));
        builder.add(allSumSalaryBegin, cc.xy(4, 7));
        builder.addLabel("Сумма долга ср. (Н):", cc.xy(2, 8));
        builder.add(allSumDebtMidBegin, cc.xy(4, 8));
        builder.addLabel("Сумма вознаграждения ср. (Н):", cc.xy(2, 9));
        builder.add(allSumSalaryMidBegin, cc.xy(4, 9));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x9297A1)),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ), "Осн. информация", TitledBorder.LEFT, TitledBorder.TOP)));
        return panel;
    }

    private JPanel getFilterPanel() {
        FormLayout layout = new FormLayout(
                "2px, right:180px, 5px, left:90px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 10px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Количество претензий:", cc.xy(2, 1));
        builder.add(allRowsFilter, cc.xy(4, 1));
        builder.addLabel("Сумма долга:", cc.xy(2, 2));
        builder.add(allSumDebtFilter, cc.xy(4, 2));
        builder.addLabel("Сумма вознаграждения:", cc.xy(2, 3));
        builder.add(allSumSalaryFilter, cc.xy(4, 3));
        builder.addLabel("Сумма долга ср.:", cc.xy(2, 4));
        builder.add(allSumDebtFilterMid, cc.xy(4, 4));
        builder.addLabel("Сумма вознаграждения ср.:", cc.xy(2, 5));
        builder.add(allSumSalaryFilterMid, cc.xy(4, 5));
        builder.addLabel("Сумма долга (Н):", cc.xy(2, 6));
        builder.add(allSumDebtFilterBegin, cc.xy(4, 6));
        builder.addLabel("Сумма вознаграждения (Н):", cc.xy(2, 7));
        builder.add(allSumSalaryFilterBegin, cc.xy(4, 7));
        builder.addLabel("Сумма долга ср. (Н):", cc.xy(2, 8));
        builder.add(allSumDebtFilterMidBegin, cc.xy(4, 8));
        builder.addLabel("Сумма вознаграждения ср. (Н):", cc.xy(2, 9));
        builder.add(allSumSalaryFilterMidBegin, cc.xy(4, 9));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x9297A1)),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ), "Фильтр", TitledBorder.LEFT, TitledBorder.TOP)));
        return panel;
    }

    private JPanel getExtPanel() {
        FormLayout layout = new FormLayout(
                "2px, right:180px, 5px, left:90px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 10px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Отношение долга:", cc.xy(2, 1));
        builder.add(sumExtDebt, cc.xy(4, 1));
        builder.addLabel("Отношение вознагр-я:", cc.xy(2, 2));
        builder.add(sumExtSalary, cc.xy(4, 2));
        builder.addLabel("Отношение долга (Н):", cc.xy(2, 3));
        builder.add(sumExtDebtBegin, cc.xy(4, 3));
        builder.addLabel("Отношение вознагр-я (Н):", cc.xy(2, 4));
        builder.add(sumExtSalaryBegin, cc.xy(4, 4));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x9297A1)),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ), "Дополнительно", TitledBorder.LEFT, TitledBorder.TOP)));
        return panel;
    }

    private JPanel getProgressPanel() {
        FormLayout layout = new FormLayout(
                "5px, right:250px, 5px", // columns
                "2px, 20px, 10px, 30px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Процесс", cc.xyw(1, 2, 3));
        builder.add(progressLabel, cc.xyw(1, 3, 3));
        builder.add(progress, cc.xyw(1, 4, 3));

        return builder.getPanel();
    }
}
