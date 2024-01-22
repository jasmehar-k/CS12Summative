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
    ArrayList<Integer> attempts = new ArrayList<>();
    ArrayList<Integer> lightTimesA = new ArrayList<>();
    ArrayList<Integer> lightTimesB = new ArrayList<>();
    ArrayList<Long> carTimesA = new ArrayList<>();
    ArrayList<Long> carTimesB = new ArrayList<>();
    ArrayList<Double> averages = new ArrayList<>();
    public MainPanel() {
        importData(attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Time for light A: "));
        inputPanel.add(spinnerA);
        inputPanel.add(new JLabel("Time for light B: "));
        inputPanel.add(spinnerB);
        inputPanel.add(button);

        int length = attempts.size();
        Object[][] tableData = new Object[length][6];

        for (int i = 0; i < length; i++) {
            tableData[i] = new Object[]{attempts.get(i), lightTimesA.get(i), lightTimesB.get(i), carTimesA.get(i), carTimesB.get(i), averages.get(i)};
        }
        

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

        JTable table = new JTable(model);
        table.setBounds(100, 55, 1000, 200);
        table.setRowSorter(sorter);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(table), BorderLayout.CENTER);

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

            Simulation simulation = new Simulation(lightA, lightB);
            try {
                simulation.runSim();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            carTimesA.add(simulation.returnTimeA());
            carTimesB.add(simulation.returnTimeB());
            attempts.add(attempts.size() + 1);
            averages.add(((double) simulation.returnTimeA() + (double) simulation.returnTimeB()) / 2);

            int newLength = attempts.size();
            exportData(newLength, attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

            refreshTable(table);
        });
    }
    

    /*  LaunchUI();

        lightTimesA.add(lightTimeA);
        lightTimesB.add(lightTimeB);

        Light lightA = new Light(320, 0, 1, 545, false, lightTimeA);
        Light lightB = new Light(320, 300, 800, 1, true, lightTimeB);
        Simulation simulation = new Simulation(lightA, lightB);
        simulation.runSim(); */
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