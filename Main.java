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
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    String[] items = {"Sales", "Inventory", "Report"};


    public Main() {
        setTitle("Store management");
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel sales = makeSalesPage();
        JPanel inventory = makeInventoryPage();
        JPanel report = makeReportPage();

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

    private JPanel makeSalesPage() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("sales panel"));
        return panel;
    }

    private JPanel makeInventoryPage() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("inventory panel"));
        return panel;
    }

    private JPanel makeReportPage() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("report panel"));
        return panel;
    }

    public static void main(String[] args) {new Main();}
}