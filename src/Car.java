import java.util.TimerTask;
import java.util.TreeSet;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Car extends TrafficObject implements Runnable {

    Light light; 
    int lane; 
    int objective; //objective lane to be at by the intersection
    boolean turning = false;
    int topSpeed;
    //long reactionTime = random.nextLong();
    long reactionTime = 780L + (long)(Math.random()*(1220L-780L)); // reaction time in ms  
    double speed ; //kmh, scaled to pixels/5 seconds,  (pixels/2.5)/500 milliseconds
    double accelerateFactor = 0.5; //change
    Intersection intersection;
    boolean passed = false; // if the car has passed the intersection

    //Doubly linked list with front and behind cars:
    Car frontCar; // car in front of the car
    Car behindCar; // car behind the car
    boolean isHorizontal;
    boolean isSpecial;
    Simulation simulation;
    int delayNum = 0;

    String imagePath;
       
    public Car(int x, int y, int width, int length, int lane, int objective, Light light, Intersection intersection, boolean isSpecial, Simulation simulation){ 
        super(x, y, width, length);
        this.lane = lane;
        this.objective = objective;
        this.topSpeed = genRandom(65,75);
        this.lane = lane;
        this.light = light;
        this.intersection = intersection;
        this.simulation = simulation;
        intersection.addCar(this, lane);
        TreeSet <Car>laneCars = intersection.getCars(lane);
        //frontCar = laneCars.higher(this);
        //behindCar = laneCars.lower(this);
        System.out.println(this.getName() + ": car in front: " + frontCar);
        System.out.println(this.getName() + ": car in front thru map: " + laneCars.higher(this));

        ArrayList<Integer> horRoads = new ArrayList<Integer>(
            Arrays.asList(0,1,4,5)
        );
        isHorizontal = horRoads.contains(lane);
        //System.out.println("car is horizontal: " + isHorizontal);
        this.isSpecial = isSpecial;

        if(isHorizontal){
            if(isSpecial){
                this.imagePath = "carA.png";
            }
            else{
                this.imagePath = "carH.png";
            }
        }
        else{
            if (isSpecial){
                this.imagePath = "carB.png";
            }
            else{
                this.imagePath = "carV.png";
            }
        }
    }
    public void setFront(Car car){
        frontCar = car;
    }
    public void setBehind(Car car){
        behindCar = car;
    }
   
    public int genRandom(int min, int max){ // generates random integer given a domain
        Random random = new Random();
        int num = random.nextInt((max - min) + 1) + min;
        return num;
    }

    public void run()
    {
        try {
            drive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

// lanes indexing:
/*             6 7
 *            | ^ |
 *            | | |
 *            | | |
 *            | | |
  5   --------     --------    0
      ------->     ------->
  4   --------     --------    1
 *            | ^ |
 *            | | |
 *            | | |
 *            | | |
 *             3 2
 * total screen: 807 x 546
 * 
 * 0: 
 *  x > 807 - 361 = 446 
 *  214<=y<=251
 * 1:
 *  x > 807 - 361 = 446
 *  251 <= y <= 
*/

    public void setLight(Light light){
        this.light = light;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setLane(int lane){
        this.lane = lane;
    }
    public void accelerate(){
        speed += speed*accelerateFactor;
        if (speed > topSpeed){
            speed = topSpeed;
            //System.out.println("top speed");
        }
    }
    public void decelerate(){
        speed -= accelerateFactor;
        System.out.println("decelerating");
        if (speed < 0){
            speed = 0;
        }
    }
    public void setPos(int xVal,int yVal){
        x = xVal;
        y = yVal;
    }
    public void move(){
        //System.out.println("speed: " + speed);

        if(isHorizontal){
            x += speed/5;
        }
        else{
            y -= speed/5;
        }
    }

    public boolean checkRedLight(){ // returns true if light is red and car is at the intersection
        if (light.checkIsGreen()){
            return false;
        }
        
        return checkIntersection();
    }
        
public boolean checkIntersection(){ // returns true if at the intersection
    if (passed){
        return false;
    }
    if (isHorizontal && light.getLeftValue() - x <= 30){
        //System.out.println("horixontal intersection");
        return true;

    }
    else if (!isHorizontal && y - light.getBottomValue() <= 30){
        //System.out.println(y-light.getBottomValue());
        return true;
        
    }
    return false;
}

    public boolean checkCarFront(){ // returns true if there is a car closeby in the front
        if (frontCar == null){
            return false;
        }
        int distance; 
        if (isHorizontal){
            distance = frontCar.getLeftValue() -x;
        }
        else{
            distance = y - frontCar.getBottomValue();
        }
        if (distance <= 60){

            return true;
        }
        return false;
    }

    public void startDriving(){ // waits for reaction time before accelerating
        TimerTask accelerate = new TimerTask() {
            public void run(){
                speed = 0.5;
                accelerate();
                accelerate();
                accelerate();
            }
        };
        Timer timer = new Timer();
        timer.schedule(accelerate,reactionTime);
    }      
    // switch the car's lanes (could be while changing lanes or turning)
    public void switchLanes(int oldLane, int newLane){
        TreeSet <Car>newLaneCars = intersection.getCars(newLane);
        intersection.switchLanes(this, oldLane, newLane);
        lane = newLane;
        if (behindCar != null){
            behindCar.frontCar = frontCar;
        }
        if (frontCar != null){
            frontCar.behindCar = behindCar;
        }
        frontCar = newLaneCars.higher(this);
        System.out.println("Switched lanes, new in front is " + frontCar);
        behindCar = newLaneCars.lower(this);
        if (frontCar != null){
            frontCar.behindCar = this;
        }
        if (behindCar != null){
            behindCar.frontCar = this;
        }
    }
    // changing lanes while driving
    public void changeLanes(int side, int oldLane, int newLane){  // side: -1:go left, 1:go right
        //System.out.println("changing lanes");
        TreeSet <Car>newLaneCars = intersection.getCars(newLane);
        if (oldLane <4){
            if (newLaneCars != null){
                for (Car newLaneCar : newLaneCars){
                        if (!(newLaneCar.getBottomValue()<y- 7 || newLaneCar.getTopValue()>this.getBottomValue() + 7)){
                            return;
                        }
                }
        }
            x= x + 40*side;
        }
        else{
            if (newLaneCars != null){
                for (Car newLaneCar : newLaneCars){
                    if(!(newLaneCar.getRightValue() < this.getLeftValue() - 7 || newLaneCar.getLeftValue() > this.getRightValue() + 7 )){ // checks if there's another car where this car needs to go
                        // car blocking    
                    return;
                    }
                }
        }
            y = y + 40*side;
        }
        switchLanes(oldLane, newLane);
    }

    public void turn(int oldLane, int newLane) throws InterruptedException{
        String newImagePath;
        if (isHorizontal){
            newImagePath = "carV.png";
        }
        else{

            newImagePath = "carH.png";
        }
        this.rotate();

        isHorizontal = !isHorizontal;
        imagePath=newImagePath;
        passed = true;
        System.out.println("turned");
        switchLanes(oldLane, newLane);
    }
    public void pass(int oldLane, int newLane) throws InterruptedException{
        //System.out.println("went straight " + lane );
        passed = true;
        switchLanes(oldLane, newLane);
    }

    public void drive() throws InterruptedException{
        //System.out.println(y-light.getBottomValue());
        while (!simulation.isAllDone()){ 
            //System.out.println(light.isGreen);
            Thread.sleep(1000);
            
            delayNum++;
            //System.out.println("lane:" + lane);
            
            move();
            // controlling speed:
            if (checkRedLight() || checkCarFront()){  // light is red or car in front
                //System.out.println("red light or car");
                if (speed > 0){ // moving
                    speed = 0;
                }
            }
            else if (speed == 0){ // light is green and no car in front, not moving
                speed = 0.5;
                startDriving();
            }
            else{ // light is green, no car in front, moving
                //System.out.println("accelerating: " + lane);
                for (int i = 0; i < 10; i ++){
                    accelerate();
                }
            } 

            // changing lanes if needed:
            if (!passed && lane != objective && x > 80 && y < 480 && delayNum > 7){
                
                if (lane < objective){
                    changeLanes(-1, lane, lane + 1);
                }
                else{
                    changeLanes(1, lane, lane - 1);
                }
            }
            // at intersection, light is green
            if (!checkRedLight()){
                accelerate();
                move();
                passed = true;
                if (lane ==2 && y <= 240){
                    turn(2, 1);
                }
                else if (lane == 5 && x >= 360){
                    turn (5, 6);
                }
                else if (lane == 4 && x<467 ){
                    pass(4,1);
               }
                else if (lane == 3 && y < 197){
                    pass (3,6);
                }
                
            }
            if (passed){
                //System.out.println("passed");
                if (isHorizontal && isSpecial){
                    System.out.println("car A: (" + x + "," + y + ")");

                }
                if (x>=800){
                    System.out.println("done! " + lane);
                    if(isSpecial){ //CarA done
                        System.out.println("Car a donee");
                        simulation.setADone();
                    }
                    if(behindCar != null){
                        behindCar.frontCar = null;
                    }
                    intersection.removeCar(this, lane);
                    return;

                }
                else if(y<= -50){
                    System.out.println("done!" + lane);
                    if (isSpecial){ // Car B done
                        System.out.println("car b donee");
                        simulation.setBDone();
                    }
                    else{
                        //respawn
                    }
                    if (behindCar != null){
                        behindCar.frontCar = null;
                    }
                    intersection.removeCar(this, lane);
                    return;
                }
            }
            delayNum++;
        }
    }
}