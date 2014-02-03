package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.DM.FilteratorReestr;
import by.oshmianski.loaders.LoadMainData;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.objects.Reestr;
import by.oshmianski.ui.utils.ActionButton;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockReestrs extends DockSimple {
    private DockingContainer dockingContainer;
    private EventList<Reestr> items = new BasicEventList<Reestr>();
    private EventList<Reestr> entries;
    private SortedList<Reestr> sortedEntries;
    private FilterList<Reestr> filteredEntries;
    private JList reestrs;
    private JTextField filterEdit;
    private TextComponentMatcherEditor<Reestr> textComponentMatcherEditor;
    private DefaultEventListModel<Reestr> listModel;
    private DefaultEventSelectionModel selectionModel;
    private JRadioButton r1;
    private JRadioButton r2;
    private JRadioButton r3;
    private ButtonGroup buttonGroup;
    private ActionButton buttonStart;
    private ActionButton buttonStop;
    private ActionButton loadReestrs;

    private final static String dockTitle = "Реестры";

    public DockReestrs(final DockingContainer dockingContainer) {
        super("DockReestrs", IconContainer.getInstance().loadImage("layers.png"), dockTitle);

        this.dockingContainer = dockingContainer;

        JScrollPane sp;
        TextFilterator<Reestr> fiteratorReestr = new FilteratorReestr();
        filterEdit = new JTextField(15);

        r1 = new JRadioButton("Сумма без учета платежей и допов", true);
        r2 = new JRadioButton("Сумма с учетом платежей", false);
        r3 = new JRadioButton("Сумма с учетом платежей и допов", false);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);
        buttonGroup.add(r3);

        loadReestrs = new ActionButton("", IconContainer.getInstance().loadImage("repeat.png"), new Dimension(25, 20), "Получить список реестров");
        loadReestrs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dockingContainer.getLoadReestrs().execute();
            }
        });

        buttonStart = new ActionButton("Старт", null, new Dimension(80, 30), "Запуск чтения реестра");
        buttonStop = new ActionButton("Стоп", null, new Dimension(80, 30), "Остановить чтение реестра");
        buttonStop.setEnabled(false);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionModel.getSelected().size() == 0) {
                    JOptionPane.showMessageDialog(null, "Не выбран реестр!");
                    return;
                }

                openReestr();
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopOpenReestr();
            }
        });


        items.getReadWriteLock().writeLock().lock();

        try {
            entries = GlazedListsSwing.swingThreadProxyList(items);

            textComponentMatcherEditor = new TextComponentMatcherEditor<Reestr>(filterEdit, fiteratorReestr, true);

            sortedEntries = new SortedList<Reestr>(entries, GlazedLists.chainComparators(GlazedLists.beanPropertyComparator(Reestr.class, "title")));
            filteredEntries = new FilterList<Reestr>(sortedEntries, textComponentMatcherEditor);

            listModel = new DefaultEventListModel<Reestr>(filteredEntries);
            reestrs = new JList(listModel);
//            reestrs.setPrototypeCellValue("jessewilson");
//            reestrs.setVisibleRowCount(7);
            reestrs.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        openReestr();
                    }
                }
            });


            selectionModel = new DefaultEventSelectionModel<Reestr>(filteredEntries);
            reestrs.setSelectionModel(selectionModel);
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            sp = new NiceScrollPane(reestrs);
            sp.setPreferredSize(new Dimension(1000, 1000));

            sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            FormLayout layout = new FormLayout(
                    "5px, right:50px, 5px, left:150px, 5px, 25px, 5px", // columns
                    "2px, 23px, 1px, top:300px, 10px, 20px, 2px, 20px, 2px, 20px, 2px");      // rows

//            FormDebugPanel debugPanel = new FormDebugPanel(layout);
//            PanelBuilder builder = new PanelBuilder(layout, debugPanel);
            PanelBuilder builder = new PanelBuilder(layout);

            // Obtain a reusable constraints object to place components in the grid.
            CellConstraints cc = new CellConstraints();

            builder.addLabel("Поиск:", cc.xy(2, 2));
            builder.add(filterEdit, cc.xy(4, 2));
            builder.add(loadReestrs, cc.xy(6, 2));
            builder.add(sp, cc.xyw(2, 4, 5));
            builder.add(r1, cc.xyw(2, 6, 5));
            builder.add(r2, cc.xyw(2, 8, 5));
            builder.add(r3, cc.xyw(2, 10, 5));

            panel.add(builder.getPanel(), BorderLayout.NORTH);

            FormLayout layoutButton = new FormLayout(
                    "15px, right:90px, 20px, left:pref, 5px", // columns
                    "20px, 40px, 2px");      // rows

//            FormDebugPanel debugPanel = new FormDebugPanel(layout);
//            PanelBuilder builder = new PanelBuilder(layout, debugPanel);
            PanelBuilder builderButton = new PanelBuilder(layoutButton);

            // Obtain a reusable constraints object to place components in the grid.
            CellConstraints ccButton = new CellConstraints();

            builderButton.add(buttonStart, cc.xy(2, 2));
            builderButton.add(buttonStop, cc.xy(4, 2));

            panel.add(builderButton.getPanel(), BorderLayout.CENTER);

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            items.getReadWriteLock().writeLock().unlock();
        }
    }

    public void clearItems() {
        items.clear();
    }

    public void dispose() {
        System.out.println("DockReestrs clear...");

        if (listModel != null) listModel = null;
        if (sortedEntries != null) sortedEntries.dispose();
        if (filteredEntries != null) filteredEntries.dispose();
        if (entries != null) entries.dispose();
        if (items != null) items.dispose();

        System.out.println("DockReestrs clear...OK");
    }

    public void appendReestr(Reestr reestr) {
        items.add(reestr);
    }

    public void stopOpenReestr() {
        LoadMainData loader = dockingContainer.getLoader();
        if (loader.isExecuted()) {
            loader.cancel();
        }
    }

    public void openReestr() {
        if (selectionModel.getSelected().size() == 0) {
            JOptionPane.showMessageDialog(null, "Не выбран реестр!");
            return;
        }

        Reestr reestr = (Reestr) selectionModel.getSelected().get(0);

        LoadMainData loader = dockingContainer.getLoader();

        if (loader.isExecuted()) {
            JOptionPane.showMessageDialog(
                    null,
                    "В данные момент выполняется загрузка!\n" +
                            "Действие отменено.",
                    "Внимание",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            loader.setReestr(reestr.getTitle());
            loader.execute();
        }
    }

    public int getSumType() {
        if(r1.isSelected())
            return 1;
        if(r2.isSelected())
            return 2;
        if(r3.isSelected())
            return 3;

        return -1;
    }

    public void setStartEnable(boolean enable){
        buttonStop.setEnabled(!enable);
        buttonStart.setEnabled(enable);
    }

    public  void setButtonLoadReestrsEnable(boolean enable){
        loadReestrs.setEnabled(enable);
    }
}
