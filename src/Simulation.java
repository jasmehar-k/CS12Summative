import java.util.concurrent.RunnableFuture;

import javafx.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
//828x545
public class Simulation implements Runnable{
    boolean carADone = false;
    boolean carBDone = false;
    int lightATime;
    int lightBTime;
    Light lightA;
    Light lightB;
    long lastLightToggleTime = 0;
    SimulationTimer timerA = new SimulationTimer("TimerA"); // timing how long car A is taking to reach its destination
    SimulationTimer timerB = new SimulationTimer("TimerB"); // timing how long car B is taking to reach its destination
    SimulationTimer timer = new SimulationTimer("Timer");  // timing the whole simulation
    int lane05Y = 222;  // y coordinate for lane 0 and 5
    int lane14Y = 265; // y coordinate for lane 1 and 4
    int lane27X = 415; // x coordinate for lane 2 and 7
    int lane36X = 371; // x coordinate for lane 3 and 6

    Intersection intersection = new Intersection();
    
    ArrayList<Car> carList = new ArrayList<Car>();;
    Car carA = new Car(0,lane14Y, 50, 25,4,4, lightA, intersection, true, this);
    Car carB = new Car(lane36X,470, 25, 50, 3,3,lightB,intersection, true, this);
     
    Car car20 = new Car(lane27X,320,25,50,2,genRandom(2, 3),lightB,intersection,false,this);
    
    Car car21 = new Car(lane27X,395,25,50,2,genRandom(2, 3),lightB,intersection,false,this);
    Car car22 = new Car(lane27X,470,25,50,2,genRandom(2, 3),lightB,intersection,false,this);
    Car car23 = new Car(lane27X,545,25,50,2,genRandom(2, 3),lightB,intersection,false,this);
  
    //Car car24 = new Car(lane27X,620,25,50,2,genRandom(2, 3),lightB,intersection,false,this);
    //Car car25 = new Car(lane27X,695,25,50,2,genRandom(2, 3),lightB,intersection,false,this);
    //Car car26 = new Car(lane27X,770,25,50,2,genRandom(2, 3),lightB,intersection,false,this);

    Car car30 = new Car(lane36X,320,25,50,3,genRandom(2, 3),lightB,intersection,false,this);
    Car car31 = new Car(lane36X,395,25,50,3,genRandom(2, 3),lightB,intersection,false,this);
    //Car car32 = new Car(lane36X,545,25,50,3,genRandom(2, 3),lightB,intersection,false,this);
    //Car car33 = new Car(lane36X,620,25,50,3,genRandom(2,3),lightB,intersection,false,this);
    //Car car34 = new Car(lane36X,695,25,50,3,genRandom(2, 3),lightB,intersection,false,this);
    //Car car35 = new Car(lane36X,770,25,50,3,genRandom(2, 3),lightB,intersection,false,this);

   // Car car40 = new Car(-225,lane14Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);
   // Car car41 = new Car(-150,lane14Y,50,25,4,genRandom(4,5),lightA,intersection,false,this);
    //Car car42 = new Car(-75,lane14Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);
    Car car43 = new Car(75,lane14Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);
    Car car44 = new Car(150,lane14Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);
    Car car45 = new Car(225,lane14Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);
    Car car46 = new Car(300,lane14Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);
    //Car car47 = new Car(75,lane05Y,50,25,4,genRandom(4, 5),lightA,intersection,false,this);

    //Car car50 = new Car(-225,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false, this);
    //Car car51 = new Car(-150,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false, this);
    //Car car52 = new Car(-75,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false, this);
    Car car53 = new Car(0,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false,this);
    Car car54 = new Car(150,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false,this);
    Car car55 = new Car(225,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false,this);
    Car car56 = new Car(300,lane05Y,50,25,5,genRandom(4, 5),lightA,intersection,false,this);   


    public Simulation(Light lightA, Light lightB) {
        this.lightATime = lightA.time;
        this.lightBTime = lightB.time;
        this.lightA = lightA;
        this.lightB = lightB;

        carA.setLight(lightA);
        carB.setLight(lightB);
        
        
        car20.setLight(lightB);
        car21.setLight(lightB);
        car22.setLight(lightB);
        car23.setLight(lightB);
       // car24.setLight(lightB);
        //car25.setLight(lightB);
        //car26.setLight(lightB);
        
        car30.setLight(lightB);
        car31.setLight(lightB);
        //car32.setLight(lightB);
        // car33.setLight(lightB);
        // car34.setLight(lightB);
        // car35.setLight(lightB);

        // car40.setLight(lightA);
        // car41.setLight(lightA);
        // car42.setLight(lightA);
        car43.setLight(lightA);
        car44.setLight(lightA);
        car45.setLight(lightA);
        car46.setLight(lightA);
        // car47.setLight(lightA);

        // car50.setLight(lightA);
        // car51.setLight(lightA);
        // car52.setLight(lightA);
        car53.setLight(lightA);
        car54.setLight(lightA);
        car55.setLight(lightA);
        car56.setLight(lightA);
        

        carSetup(car43, carA, 4);
        carSetup(car44, car43, 4);
        carSetup(car45, car44, 4);
        carSetup(car46, car45, 4);

        carSetup(car31, carB, 3);
        carSetup(car30, car31, 3);
        
        carSetup(car22, car23, 2);
        carSetup(car21, car22, 2);
        carSetup(car20, car21, 2);

        carSetup(car54, car53, 5);
        carSetup(car55, car54, 5);
        carSetup(car56, car55, 5);
        

        carList.add(carA);
        carList.add(carB);
        carList.add(car20);
        carList.add(car21);
        carList.add(car22);
        carList.add(car23);
        // carList.add(car24);
        // carList.add(car25);
        // carList.add(car26);
        carList.add(car30);
        carList.add(car31);
        //carList.add(car32);
        // carList.add(car33);
        // carList.add(car34);
        // carList.add(car35);
        // carList.add(car40);
        // carList.add(car41);
        // carList.add(car42);
         carList.add(car43);
         carList.add(car44);
        carList.add(car45);
        carList.add(car46);
        // carList.add(car47);
        // carList.add(car50);
        // carList.add(car51);
        // carList.add(car52);
        carList.add(car53);
        carList.add(car54);
        carList.add(car55);
        carList.add(car56);
        

        

        StartAnimationTimer();
    }

    private void carSetup(Car inFront, Car behind, int lane){
        inFront.setBehind(behind);
        behind.setFront(inFront);
           }
    private void StartAnimationTimer()
    {
        java.awt.event.ActionListener listener = new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                SimulationPanel.GetInstance().UpdateCars(carList);
            }
        };

        javax.swing.Timer aniTimer = new javax.swing.Timer(500, listener);
        aniTimer.start();
    }
    
    public int genRandom(int min, int max){ // generates random integer given a domain
        Random random = new Random();
        int num = random.nextInt((max - min) + 1) + min;
        return num;
    }

    Thread tA = new Thread(carA);
    Thread tB = new Thread(carB);

    Thread t20 = new Thread(car20);
    Thread t21 = new Thread(car21);
    Thread t22 = new Thread(car22);
    Thread t23 = new Thread(car23);
    // Thread t24 = new Thread(car24);
    // Thread t25 = new Thread(car25);
    // Thread t26 = new Thread(car26);

    Thread t30 = new Thread(car30);
    Thread t31 = new Thread(car31);
    //Thread t32 = new Thread(car32);
    // Thread t33 = new Thread(car33);
    // Thread t34 = new Thread(car34);
    // Thread t35 = new Thread(car35);

    // Thread t40 = new Thread(car40);
    // Thread t41 = new Thread(car41);
    // Thread t42 = new Thread(car42);
    Thread t43 = new Thread(car43);
    Thread t44 = new Thread(car44);
    Thread t45 = new Thread(car45);
    Thread t46 = new Thread(car46);
    // Thread t47 = new Thread(car47);

    // Thread t50 = new Thread(car50);
    // Thread t51 = new Thread(car51);
    // Thread t52 = new Thread(car52);
    Thread t53 = new Thread(car53);
    Thread t54 = new Thread(car54);
    Thread t55 = new Thread(car55);
    Thread t56 = new Thread(car56);

    Thread simulationThread = new Thread(this);
    public void run(){
        boolean lightATurn = false;
        while (!isAllDone()){

            if (timer.secondPassed()) {
                if ((timer.getCurrentSeconds() % 60) < 10) {
                    System.out.println(timer.getCurrentSeconds() / 60 + ":0" + timer.getCurrentSeconds() % 60);
                } else {
                    System.out.println(timer.getCurrentSeconds() / 60 + ":" + timer.getCurrentSeconds() % 60);
                }
            }
// Traffic Lights Code
            if (lightA.checkIsGreen() && timer.getCurrentSeconds() - lastLightToggleTime == lightATime) {
                lightA.setValue(false);
                //lightB.setValue(true);
                lastLightToggleTime = timer.getCurrentSeconds();
            }
            else if (!lightATurn && !lightA.checkIsGreen() && !lightB.checkIsGreen() && timer.getCurrentSeconds() - lastLightToggleTime == 10) {
                lightB.setValue(true);
                lightATurn = true;
                lastLightToggleTime = timer.getCurrentSeconds();
                System.out.println("light b");
            }
            else if ( lightB.checkIsGreen() && timer.getCurrentSeconds() - lastLightToggleTime == lightBTime) {
                lightB.setValue(false);
                //lightB.setValue(true);
                lastLightToggleTime = timer.getCurrentSeconds();
            }
            else if (lightATurn && !lightA.checkIsGreen() && !lightB.checkIsGreen() && timer.getCurrentSeconds() - lastLightToggleTime == 10) {
                lightA.setValue(true);
                //lightB.setValue(true);
                lightATurn = false;
                lastLightToggleTime = timer.getCurrentSeconds();
                System.out.println("light a");
            }
        }
    
        timer.stopTimer();
        System.out.println("Car A Time: " + timerA.getSeconds());
        System.out.println("Car B Time: " + timerB.getSeconds());
    }
    
    /* 
    public void run(){
        while (!isAllDone()){

            //System.out.println("lightA="+lightA.checkIsGreen() + ", lightB="+lightB.checkIsGreen()); 

            if (timer.secondPassed()) {
                if ((timer.getCurrentSeconds() % 60) < 10) {
                    System.out.println(timer.getCurrentSeconds() / 60 + ":0" + timer.getCurrentSeconds() % 60);
                } else {
                    System.out.println(timer.getCurrentSeconds() / 60 + ":" + timer.getCurrentSeconds() % 60);
                }
            }

            if (timer.getCurrentSeconds() % (lightATime + lightBTime) == lightATime || timer.getCurrentSeconds() == lightATime) {
                lightA.setValue(false);
                lightB.setValue(true);
            }
    
            if (timer.getCurrentSeconds() % (lightATime + lightBTime) == 0) {
                lightA.setValue(true);
                lightB.setValue(false);
            }

            if ((timer.getCurrentSeconds() + 7) % (lightATime + lightBTime) == lightATime || timer.getCurrentSeconds() == lightATime) {
                lightA.setValue(false);
                lightB.setValue(false);
            }

            if ((timer.getCurrentSeconds() + 7) % (lightATime + lightBTime) == 0) {
                lightA.setValue(false);
                lightB.setValue(false);
            }

        }
        timer.stopTimer();
        System.out.println("Car A Time: " + timerA.getSeconds());
        System.out.println("Car B Time: " + timerB.getSeconds());
    }
    */

    public void runSim() throws InterruptedException{
        timerA.startTimer();
        timerB.startTimer();
        timer.startTimer();
        lightA.setValue(true);
        lightB.setValue(false);

        simulationThread.start();

        tA.start();
        tB.start();

        t20.start();
        t21.start();
        t22.start();
        t23.start();
        // t24.start();
        // t25.start();
        // t26.start();
        t30.start();
        t31.start();
       // t32.start();
        // t33.start();
        // t34.start();
        
        // t35.start();
        // t40.start();
        // t41.start();
        // t42.start();
        t43.start();
        t44.start();
        t45.start();
        t46.start();
        // t47.start();
        // t50.start();
        // t51.start();
        // t52.start();
        t53.start();
        t54.start();
        t55.start();
        t56.start();
        
        /* 
        tA.join();
        tB.join();
        

        t20.join();
        t21.join();
        t22.join();
        t23.join();
        // t24.join();
        // t25.join();
        // t26.join();
        t30.join();
        t31.join();
        //t32.join();
        // t33.join();
        // t34.join();
        // t35.join();
        
        // t40.join();
        // t41.join();
        // t42.join();
        t43.join();
        t44.join();
        t45.join();
        t46.join();
        // t47.join();
        // t50.join();
        // t51.join();
        // t52.join();
        t53.join();
        t54.join();
        t55.join();
        t56.join();
        */
        
        
    }
    public void setADone(){
        timerA.stopTimer();
        carADone = true;
        System.out.println("car a is done!!!! wee");
    }
    public void setBDone(){
        timerB.stopTimer();
        carBDone = true;
        System.out.println("car b is done!!!! woo");
    }
    public boolean isAllDone(){
        if (carADone && carBDone){
            System.out.println("DONE");
            return true;
        }
        return false;
    }

    public long returnTimeA() {
        return timerA.getSeconds();
    }

    public long returnTimeB() {
        return timerB.getSeconds();
    }
}