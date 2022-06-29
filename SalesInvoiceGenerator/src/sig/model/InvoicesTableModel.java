/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sig.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Hassan
 */
public class InvoicesTableModel extends AbstractTableModel {

    private ArrayList<InvoiceHeader> data;
    private String[] cols = {"No.", "Date", "Customer", "Total"};

    public InvoicesTableModel(ArrayList<InvoiceHeader> data) {
        this.data = data;
    }

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
        InvoiceHeader inv = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return inv.getNum();
            case 1:
                return inv.getDate();

            case 2:
                return inv.getCustomer();

            case 3:
                return inv.getTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return cols[column]; //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
