package org.sjwimmer.ta4jchart.chartbuilder.toolbar;

import org.sjwimmer.ta4jchart.chartbuilder.crosshair.TacChartMouseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TacStickyCrossHairButton extends JToggleButton implements ActionListener {

    private final TacChartMouseHandler mouseHandler;

    public TacStickyCrossHairButton(TacChartMouseHandler mouseHandler) {
        super("Sticky Cross Hair");
        this.mouseHandler = mouseHandler;
        setSelected(true);
        addActionListener(this);
        setToolTipText("If enabled the domain cross hair will stick to the nearest date entry");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouseHandler.setSticky(isSelected());
    }
}
