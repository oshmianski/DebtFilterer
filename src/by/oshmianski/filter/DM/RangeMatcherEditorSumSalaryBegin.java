package by.oshmianski.filter.DM;

import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.FilterComponent;
import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.RangeMatcherEditor;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class RangeMatcherEditorSumSalaryBegin extends RangeMatcherEditor implements FilterComponent<DataMainItem> {

    private JTextField filterFrom = new JTextField(10);
    private JTextField filterTo = new JTextField(10);

    private DockingContainer container;

    private JPanel panel;

    public RangeMatcherEditorSumSalaryBegin(FilteratorDMSumSalaryBegin filteratorDMSumSalary, final DockingContainer container) {
        super(filteratorDMSumSalary);
        this.container = container;

        panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        FormLayout layout = new FormLayout(
                "5px, right:50px, 5px, left:pref, 5px", // columns
                "2px, 26px, 1px, 26px, 1px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("От:", cc.xy(2, 2));
        builder.add(filterFrom, cc.xy(4, 2));
        builder.addLabel("До:", cc.xy(2, 4));
        builder.add(filterTo, cc.xy(4, 4));

        panel.add(builder.getPanel(), BorderLayout.CENTER);

        addMatcherEditorListener(new Listener<DataMainItem>() {
            @Override
            public void changedMatcher(Event<DataMainItem> dataMainItemEvent) {
                if (container.getUIProcessor() != null) {
                    container.getUIProcessor().setFilteredCount();
                }
            }
        });

        filterFrom.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setFilterRange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setFilterRange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setFilterRange();
            }
        });

        filterTo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setFilterRange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setFilterRange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setFilterRange();
            }
        });
    }

    public JComponent getComponent() {
        return panel;
    }

    @Override
    public boolean isSelectDeselectAllVisible() {
        return false;
    }

    @Override
    public ActionListener getSelectAction() {
        return null;
    }

    @Override
    public ActionListener getDeselectAction() {
        return null;
    }

    @Override
    public MatcherEditor<DataMainItem> getMatcherEditor() {
        return this;
    }

    @Override
    public void dispose() {

    }

    public String toString() {
        return "Сумма вознаграждения (начальная)";
    }

    private void setFilterRange() {
        String fromStr = filterFrom.getText().length() == 1 ? filterFrom.getText().replace("-", "") : filterFrom.getText().replaceAll("\\s+", "").replaceAll("\\.", ",");
        String toStr = filterTo.getText().length() == 1 ? filterTo.getText().replace("-", "") : filterTo.getText().replaceAll("\\s+", "").replaceAll("\\.", ",");

        final BigDecimal from = fromStr.isEmpty() ? null : new BigDecimal(fromStr);
        final BigDecimal to = toStr.isEmpty() ? null : new BigDecimal(toStr);

        setRange(from, to);
    }
}