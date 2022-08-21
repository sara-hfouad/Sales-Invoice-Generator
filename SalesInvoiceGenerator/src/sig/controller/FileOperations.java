package sig.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import sig.model.InvoiceHeader;
import sig.model.InvoiceLine;
import sig.view.InvoiceFrame;

public class FileOperations {

    InvoiceFrame frame;

    public FileOperations(InvoiceFrame frame) {
        this.frame = frame;
    }

    public void loadFile(InvoiceFrame frame) {
        try {
            
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                String headerStrPath = headerFile.getAbsolutePath();

                String extension = "";
                int i = headerStrPath.lastIndexOf('.');
                if (i > 0) {
                    extension = headerStrPath.substring(i + 1);
                }
                if (!extension.equals("csv")) {
                    extError();
                    return;
                }
                frame.getErrorLbl().setText("");

                Path headerPath = Paths.get(headerStrPath);
                List<String> headerLines = Files.lines(headerPath).collect(Collectors.toList());
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<InvoiceHeader>();
                for (String line : headerLines) {
                    String[] arr = line.split(",");
                    if (arr.length != 3) {
                        emptyInv();
                        return;
                    }
                    if (arr[0].isEmpty() || arr[1].isEmpty() || arr[2].isEmpty()) {
                        emptyInv();
                        return;
                    } else {
                        frame.getErrorLbl().setText("");
                    }
                    int id = 0;
                    try {
                        id = Integer.parseInt(arr[0]);
                    } catch (Exception e) {
                        wrongId();
                        return;
                    }

                    if (checkDateFormat(arr[1]) && checkDateCorrect(arr[1])) {
                        InvoiceHeader inv = new InvoiceHeader(id, arr[1], arr[2]);
                        invoiceHeaders.add(inv);

                    } else {
                        wrongDateFormat();
                        return;
                    }

                }

                res = fc.showOpenDialog(frame);
                if (res == JFileChooser.APPROVE_OPTION) {
                    String lineStrPath = fc.getSelectedFile().getAbsolutePath();
                    extension = "";
                    i = headerStrPath.lastIndexOf('.');
                    if (i > 0) {
                        extension = headerStrPath.substring(i + 1);
                    }

                    if (!extension.equals("csv")) {
                        extError();
                        return;
                    }
                    frame.getErrorLbl().setText("");
                    Path linePath = Paths.get(lineStrPath);
                    List<String> lines = Files.lines(linePath).collect(Collectors.toList());

                    for (String line : lines) {
                        String[] arr = line.split(",");
                        if (arr.length != 4) {
                            emptyItems();
                            return;
                        }
                        if (arr[1].isEmpty() || arr[2].isEmpty() || arr[3].isEmpty()) {
                            emptyItems();
                            return;
                        } else {
                            frame.getErrorLbl().setText("");
                        }

                        double price = 0;
                        int count = 0;
                        try {
                            price = Double.parseDouble(arr[2]);
                        } catch (Exception e) {
                            wrongPrice();
                            return;
                        }
                        try {
                            count = Integer.parseInt(arr[3]);
                        } catch (Exception e) {
                            wrongCount();
                            return;
                        }

                        if (price < 0) {
                            wrongPrice();
                            return;
                        } else {
                            frame.getErrorLbl().setText("");
                        }
                        if (count <= 0) {
                            wrongCount();
                            return;
                        } else {
                            frame.getErrorLbl().setText("");
                        }

                        int invId = Integer.parseInt(arr[0]);
                        price = Double.parseDouble(arr[2]);
                        count = Integer.parseInt(arr[3]);
                        InvoiceHeader inv = getInvById(invoiceHeaders, invId);
                        InvoiceLine l = new InvoiceLine(inv, arr[1], price, count);
                        inv.getItems().add(l);
                    }
                    frame.setInvoiceHeadersList(invoiceHeaders);
                    Color g=new Color(61,214,61);
                    frame.getErrorLbl().setForeground(g);
                    frame.getErrorLbl().setText("Files Loaded Successfully");
                }

            }
        } catch (IOException e) {
            fileNotFound();
            return;
        }
    }

    public void saveFile(InvoiceFrame frame) {
        try {
            JFileChooser fc = new JFileChooser();
            int res = fc.showSaveDialog(null);
            ArrayList<InvoiceHeader> invoiceHeaders = frame.getInvoiceHeadersList();
            String invoices = "";
            String items = "";

            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                String headerStrPath = file.getPath();
                FileWriter fw = new FileWriter(headerStrPath);

                String extension = "";
                int i = headerStrPath.lastIndexOf('.');
                if (i > 0) {
                    extension = headerStrPath.substring(i + 1);
                }
                if (!extension.equals("csv")) {
                    extError();
                    return;
                }
                for (InvoiceHeader inv : invoiceHeaders) {
                    invoices += inv.getNum() + "," + inv.getDate() + "," + inv.getCustomer() + "\n";
                }
                fw.write(invoices);
                fw.flush();
                fw.close();

                res = fc.showSaveDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    headerStrPath = file.getPath();
                    fw = new FileWriter(headerStrPath);

                    extension = "";
                    i = headerStrPath.lastIndexOf('.');
                    if (i > 0) {
                        extension = headerStrPath.substring(i + 1);
                    }
                    if (!extension.equals("csv")) {
                        extError();
                        return;
                    }
                    for (InvoiceHeader inv : invoiceHeaders) {
                        for (InvoiceLine item : inv.getItems()) {
                            items += item.getInvoice().getNum() + "," + item.getName() + "," + item.getPrice() + "," + item.getCount() + "\n";
                        }
                    }
                    fw.write(items);
                    fw.flush();
                    fw.close();

                    Color g=new Color(61,214,61);
                    frame.getErrorLbl().setForeground(g);
                    frame.getErrorLbl().setText("Files Saved Successfully");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    private InvoiceHeader getInvById(ArrayList<InvoiceHeader> invoices, int id) {
        for (InvoiceHeader inv : invoices) {
            if (inv.getNum() == id) {
                return inv;
            }
        }
        return null;
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
        if (arr.length != 3) {
            return false;
        }
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

    private void extError() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "Wrong Extension!";
        s += "\n";
        s += "Please make sure the files you select are CSV files";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void wrongDateFormat() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "Wrong Date Format!";
        s += "\n";
        s += "Please make sure all dates are in the form of 'dd-mm-yyyy' and are today or earlier ";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void fileNotFound() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "Error!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void wrongPrice() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "Please make sure all items prices are valid!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void wrongCount() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "Please make sure all items counts are valid!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void emptyItems() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "There are empty values in the file! Please fill in the item name, item price, and count of all invoices items!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void emptyInv() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "There are empty values in the file! Please fill in the invoice id, invoice date, and customer name of all invoices!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

    private void wrongId() {
        frame.getErrorLbl().setForeground(Color.RED);
        String s = "Please make sure all invoives ids are valid!";
        frame.getErrorLbl().setText("<HTML>" + s + "</HTML>");
    }

}
