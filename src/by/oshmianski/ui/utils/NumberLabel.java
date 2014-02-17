package by.oshmianski.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by vintselovich on 17.02.14.
 */
public class NumberLabel extends JLabel {
    private DecimalFormat formatNumber;

    public NumberLabel(DecimalFormat formatNumber) {
        super("0");
        this.formatNumber = formatNumber;
        setFont(new Font("Tahoma", Font.PLAIN, 12));
        setForeground(Color.BLUE);
    }
    public NumberLabel() {
        this(new DecimalFormat("###,##0"));
    }

    public void setText(int text) {
        setText(formatNumber.format(text));
    }

    public void setText(BigDecimal text) {
        setText(formatNumber.format(text));
    }
}
