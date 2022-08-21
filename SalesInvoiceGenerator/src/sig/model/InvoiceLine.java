package sig.model;

public class InvoiceLine {

    private InvoiceHeader invoice;
    private String name;
    private double price;
    private int count;

    public InvoiceLine(InvoiceHeader invoice, String name, double price, int count) {
        this.invoice = invoice;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public InvoiceHeader getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceHeader invoice) {
        this.invoice = invoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return count * price;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" + "name=" + name + ", price=" + price + ", count=" + count + '}';
    }

}
