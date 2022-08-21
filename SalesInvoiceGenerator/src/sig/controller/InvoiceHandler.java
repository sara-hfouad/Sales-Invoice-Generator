package sig.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import sig.model.InvoiceHeader;
import sig.view.NewInvoiceFrame;

public class InvoiceHandler implements ActionListener {

    NewInvoiceFrame createFrame;

    public InvoiceHandler(NewInvoiceFrame frame) {
        this.createFrame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Ok":
                createInvoice();
                break;
            case "Cancel":
                cancel();
                break;
        }
    }

    private void createInvoice() {
        createFrame.getMainFrame().getErrorLbl().setForeground(Color.RED);
        String date = createFrame.getDateTextF().getText();
        String name = createFrame.getNameTextF().getText();
        
        if(date.isEmpty() || name.isEmpty()){
            empty();
            return;
        }
        else{
            createFrame.getErrorLbl().setText("");
        }
        
        int s = createFrame.getMainFrame().getInvoiceHeadersList().size();
        int id;
        if (s > 0) {
            id = createFrame.getMainFrame().getInvoiceHeadersList().get(s - 1).getNum() + 1;
        } else {
            id = 1;
        }

        if (checkDateFormat(date) &&  checkDateCorrect(date) ) {
//            createFrame.getErrorLbl().setText("");
            InvoiceHeader newInv = new InvoiceHeader(id, date, name);
            ArrayList<InvoiceHeader> newList = createFrame.getMainFrame().getInvoiceHeadersList();
            newList.add(newInv);
            createFrame.getMainFrame().setInvoiceHeadersList(newList);
            Color g=new Color(61,214,61);
            createFrame.getMainFrame().getErrorLbl().setForeground(g);
            createFrame.getMainFrame().getErrorLbl().setText("Invoice created successfully!");
            createFrame.dispose();
            
        } else {
            wrongDateFormat();
        }

    }

    private void cancel() {
        createFrame.dispose();
    }

    public boolean checkDateFormat(String date) {
        String[] arr = date.split("-");
        if (arr.length != 3) {
            return false;
        }

        try {
            int d = Integer.parseInt(arr[0]);
            int m = Integer.parseInt(arr[1]);
            int y = Integer.parseInt(arr[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;

    }

    public boolean checkDateCorrect(String date) {
        String[] arr = date.split("-");

        int d = Integer.parseInt(arr[0]);
        int m = Integer.parseInt(arr[1]);
        int y = Integer.parseInt(arr[2]);

        LocalDate currentdate = LocalDate.now();
        int day = currentdate.getDayOfMonth();
        int month = currentdate.getMonthValue();
        int year = currentdate.getYear();

        if (y > year) {
            return false;
        }
        if (m > month && y == year) {
            return false;
        }
        if (d > day && m == month && y == year) {
            return false;
        }

        return true;

    }

    private void wrongDateFormat() {
        String s = "Wrong Date Format!";
        s += "\n";
        s += "Please make sure all dates are in the form of 'dd-mm-yyyy' and are today or earlier ";
        createFrame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }
    
    private void empty(){
        String s = "Please fill in the date and customer name to create an invoice!";
        createFrame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

}
