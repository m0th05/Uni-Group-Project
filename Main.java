import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


//Have inventory check for file and create one if not present
//Add validation to inventory page
//try get graph to work
//  

public class Main extends JFrame {
    private final ArrayList<String> sales;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    String[] items = {"Sales", "Inventory", "Report"};

    public Main() {                                 //  Initialises the window
        setTitle("Store management");
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //  Exits program when window is closed

        sales = new ArrayList<>();

        // initialised panels
        JPanel salesPanel = makeSalesPage();
        JPanel inventory = makeInventoryPage();
        JPanel report = makeReportPage();

        // added in panels to the window
        cardPanel.add(salesPanel, "Sales");
        cardPanel.add(inventory, "Inventory");
        cardPanel.add(report, "Report");

        JComboBox<String> dropdown = new JComboBox<>(items);
        dropdown.addActionListener(_ -> {
            String selectedItem = (String) dropdown.getSelectedItem();
            cardLayout.show(cardPanel, selectedItem);
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
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        // this is all test changes, so it outlines how it works for the rest of the panels
        panel.add(new JLabel("sales panel"));
        panel.add(addSale);
        panel.add(removeSale);
        panel.add(new JScrollPane(textArea));


// START OF ANDREWS CODE ---------------------------------------------------------------------
        addSale.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField amountField = new JTextField();
            JTextField priceField = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.add(new JLabel("Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Amount of items:"));
            inputPanel.add(amountField);
            inputPanel.add(new JLabel("Total:"));
            inputPanel.add(priceField);

            int result = JOptionPane.showConfirmDialog(null, inputPanel,
                    "Enter Sale Details", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String amount = amountField.getText().trim();
                String price = priceField.getText().trim();

                if (!name.isEmpty() && !amount.isEmpty() && !price.isEmpty()) {
                    String entry = "Name: " + name + " | Amount of items: " + amount + " | Total: " + price;
                    sales.add(entry);
                    updateSalesTextArea(textArea);
                } else {
                    JOptionPane.showMessageDialog(null, "All fields must be filled.");
                }
            }
        });

        removeSale.addActionListener(e -> {
            String nameToRemove = JOptionPane.showInputDialog("Enter name of sale to remove:");
            if (nameToRemove != null && !nameToRemove.trim().isEmpty()) {
                boolean removed = sales.removeIf(s -> s.toLowerCase().contains(nameToRemove.toLowerCase()));
                if (removed) {
                    updateSalesTextArea(textArea);
                    JOptionPane.showMessageDialog(null, "Sale removed.");
                } else {
                    JOptionPane.showMessageDialog(null, "No matching sale found.");
                }
            }
        });

        return panel;
    }

    private void updateSalesTextArea(JTextArea textArea) {
        textArea.setText(String.join("\n", sales));
    }

// END OF SECTION BY ANDREW ---------------------------------------------------------------------------------------


    // INVENTORY PAGE COMPLETED BY S.W. ---------------------------------------------------------------------------
    public JPanel makeInventoryPage() {
        HashMap<AtomicInteger, JButton> RowMap = new HashMap<>();

        JPanel InvPanel = new JPanel(new BorderLayout());
        JPanel ControlPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JLabel InvHeader = new JLabel("Inventory Panel");               //
        JButton AddRowButton = new JButton("Create Row");               //  Adds the control row
        JButton SaveInventoryButton = new JButton("Save");              //  to the Inv page
        ControlPanel.add(InvHeader);                                        //
        ControlPanel.add(AddRowButton);                                     //
        ControlPanel.add(SaveInventoryButton);                              //
        InvPanel.add(ControlPanel, BorderLayout.NORTH);

        JPanel InvPageContents = new JPanel();
        InvPageContents.setLayout(new BoxLayout(InvPageContents, BoxLayout.Y_AXIS));
        loadInventoryFromFile(InvPageContents);
        JScrollPane scrollPane = new JScrollPane(InvPageContents);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        InvPanel.add(scrollPane, BorderLayout.CENTER);
        AtomicInteger RowCounter = new AtomicInteger(1);

        // ADD ROW BUTTON FUNCTIONALITY
        AddRowButton.addActionListener(_ -> {                 //  ADDS A ROW TO THE PAGE
            JPanel RowPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            JButton DeleteRowButton = new JButton("Del");
            JTextField ItemTextField = new JTextField(10);
            JTextField StockTextField = new JTextField(10);

            RowPanel.add(DeleteRowButton);
            RowPanel.add(ItemTextField);
            RowPanel.add(StockTextField);
            RowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, RowPanel.getPreferredSize().height)); // Ensures uniform row width

            InvPageContents.add(RowPanel);
            InvPageContents.revalidate();
            InvPageContents.repaint();

            AtomicInteger rowID = new AtomicInteger(RowCounter.getAndIncrement());
            RowMap.put(rowID, DeleteRowButton);

            DeleteRowButton.addActionListener(_2 -> {
                InvPageContents.remove(RowPanel);
                InvPageContents.revalidate();
                InvPageContents.repaint();
            });
        });

        // SAVE BUTTON FUNCTIONALITY
        SaveInventoryButton.addActionListener(_ -> {
            try (FileWriter writer = new FileWriter("inventory.txt")) {
                for (Component comp : InvPageContents.getComponents()) {
                    if (comp instanceof JPanel) {
                        JPanel row = (JPanel) comp;
                        Component[] rowComponents = row.getComponents();

                        if (rowComponents.length >= 3
                                && rowComponents[1] instanceof JTextField
                                && rowComponents[2] instanceof JTextField) {

                            JTextField itemField = (JTextField) rowComponents[1];
                            JTextField stockField = (JTextField) rowComponents[2];

                            writer.write(itemField.getText() + "," + stockField.getText() + "\n");
                        }
                    }
                }
                System.out.println("Inventory saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return InvPanel;
    }

    private void loadInventoryFromFile(JPanel InvPageContents) {
        // Clear existing rows but keep the header row
        InvPageContents.removeAll();

        // Add the header row FIRST before loading saved data
        JPanel LabelRowPanel = new JPanel(new GridLayout(1, 3));
        JLabel DelHeader = new JLabel("Delete Row");
        JLabel ItemHeader = new JLabel("Item");
        JLabel StockHeader = new JLabel("Stock");

        LabelRowPanel.add(DelHeader);
        LabelRowPanel.add(ItemHeader);
        LabelRowPanel.add(StockHeader);
        InvPageContents.add(LabelRowPanel);

        // Load saved rows
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    JPanel RowPanel = new JPanel(new GridLayout(1, 3, 10, 10));
                    JButton DeleteRowButton = new JButton("Del");
                    JTextField ItemTextField = new JTextField(data[0], 10);
                    JTextField StockTextField = new JTextField(data[1], 10);

                    RowPanel.add(DeleteRowButton);
                    RowPanel.add(ItemTextField);
                    RowPanel.add(StockTextField);

                    RowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, RowPanel.getPreferredSize().height));
                    InvPageContents.add(RowPanel);

                    // Delete button functionality
                    DeleteRowButton.addActionListener(_ -> {
                        InvPageContents.remove(RowPanel);
                        InvPageContents.revalidate();
                        InvPageContents.repaint();
                    });
                }
            }
            System.out.println("Inventory loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh UI
        InvPageContents.revalidate();
        InvPageContents.repaint();
    }

    //  END OF SECTION COMPLETED BY S.W. --------------------------------------------------------------------------

    // TB---------------------------------------------------------------------------------------------------------------

    // panel for report page, need to figure out how to add stuff to the page
    private JPanel makeReportPage() {
        JPanel panel = new JPanel();
        JButton printReport = new JButton("Print Report");
        JTextArea textArea = new JTextArea(20, 30);
        panel.add(new JLabel("report panel"));
        panel.add(printReport);
        panel.add(textArea);

        printReport.addActionListener(e -> {
            StringBuilder report = new StringBuilder();
            report.append("=== Sales Report ===\n\n");

            // Reads Sales
            if (sales.isEmpty()) {
                report.append("No sales recorded.\n");
            } else {
                for (String sale : sales) {
                    report.append(sale).append("\n");
                }
            }

            report.append("\n=== Inventory Report ===\n\n");

            // Load inventory from file
            try (BufferedReader reader = new BufferedReader(new FileReader("inventory.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    report.append(line).append("\n");
                }
            } catch (IOException ex) {
                report.append("Error loading inventory.\n");
            }

            textArea.setText(report.toString());
        });
        return panel;
    }

    public static void main(String[] args) {
        new Main();
    }
}
//TB--------------------------------------------------------------------------------------------------------------------

// store management class, all the functions go here and will be called upon later in the code when we figure how to add stuff to the panels
class StoreManagement {
    // add functionality
    public void addSaleButton() {
    }

    public void removeSaleButton() {
    }

    public void addInventoryButton() {
    }

    public void removeInventoryButton() {
    }

    public void printReportButton() {
    }

    // these are temporary names
}