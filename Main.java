import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main extends JFrame{
    private ArrayList<String> products;
    private ArrayList<String> sales;
    private int initInventory;
    private int endInventory;
    String[] items = {"Test 1", "Test 2"};


    public Main() {
        setTitle("Store management");
        setLayout(new FlowLayout(5, 30, 10));
        JComboBox<String> dropdown = new JComboBox<>(items);
        add(dropdown);
        setSize(400, 350);
        setVisible(true);
    }










    public static void main(String[] args) {new Main();}
}