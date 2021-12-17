package org.sjwimmer.ta4jchart.chart.elements;

import org.sjwimmer.ta4jchart.chart.TacChartMouseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TacStickyCrossHairButton extends JToggleButton implements ActionListener {

    private final TacChartMouseHandler mouseHandler;

    public TacStickyCrossHairButton(TacChartMouseHandler mouseHandler) {
        super("Sticky Cross hair");
        this.mouseHandler = mouseHandler;
        setSelected(true);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouseHandler.setSticky(isSelected());
    }
}
