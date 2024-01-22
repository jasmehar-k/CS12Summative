import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            MainPanel mp = new MainPanel();
            
            JFrame frame = new JFrame("Traffic Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(mp);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}


// import javax.swing.*;
// import javax.swing.table.TableRowSorter;
// import javax.swing.table.DefaultTableModel;
// import javax.swing.table.TableModel;
// import java.io.*;
// import java.util.Scanner;
// import java.util.ArrayList;
// import java.util.Arrays;
// import javax.swing.JFrame;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.RowSorter;

// public class Main {
//     public static void importData(ArrayList<Integer> list0, ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Double> list5) {
//         String file = "src\\info.csv";
//         BufferedReader reader = null;
//         String line = "";
//         try {
//             reader = new BufferedReader(new FileReader(file));
//             int i = 0;
//             while ((line = reader.readLine())!= null){
//                 if (i ==0){
//                     i++;
//                     continue;
//                 }
//                 String[] values = line.split(",");
//                 list0.add(Integer.valueOf(values[0]));
//                 list1.add(Integer.valueOf(values[1]));
//                 list2.add(Integer.valueOf(values[2]));
//                 list3.add(Integer.valueOf(values[3]));
//                 list4.add(Integer.valueOf(values[4]));
//                 list5.add(Double.valueOf(values[5]));
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static void exportData(int length, ArrayList<Integer> list0, ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4, ArrayList<Double> list5) {
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\info.csv"))){
//             writer.write("attempts,lightA,lightB,timeA,timeB,averages");
//             writer.newLine();
//             for (int i = 0; i < length; i++) {
//                 String row = list0.get(i) + "," + list1.get(i) + "," + list2.get(i) + "," + list3.get(i) + "," + list4.get(i) + "," + list5.get(i);
//                 writer.write(row);
//                 writer.newLine();
//             }
//             writer.flush();
//         }
//         catch (IOException e){
//             e.printStackTrace();
//         }
//     }

//     private static void LaunchUI()
//     {
//         JFrame sf = new JFrame();
//         sf.add(SimulationPanel.GetInstance());
//         sf.pack();
//         sf.setVisible(true);
//         sf.setSize(828, 545);
//     }

//     public static void main(String[] args) throws InterruptedException {
//         Scanner scan = new Scanner(System.in);

//         ArrayList<Integer> attempts = new ArrayList<>();
//         ArrayList<Integer> lightTimesA = new ArrayList<>();
//         ArrayList<Integer> lightTimesB = new ArrayList<>();
//         ArrayList<Integer> carTimesA = new ArrayList<>();
//         ArrayList<Integer> carTimesB = new ArrayList<>();
//         ArrayList<Double> averages = new ArrayList<>();

//         importData(attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

//         System.out.println("Welcome!");

//         int lightTimeA = -1;
//         int lightTimeB = -1;

//         while (lightTimeA < 0 || lightTimeA > 90) {
//             System.out.println("Please enter the time for light A (between 0-90s): ");
//             while (!scan.hasNextInt()) {
//                 System.out.println("INVALID INPUT >:( !!!!!!! Please enter the time for light A (between 0-90s): ");
//                 scan.next();
//             }
//             lightTimeA = scan.nextInt();
//         }

//         while (lightTimeB < 0 || lightTimeB > 90) {
//             System.out.println("Please enter the time for light B (between 0-90s): ");
//             while (!scan.hasNextInt()) {
//                 System.out.println("INVALID INPUT >:( !!!!!!! Please enter the time for light B (between 0-90s): ");
//                 scan.next();
//             }
//             lightTimeB = scan.nextInt();
//         }

//         System.out.println("Starting simulation...");

//         LaunchUI();

//         lightTimesA.add(lightTimeA);
//         lightTimesB.add(lightTimeB);

//         Light lightA = new Light(320, 0, 1, 545, false, lightTimeA);
//         Light lightB = new Light(320, 300, 800, 1, true, lightTimeB);
//         Simulation simulation = new Simulation(lightA, lightB);
//         simulation.runSim();

//         carTimesA.add(simulation.returnTimeA());
//         carTimesB.add(simulation.returnTimeB());
//         attempts.add(attempts.size() + 1);
//         averages.add(((double) simulation.returnTimeA() + (double) simulation.returnTimeB()) / 2);

//         int length = attempts.size();
//         exportData(length, attempts, lightTimesA, lightTimesB, carTimesA, carTimesB, averages);

//         Object[][] tableData = new Object[length][6];

//         for (int i = 0; i < length; i++) {
//             tableData[i] = new Object[]{attempts.get(i), lightTimesA.get(i), lightTimesB.get(i), carTimesA.get(i), carTimesB.get(i  ), averages.get(i)};
//         }

//         // Creating a table to display and sort the accumulated data
//         JFrame frame = new JFrame("");
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         String[] tableColumn = {"Attempt #", "Light A Time", "Light B Time", "Car A Time", "Car B Time", "Average Car Time"};

//         TableModel model = new DefaultTableModel(tableData, tableColumn) {
        
//             public Class getColumnClass(int column) {
//                 Class returnValue;
//                 if ((column >= 0) && (column < getColumnCount())) {
//                     returnValue = getValueAt(0, column).getClass();
//                 } else {
//                     returnValue = Object.class;
//                 }
//                 return returnValue;
//             }
//         };

//         RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

//         JTable table = new JTable(tableData, tableColumn);
//         table.setBounds(100, 55, 600, 400);
//         table.setRowSorter(sorter);
//         table.setDefaultEditor(Object.class, null);

//         JScrollPane jScrollPane = new JScrollPane(table);
//         frame.add(jScrollPane);
//         frame.setSize(600, 400);
//         frame.setVisible(true);

//         System.out.println("Goodbye! Don't come back :)");
//     }
// }