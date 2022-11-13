package de.sjwimmer.ta4jchart.chartbuilder.data;

import javax.swing.*;

public class DataPanel extends JPanel {

    public DataPanel(TacDataTableModel dataTableModel) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        TacDataTable tacDataTable = new TacDataTable(dataTableModel);
        add(new JScrollPane(tacDataTable));
    }

}
