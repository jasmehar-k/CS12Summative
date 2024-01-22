import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainPanel extends JPanel {
    static long serialVersionUID = 1L;
    JSpinner spinnerA = new JSpinner(new SpinnerNumberModel(10, 10, 90, 1));
    JSpinner spinnerB = new JSpinner(new SpinnerNumberModel(10, 10, 90, 1));
    JButton button = new JButton("Run Simulation");
    String[] column = {"Attempt #", "Light A Time", "Light B Time", "Car A Time", "Car B Time", "Average Car Time"};
    // columns for the table:
    ArrayList<Integer> attempts = new ArrayList<>();
    ArrayList<Integer> lightTimesA = new ArrayList<>();
    ArrayList<Integer> lightTimesB = new ArrayList<>();
    ArrayList<Long> carTimesA = new ArrayList<>();
    ArrayList<Long> carTimesB = new ArrayList<>();
    ArrayList<Double> averages = new ArrayList<>();
    Simulation simulation = null;
    JTable simCSVTable = null;
    public MainPanel() {
        importData(attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

        JPanel textPanel = new JPanel();
        JPanel inputPanel = new JPanel();

        textPanel.add(new JLabel("Welcome to the Traffic Simulator!"));
        textPanel.add(new JLabel("This simulation allows you to measure traffic flow and how traffic light timings affect efficiency."));
        textPanel.add(new JLabel("You can affect traffic flow by changing the duration of green lights at an intersection from a minimum of 10 seconds to a maximum of 90 seconds."));
        textPanel.add(new JLabel("Among the standardized number of cars, two cars will measure efficiency - Car A will travel from left to right, and Car B will travel from bottom to top."));
        textPanel.add(new JLabel("The simulation will measure the time it takes for both cars to reach their predetermined destinations on the opposite side of the intersection."));
        textPanel.add(new JLabel(" Clicking the different headings on the table will allow you to sort the table by minimum to maximum under the chosen heading. Have fun!"));

        inputPanel.add(new JLabel("Time for light A: "));
        inputPanel.add(spinnerA);
        inputPanel.add(new JLabel("Time for light B: "));
        inputPanel.add(spinnerB);
        inputPanel.add(button);
        // JPanel inputPanel = new JPanel();

        // inputPanel.add(new JLabel("Time for light A: "));
        // inputPanel.add(spinnerA);
        // inputPanel.add(new JLabel("Time for light B: "));
        // inputPanel.add(spinnerB);
        // inputPanel.add(button);

        int length = attempts.size();
        Object[][] tableData = new Object[length][6];

        for (int i = 0; i < length; i++) {
            tableData[i] = new Object[]{attempts.get(i), lightTimesA.get(i), lightTimesB.get(i), carTimesA.get(i), carTimesB.get(i), averages.get(i)};
        }
        
        // table:
        TableModel model = new DefaultTableModel(tableData, column) {
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };

        RowSorter<TableModel> sorter = new TableRowSorter<>(model);

        // simCSVTable = new JTable(model);
        // simCSVTable.setBounds(100, 55, 1000, 200);
        // simCSVTable.setRowSorter(sorter);

        // setLayout(new BorderLayout());
        // add(inputPanel, BorderLayout.PAGE_START);
        // add(new JScrollPane(simCSVTable), BorderLayout.CENTER);
        JTable simCSVTable = new JTable(model);
        simCSVTable.setBounds(1, 1, 1, 1);
        simCSVTable.setRowSorter(sorter);

        setLayout(new BorderLayout());

        textPanel.setPreferredSize(new Dimension(1000, 120));
        inputPanel.setPreferredSize(new Dimension(200, 400));

        add(textPanel, BorderLayout.PAGE_START);
        add(inputPanel, BorderLayout.LINE_START);
        add(new JScrollPane(simCSVTable), BorderLayout.CENTER);
        // start button:
        button.addActionListener(e -> {
            int lightTimeA = (Integer) spinnerA.getValue();
            int lightTimeB = (Integer) spinnerB.getValue();          

            SwingUtilities.invokeLater(() -> {
                LaunchUI();
            });

            lightTimesA.add(lightTimeA);
            lightTimesB.add(lightTimeB);


            Light lightA = new Light(349, 500, 1, 80, true, lightTimeA);
            Light lightB = new Light(349, 305, 80, 1, false, lightTimeB);

            simulation = new Simulation(lightA, lightB, this);
            try {
                simulation.runSim();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            refreshTable(simCSVTable);
        });
    }

    public void ExportData()
    {
        carTimesA.add(simulation.returnTimeA());
        carTimesB.add(simulation.returnTimeB());
        attempts.add(attempts.size() + 1);
        averages.add(((double) simulation.returnTimeA() + (double) simulation.returnTimeB()) / 2);

        int newLength = attempts.size();
        exportData(newLength, attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

        refreshTable(simCSVTable);
    }

    public void refreshTable (JTable table) {
        attempts.clear();
        lightTimesA.clear();
        lightTimesB.clear();
        carTimesA.clear();
        carTimesB.clear();
        averages.clear();
        importData(attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

        int length = attempts.size();
        Object[][] newTableData = new Object[length][6];

        for (int i = 0; i < length; i++) {
            newTableData[i] = new Object[]{attempts.get(i), lightTimesA.get(i), lightTimesB.get(i), carTimesA.get(i), carTimesB.get(i), averages.get(i)};
        }

        TableModel newModel = new DefaultTableModel(newTableData, column) {
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };

        RowSorter<TableModel> sorter = new TableRowSorter<>(newModel);

        table.setModel(newModel);
        table.setRowSorter(sorter);
    }
    private static void LaunchUI()
         {
            JFrame sf = new JFrame();
            sf.add(SimulationPanel.GetInstance());
            sf.pack();
            sf.setVisible(true);
            sf.setSize(828, 545);
         }
    public static void importData(ArrayList<Integer> list0, ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Long> list3, ArrayList<Long> list4, ArrayList<Double> list5) {
        String file = "src\\info.csv";
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            int i = 0;
            while ((line = reader.readLine())!= null){
                if (i ==0){
                    i++;
                    continue;
                }
                String[] values = line.split(",");
                list0.add(Integer.valueOf(values[0]));
                list1.add(Integer.valueOf(values[1]));
                list2.add(Integer.valueOf(values[2]));
                list3.add(Long.valueOf(values[3]));
                list4.add(Long.valueOf(values[4]));
                list5.add(Double.valueOf(values[5]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportData(int length, ArrayList<Integer> list0, ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Long> list3, ArrayList<Long> list4, ArrayList<Double> list5) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\info.csv"))){
            writer.write("attempts,lightA,lightB,timeA,timeB,averages");
            writer.newLine();
            for (int i = 0; i < length; i++) {
                String row = list0.get(i) + "," + list1.get(i) + "," + list2.get(i) + "," + list3.get(i) + "," + list4.get(i) + "," + list5.get(i);
                writer.write(row);
                writer.newLine();
            }
            writer.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}