package sig.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import sig.model.InvoiceHeader;
import sig.model.InvoiceLine;
import sig.model.InvoicesTableModel;
import sig.model.ItemsTableModel;
import sig.view.NewItemFrame;

public class ItemHandler implements ActionListener {

    NewItemFrame createFrame;

    public ItemHandler(NewItemFrame createFrame) {
        this.createFrame = createFrame;
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Ok":
                createItem();
                break;
            case "Cancel":
                cancel();
                break;
        }
    }

    private void createItem() {
        createFrame.getMainFrame().getErrorLbl().setForeground(Color.RED);
        int selectedRow = createFrame.getMainFrame().getInvoicesTable().getSelectedRow();
        if (selectedRow != -1) {
            String name = createFrame.getNameTextF().getText();
            String prcStr = createFrame.getPriceTextF().getText();
            String cntStr = createFrame.getCountTextF().getText();

            if (name.isEmpty() || prcStr.isEmpty() || cntStr.isEmpty()) {
                empty();
                return;
            } else {
                createFrame.getErrorLbl().setText("");
            }
            double price = 0;
            int count = 0;
            try {
                price = Double.parseDouble(prcStr);
            } catch (Exception e) {
                wrongPrice();
                return;
            }
            try {
                count = Integer.parseInt(cntStr);
            } catch (Exception e) {
                wrongCount();
                return;
            }
            
            if (price < 0) {
                wrongPrice();
                return;
            } else {
                createFrame.getErrorLbl().setText("");
            }
            if (count <= 0) {
                wrongCount();
                return;
            } else {
                createFrame.getErrorLbl().setText("");
            }

            InvoiceHeader invoice = createFrame.getMainFrame().getInvoiceHeadersList().get(selectedRow);

            InvoiceLine newItem = new InvoiceLine(invoice, name, price, count);
            ArrayList<InvoiceLine> newList = invoice.getItems();
            newList.add(newItem);
            invoice.setItems(newList);
            createFrame.getMainFrame().getTotalLabel().setText(invoice.getTotal() + "");
            createFrame.getMainFrame().getItemsTable().setModel(new ItemsTableModel(newList));

            InvoicesTableModel invoicesTableModel = new InvoicesTableModel(createFrame.getMainFrame().getInvoiceHeadersList());
            createFrame.getMainFrame().getInvoicesTable().setModel(invoicesTableModel);
            Color g=new Color(61,214,61);
            createFrame.getMainFrame().getErrorLbl().setForeground(g);
            createFrame.getMainFrame().getErrorLbl().setText("Invoice item created successfully!");
            createFrame.dispose();
        } else {
            selectInv();
            return;
        }

    }

    private void cancel() {
        createFrame.dispose();
    }

    private void empty() {
        String s = "Please fill in the item name, item price, and count to create an invoice item!";
        createFrame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void selectInv() {
        String s = "Please select an invoice first to create an item for!";
        createFrame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void wrongPrice() {
        String s = "Please enter a valid item price!";
        createFrame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void wrongCount() {
        String s = "Please enter a valid item count!";
        createFrame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

}
