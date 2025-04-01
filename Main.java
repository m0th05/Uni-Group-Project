import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame{
    private ArrayList<String> products;
    private ArrayList<String> sales;
    private int initInventory;
    private int endInventory;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    String[] items = {"Sales", "Inventory", "Report"};


    public Main() {
        setTitle("Store management");
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // initialised panels
        JPanel sales = makeSalesPage();
        JPanel inventory = makeInventoryPage();
        JPanel report = makeReportPage();

        // added in panels to the window
        cardPanel.add(sales, "Sales");
        cardPanel.add(inventory, "Inventory");
        cardPanel.add(report,"Report");

        JComboBox<String> dropdown = new JComboBox<>(items);
        dropdown.addActionListener(_ -> {
            String selectedItem = (String) dropdown.getSelectedItem();
            cardLayout.show(cardPanel,selectedItem);
        });

        add(dropdown, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        setSize(500, 500);
        setVisible(true);
    }

    // panel for sales page, need to figure out how to add stuff to the page
    private JPanel makeSalesPage() {
        JPanel panel = new JPanel();
        JButton addSale = new JButton("add sale");
        JButton removeSale = new JButton("remove sale");
        JTextArea textArea = new JTextArea(10,30);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30,30));
        // this is all test changes, so it outlines how it works for the rest of the panels
        panel.add(new JLabel("sales panel"));
        panel.add(addSale);
        panel.add(removeSale);
        panel.add(textArea);
        return panel;
    }

    // panel for inventory page, need to figure out how to add stuff to the page
    private JPanel makeInventoryPage() {
        JPanel panel = new JPanel();
        JButton addInvent = new JButton("add inventory");
        JTextArea textArea = new JTextArea(10,30);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        panel.add(new JLabel("inventory panel"));
        panel.add(addInvent);
        panel.add(new JButton("Remove inventory"));
        panel.add(textArea);
        return panel;
    }

    // panel for report page, need to figure out how to add stuff to the page
    private JPanel makeReportPage() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("report panel"));
        return panel;
    }

    public static void main(String[] args) {new Main();}
}

// store management class, all the functions go here and will be called upon later in the code when we figure how to add stuff to the panels
class StoreManagement {
    // add functionality
}