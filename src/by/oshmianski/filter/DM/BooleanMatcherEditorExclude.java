package by.oshmianski.filter.DM;

import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.FilterComponent;
import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class BooleanMatcherEditorExclude extends AbstractMatcherEditor implements FilterComponent<DataMainItem> {

    private JCheckBox excludeDraft = new JCheckBox("показать");

    private DockingContainer container;

    private JPanel panel;

    final private ChangeListener changeListener;
    private boolean editorValue = true;
    private FilteratorDMExclude filteratorDMExclude;

    public BooleanMatcherEditorExclude(FilteratorDMExclude filteratorDMExclude, final DockingContainer container) {
        this.container = container;
        this.filteratorDMExclude = filteratorDMExclude;

        panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        FormLayout layout = new FormLayout(
                "5px, left:pref, 5px", // columns
                "2px, 23px, 2px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.add(excludeDraft, cc.xy(2, 2));

        panel.add(builder.getPanel(), BorderLayout.CENTER);

        changeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                editorValueChanged(e);
            }
        };
        excludeDraft.addChangeListener(changeListener);


        addMatcherEditorListener(new Listener<DataMainItem>() {
            @Override
            public void changedMatcher(Event<DataMainItem> dataMainItemEvent) {
                if (container.getUIProcessor() != null) {
                    container.getUIProcessor().setFilteredCount();
                }
            }
        });

    }

    private void editorValueChanged(ChangeEvent e) {
        editorValue = ((JCheckBox) e.getSource()).isSelected();
        if (!editorValue) {
            fireMatchAll();
        } else {
            final BooleanMatcher newMatcher = new BooleanMatcher(filteratorDMExclude); //put filterator !!!
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    fireChanged(newMatcher);
                }
            };

            SwingUtilities.invokeLater(r);
        }


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
        return "Исключения";
    }
}