package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.ui.utils.ActionButton;
import by.oshmianski.utils.IconContainer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockMSWordExport extends DockSimple {
    private DockingContainer dockingContainer;
    private JTextField filterEdit;
    private ActionButton buttonExportWord;

    private final static String dockTitle = "Экстпор MS Word";

    public DockMSWordExport(final DockingContainer dockingContainer) {
        super("DockMSWordExport", IconContainer.getInstance().loadImage("layers.png"), dockTitle);

        this.dockingContainer = dockingContainer;

        filterEdit = new JTextField(15);

        buttonExportWord = new ActionButton("Экспорт в MS Word", IconContainer.getInstance().loadImage("page_white_word.png"), new Dimension(160, 30), "Выгрузка данных в MS Word");
        buttonExportWord.addActionListener(new ActionListenerExportMSWord());

        FormLayout layout = new FormLayout(
                "5px, right:50px, 5px, left:150px, 5px, 25px, 5px", // columns
                "2px, 23px, 2px, top:23px, 2px, 23px, 2px, 23px, 2px, 23px, 2px");      // rows

//            FormDebugPanel debugPanel = new FormDebugPanel();
//            PanelBuilder builder = new PanelBuilder(layout, debugPanel);
        PanelBuilder builder = new PanelBuilder(layout);

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Поиск:", cc.xy(2, 2));
        builder.add(filterEdit, cc.xy(4, 2));
//        builder.add(loadReestrs, cc.xy(6, 2));
//        builder.add(sp, cc.xyw(2, 4, 5));
//        builder.add(r1, cc.xyw(2, 6, 5));
//        builder.add(r2, cc.xyw(2, 8, 5));
//        builder.add(r3, cc.xyw(2, 10, 5));

        panel.add(builder.getPanel(), BorderLayout.NORTH);

        FormLayout layoutButton = new FormLayout(
                "15px, left:90px, 20px, left:pref, 5px", // columns
                "20px, 40px, 2px, 40px, 2px, 40px, 2px");      // rows

//            FormDebugPanel debugPanel = new FormDebugPanel();
//            PanelBuilder builderButton = new PanelBuilder(layoutButton, debugPanel);
        PanelBuilder builderButton = new PanelBuilder(layoutButton);

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints ccButton = new CellConstraints();

        builderButton.add(buttonExportWord, ccButton.xyw(2, 4, 3));

        panel.add(builderButton.getPanel(), BorderLayout.CENTER);

    }

    public void dispose() {
        System.out.println("DockMSWordExport clear...");


        System.out.println("DockMSWordExport clear...OK");
    }

    private class ActionListenerExportMSWord implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Not implemented yet!");
        }
    }
}
