import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends JFrame{
    private ArrayList<String> products;
    private ArrayList<String> sales;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    String[] items = {"Sales", "Inventory", "Report"};


    public Main() {                                 //  Initialises the window
        setTitle("Store management");
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //  Exits program when window is closed

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

    // INVENTORY PAGE COMPLETED BY S.W. ---------------------------------------------------------------------------
    public JPanel makeInventoryPage() {
        HashMap<AtomicInteger, JButton> RowMap = new HashMap<>();

        JPanel InvPanel = new JPanel(new BorderLayout());
        JPanel ControlPanel = new JPanel(new GridLayout(1,3, 5, 5));
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
            JPanel RowPanel = new JPanel(new GridLayout(1,3, 10, 10));
            JButton DeleteRowButton = new JButton("Del");
            JTextField ItemTextField = new JTextField(10);
            JTextField StockTextField = new JTextField(10);

            RowPanel.add(DeleteRowButton);
            RowPanel.add(ItemTextField);
            RowPanel.add(StockTextField);
            InvPageContents.add(RowPanel);

            AtomicInteger rowID = new AtomicInteger(RowCounter.getAndIncrement());
            RowMap.put(rowID, DeleteRowButton);

            InvPanel.repaint();
            InvPanel.revalidate();


            RowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, RowPanel.getPreferredSize().height)); // Ensures uniform row width
            InvPageContents.add(RowPanel);
            InvPageContents.revalidate();
            InvPageContents.repaint();


            DeleteRowButton.addActionListener(_ -> {
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
        JPanel LabelRowPanel = new JPanel(new GridLayout(1,3));
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
                    JPanel RowPanel = new JPanel(new GridLayout(1,3, 10, 10));
                    JButton DeleteRowButton = new JButton("Del");
                    JTextField ItemTextField = new JTextField(data[0], 10);
                    JTextField StockTextField = new JTextField(data[1], 10);

                    RowPanel.add(DeleteRowButton);
                    RowPanel.add(ItemTextField);
                    RowPanel.add(StockTextField);

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

    // panel for report page, need to figure out how to add stuff to the page
    private JPanel makeReportPage() {
        JPanel panel = new JPanel();
        JButton printReport = new JButton("Print Report");
        JTextArea textArea = new JTextArea(20,30);
        panel.add(new JLabel("report panel"));
        panel.add(printReport);
        panel.add(textArea);
        return panel;
    }

    public static void main(String[] args) {new Main();}
}

// store management class, all the functions go here and will be called upon later in the code when we figure how to add stuff to the panels
class StoreManagement {
    // add functionality
    public void addSaleButton() {

    }

    public void removeSaleButton(){

    }

    public void addInventoryButton() {

    }

    public void removeInventoryButton() {

    }

    public void printReportButton() {

    }

    // these are temporary names
}
