package sig.model;

import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {

    private int num;
    private String date;
    private String customer;
    private ArrayList<InvoiceLine> Items;

    public InvoiceHeader(int num, String date, String customer) {
        this.num = num;
        this.date = date;
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<InvoiceLine> getItems() {
        if (Items == null) {
            Items = new ArrayList<InvoiceLine>();
        }
        return Items;
    }

    public void setItems(ArrayList<InvoiceLine> Items) {
        this.Items = Items;
    }

    public double getTotal() {
        double total = 0;
        for (InvoiceLine l : getItems()) {
            total += l.getTotal();
        }
        return total;
    }
    
    public void updateTotal() {
        double total = 0;
        for (InvoiceLine l : getItems()) {
            total += l.getTotal();
        }
    }

    @Override
    public String toString() {
        return "InvoiceHeader{" + "num=" + num + ", date=" + date + ", customer=" + customer + '}';
    }

}
