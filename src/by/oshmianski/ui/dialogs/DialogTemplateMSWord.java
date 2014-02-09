package by.oshmianski.ui.dialogs;

import by.oshmianski.loaders.LoadTemplatesMSWord;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.TemplateWordModel;
import by.oshmianski.objects.TemplateWord;
import by.oshmianski.ui.utils.ActionButton;
import by.oshmianski.ui.utils.BetterJTable;
import by.oshmianski.ui.utils.ColorRenderer;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.gui.AbstractTableComparatorChooser;
import ca.odell.glazedlists.swing.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by oshmianski on 09.02.14.
 */
public class DialogTemplateMSWord extends JDialog implements ActionListener {
    private ActionButton ok;
    private ActionButton cancel;

    private EventList<TemplateWord> items = new BasicEventList<TemplateWord>();
    private EventList<TemplateWord> entries;
    private SortedList<TemplateWord> sortedEntries;
    private FilterList<TemplateWord> filteredEntries;
    private JTextField filterEdit;

    private TextComponentMatcherEditor<TemplateWord> textComponentMatcherEditor;
    private DefaultEventTableModel<TemplateWord> model;
    private DefaultEventSelectionModel selectionModel;

    private TemplateWord templateWordSelected;
    private boolean isCanceled = true;
    private BetterJTable table;

    public DialogTemplateMSWord(JFrame owner) {
        super(owner, true);
        setTitle("Выбор шаблона экспорта MS Word");

        JScrollPane sp = null;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                hideDialog();
            }
        });

        KeyStroke strokeCtrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK);
        Action escapeCtrlW = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                hideDialog();
            }
        };
        InputMap inputMapCtrlW = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMapCtrlW.put(strokeCtrlW, "ctrlW");
        rootPane.getActionMap().put("ctrlW", escapeCtrlW);

        Dimension dialogSize = new Dimension(400, 300);
        setSize(dialogSize);

        JPanel panelMain = (JPanel) getContentPane();
        panelMain.setLayout(new BorderLayout());
        panelMain.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

        ok = new ActionButton("Ok", null, new Dimension(80, 30), "Ok");
        ok.addActionListener(this);

        cancel = new ActionButton("Отмена", null, new Dimension(80, 30), "Отмена");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideDialog();

                isCanceled = true;
            }
        });

        filterEdit = new JTextField(15);
        table = new BetterJTable(null, true);

        entries = GlazedListsSwing.swingThreadProxyList(items);

        textComponentMatcherEditor = new TextComponentMatcherEditor<TemplateWord>(filterEdit, GlazedLists.textFilterator(TemplateWord.class, "title"), true);

        sortedEntries = new SortedList<TemplateWord>(entries, GlazedLists.chainComparators(GlazedLists.beanPropertyComparator(TemplateWord.class, "title")));
        filteredEntries = new FilterList<TemplateWord>(sortedEntries, textComponentMatcherEditor);

        model = new DefaultEventTableModel<TemplateWord>(filteredEntries, new TemplateWordModel(filteredEntries));
        table.setModel(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    hideDialog();

                    isCanceled = false;
                }
            }
        });

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(450);
        table.getColumnModel().getColumn(1).setPreferredWidth(450);

        table.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer(Color.BLACK, false));
        table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLUE, false));

        table.setRowHeight(20);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
        table.setSelectionBackground(new Color(217, 235, 245));
        table.setSelectionForeground(Color.BLACK);

        TableComparatorChooser.install(table, sortedEntries, AbstractTableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);

        selectionModel = new DefaultEventSelectionModel(filteredEntries);
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setSelectionModel(selectionModel);


        selectionModel = new DefaultEventSelectionModel<TemplateWord>(filteredEntries);
        table.setSelectionModel(selectionModel);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sp = new NiceScrollPane(table);
        sp.setPreferredSize(new Dimension(600, 300));

        sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
        sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

        FormLayout layoutButton = new FormLayout(
                "20px, left:90px, 10px, left:pref, 5px", // columns
                "2px, 40px, 2px");      // rows

//            FormDebugPanel debugPanel = new FormDebugPanel();
//            PanelBuilder builderButton = new PanelBuilder(layoutButton, debugPanel);
        PanelBuilder builderButton = new PanelBuilder(layoutButton);

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints ccButton = new CellConstraints();

        builderButton.add(ok, ccButton.xy(2, 2));
        builderButton.add(cancel, ccButton.xy(4, 2));

        panelMain.add(builderButton.getPanel(), BorderLayout.SOUTH);
        panelMain.add(sp, BorderLayout.CENTER);

        loadTemplatesWord();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (isDialogDataCorrect()) {
            hideDialog();

            isCanceled = false;
        }
    }

    private void hideDialog() {
        setVisible(false);
        items.dispose();
    }

    public void loadTemplatesWord() {
        try {
            LoadTemplatesMSWord loader = new LoadTemplatesMSWord(items);
            loader.execute();
        } catch (Exception ex) {
            MyLog.add2Log(ex);
        }
    }

    public boolean isDialogDataCorrect() {

        try {


            return true;
        } catch (Exception ex) {
            MyLog.add2Log(ex);

            return false;
        }
    }

    public String getSelectedTemplateUnid(){
        String ret = "";

        if (selectionModel.getSelected().size() > 0) {
            Object selectedObject = selectionModel.getSelected().get(0);
            if (selectedObject instanceof TemplateWord) {
                ret = ((TemplateWord) selectedObject).getUnid();
            }
        }

        return ret;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
