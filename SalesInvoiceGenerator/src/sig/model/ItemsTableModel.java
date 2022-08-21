package sig.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ItemsTableModel extends AbstractTableModel {

    private ArrayList<InvoiceLine> data;

    public ItemsTableModel(ArrayList<InvoiceLine> data) {
        this.data = data;
    }
    private String[] cols = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine item = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex+1;
            case 1:
                return item.getName();
            case 2:
                return item.getPrice();
            case 3:
                return item.getCount();
            case 4:
                return item.getTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return cols[column]; //To change body of generated methods, choose Tools | Templates.
    }

}
