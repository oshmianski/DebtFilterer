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
    private NumberLabel allSumDSalary;
    private NumberLabel allSumDebtMid;
    private NumberLabel allSumDSalaryMid;

    private NumberLabel allRowsFilter;
    private NumberLabel allSumDebtFilter;
    private NumberLabel allSumDSalaryFilter;
    private NumberLabel allSumDebtFilterMid;
    private NumberLabel allSumDSalaryFilterMid;

    private NumberLabel sumExtDebt;
    private NumberLabel sumExtSalary;

    BigDecimal sumDebt = new BigDecimal(BigInteger.ZERO);
    BigDecimal sumSalary = new BigDecimal(BigInteger.ZERO);
    int count = 0;

    public DockInfo() {
        super("DockInfo", IconContainer.getInstance().loadImage("info.png"), "Информация");

        allRows = new NumberLabel();
        allSumDebt = new NumberLabel();
        allSumDSalary = new NumberLabel();
        allSumDebtMid = new NumberLabel();
        allSumDSalaryMid = new NumberLabel();

        allRowsFilter = new NumberLabel();
        allSumDebtFilter = new NumberLabel();
        allSumDSalaryFilter = new NumberLabel();
        allSumDebtFilterMid = new NumberLabel();
        allSumDSalaryFilterMid = new NumberLabel();

        sumExtDebt = new NumberLabel(new DecimalFormat("###,##0.###"));
        sumExtSalary = new NumberLabel(new DecimalFormat("###,##0.###"));

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

    public void setInfoDataGeneral(int count, BigDecimal sumDebt, BigDecimal sumSalary) {
        allRows.setText(count);
        allSumDebt.setText(sumDebt);
        allSumDSalary.setText(sumSalary);

        this.count = count;
        this.sumDebt = sumDebt;
        this.sumSalary = sumSalary;

        if (count > 0) {
            allSumDebtMid.setText(sumDebt.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP));
            allSumDSalaryMid.setText(sumSalary.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP));
        } else {
            allSumDebtMid.setText("0");
            allSumDSalaryMid.setText("0");
        }
    }

    public void setInfoData(FilterList<DataMainItem> itemsFilter) {
        BigDecimal sumDebtFilter = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumSalaryFilter = new BigDecimal(BigInteger.ZERO);

        BigDecimal count = new BigDecimal(itemsFilter.size());

        for (DataMainItem item : itemsFilter) {
            sumDebtFilter = sumDebtFilter.add(item.getSumDebt());
            sumSalaryFilter = sumSalaryFilter.add(item.getSumSalary());
        }

        allRowsFilter.setText(itemsFilter.size());
        allSumDebtFilter.setText(sumDebtFilter);
        allSumDSalaryFilter.setText(sumSalaryFilter);

        if (itemsFilter.size() > 0) {
            allSumDebtFilterMid.setText(sumDebtFilter.divide(count, 2, RoundingMode.HALF_UP));
            allSumDSalaryFilterMid.setText(sumSalaryFilter.divide(count, 2, RoundingMode.HALF_UP));
        } else {
            allSumDebtFilterMid.setText("0");
            allSumDSalaryFilterMid.setText("0");
        }

        if (!(sumDebt.compareTo(BigDecimal.ZERO) == 0)) {
            sumExtDebt.setText(sumDebtFilter.divide(sumDebt, 2, RoundingMode.HALF_UP));
        } else {
            sumExtDebt.setText("1");
        }

        if (!(sumSalary.compareTo(BigDecimal.ZERO) == 0)) {
            sumExtSalary.setText(sumSalaryFilter.divide(sumSalary, 2, RoundingMode.HALF_UP));
        } else {
            sumExtSalary.setText("1");
        }
    }

    private JPanel getGeneralPanel() {
        FormLayout layout = new FormLayout(
                "2px, right:160px, 5px, left:90px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 10px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Количество претензий:", cc.xy(2, 1));
        builder.add(allRows, cc.xy(4, 1));
        builder.addLabel("Сумма долга:", cc.xy(2, 2));
        builder.add(allSumDebt, cc.xy(4, 2));
        builder.addLabel("Сумма вознаграждения:", cc.xy(2, 3));
        builder.add(allSumDSalary, cc.xy(4, 3));
        builder.addLabel("Сумма долга ср.:", cc.xy(2, 4));
        builder.add(allSumDebtMid, cc.xy(4, 4));
        builder.addLabel("Сумма вознаграждения ср.:", cc.xy(2, 5));
        builder.add(allSumDSalaryMid, cc.xy(4, 5));

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
                "2px, right:160px, 5px, left:90px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 10px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Количество претензий:", cc.xy(2, 1));
        builder.add(allRowsFilter, cc.xy(4, 1));
        builder.addLabel("Сумма долга:", cc.xy(2, 2));
        builder.add(allSumDebtFilter, cc.xy(4, 2));
        builder.addLabel("Сумма вознаграждения:", cc.xy(2, 3));
        builder.add(allSumDSalaryFilter, cc.xy(4, 3));
        builder.addLabel("Сумма долга ср.:", cc.xy(2, 4));
        builder.add(allSumDebtFilterMid, cc.xy(4, 4));
        builder.addLabel("Сумма вознаграждения ср.:", cc.xy(2, 5));
        builder.add(allSumDSalaryFilterMid, cc.xy(4, 5));

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
                "2px, right:160px, 5px, left:90px, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 10px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Отношение долга:", cc.xy(2, 1));
        builder.add(sumExtDebt, cc.xy(4, 1));
        builder.addLabel("Отношение вознагр-я:", cc.xy(2, 2));
        builder.add(sumExtSalary, cc.xy(4, 2));

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
