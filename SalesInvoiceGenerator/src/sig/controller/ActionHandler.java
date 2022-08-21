package sig.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sig.model.InvoiceHeader;
import sig.model.InvoiceLine;
import sig.model.InvoicesTableModel;
import sig.model.ItemsTableModel;
import sig.view.NewInvoiceFrame;
import sig.view.NewItemFrame;
import sig.view.InvoiceFrame;

public class ActionHandler implements ActionListener, ListSelectionListener {

    InvoiceFrame frame;
    FileOperations op;

    public ActionHandler(InvoiceFrame frame) {
        this.frame = frame;
        op = new FileOperations(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Create New Invoice":
                frame.getErrorLbl().setText("");
                createInvoice();
                break;
            case "Delete Invoice":
                frame.getErrorLbl().setText("");
                deleteInvoice();
                break;
            case "Create New Item":
                frame.getErrorLbl().setText("");
                createItem();
                break;
            case "Delete Item":
                frame.getErrorLbl().setText("");
                deleteItem();
                break;
            case "Load File":
                op.loadFile(frame);
                break;
            case "Save File":
                op.saveFile(frame);
                break;

        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = frame.getInvoicesTable().getSelectedRow();
        InvoiceHeader invoice = null;
        if (selectedRow != -1) {
            invoice = frame.getInvoiceHeadersList().get(selectedRow);
        }
        if (invoice != null) {
            ArrayList<InvoiceLine> invItems = invoice.getItems();
            frame.getItemsTable().setModel(new ItemsTableModel(invItems));
            frame.getNumLbl().setText(invoice.getNum() + "");
            frame.getDateLbl().setText(invoice.getDate());
            frame.getNameLbl().setText(invoice.getCustomer());
            frame.getTotalLabel().setText(invoice.getTotal() + "");
        }

    }

    private void deleteInvoice() {
        frame.getErrorLbl().setForeground(Color.RED);
        int selectedRow = frame.getInvoicesTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getErrorLbl().setText("");
            frame.getInvoiceHeadersList().remove(selectedRow);
            ArrayList<InvoiceHeader> newInvoices = new ArrayList<>();
            for (InvoiceHeader inv : frame.getInvoiceHeadersList()) {
                newInvoices.add(inv);
            }
            frame.setInvoiceHeadersList(newInvoices);

            ArrayList<InvoiceLine> itemsList = new ArrayList<>();
            ItemsTableModel itemsTableModel = new ItemsTableModel(itemsList);
            frame.getItemsTable().setModel(itemsTableModel);
            
            frame.getNumLbl().setText("");
            frame.getDateLbl().setText("");
            frame.getNameLbl().setText("");
            frame.getTotalLabel().setText("");
        } else {
            selectInv();
            return;
        }

    }

    private void createInvoice() {
        NewInvoiceFrame invFrame = new NewInvoiceFrame();
        invFrame.setVisible(true);
        frame.setCrtInvFrame(invFrame);
        invFrame.setMainFrame(frame);
    }

    private void createItem() {
        NewItemFrame itemFrame = new NewItemFrame();
        itemFrame.setVisible(true);
        frame.setCrtItmFrame(itemFrame);
        itemFrame.setMainFrame(frame);
    }

    private void deleteItem() {
        frame.getErrorLbl().setForeground(Color.RED);
        int selectedInv = frame.getInvoicesTable().getSelectedRow();
        int selectedItem = frame.getItemsTable().getSelectedRow();

        if (selectedInv != -1) {
            if (selectedItem != -1) {
                frame.getErrorLbl().setText("");
                InvoiceHeader inv = frame.getInvoiceHeadersList().get(selectedInv);
                inv.getItems().remove(selectedItem);

                ArrayList<InvoiceLine> newItems = new ArrayList<>();
                for (InvoiceLine l : inv.getItems()) {
                    newItems.add(l);
                }
                frame.getItemsTable().setModel(new ItemsTableModel(newItems));

                InvoicesTableModel invoicesTableModel = new InvoicesTableModel(frame.getInvoiceHeadersList());
                frame.getInvoicesTable().setModel(invoicesTableModel);
            }
            else{
                selectItem();
                return;
            }
        }
        else{
            selectItemInv();
            return;
        }
    }

    private void selectInv() {
        String s = "Please select an invoice to delete!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void selectItem() {
        String s = "Please select an invoice item to delete!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }
    
     private void selectItemInv() {
        String s = "Please select the invoice of the invoice item you want to delete!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

}
