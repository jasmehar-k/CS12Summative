import java.util.TimerTask;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;

public class Car extends TrafficObject implements Runnable {

    Light light; 
    int lane; 
    int objective; //objective lane to be at by the intersection
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
    boolean isSpecial; // car A and car B are special
    Simulation simulation;
    int delayNum = 0; // variable set to delay how soon cars can change lanes

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
        System.out.println(this.getName() + ": car in front: " + frontCar);
        System.out.println(this.getName() + ": car in front thru map: " + laneCars.higher(this));

        ArrayList<Integer> horRoads = new ArrayList<Integer>(
            Arrays.asList(0,1,4,5)
        );
        isHorizontal = horRoads.contains(lane);
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
 * total screen: 828 x 545
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
        }
    }
    public void decelerate(){
        speed -= accelerateFactor;
        if (speed < 0){
            speed = 0;
        }
    }
    public void setPos(int xVal,int yVal){
        x = xVal;
        y = yVal;
    }
    public void move(){
        if(isHorizontal){
            x += speed/10;
        }
        else{
            y -= speed/10;
        }
    }

    public boolean checkIfCarShouldStop(){ // returns true if light is red and car is at the intersection
       if(passed)
       {
           return false;
       }
       return checkIfAtIntersection() && !light.checkIsGreen();
    }
        
public boolean checkIfAtIntersection(){ // returns true if at the intersection
    if (isHorizontal && x >= light.getLeftValue() - 70){
        return true;

    }
    else if (!isHorizontal && y <= light.getBottomValue() + 110){
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
        TreeSet <Car>newLaneCars = (TreeSet <Car>)intersection.getCars(newLane).clone();
        if (oldLane <4){
            if (newLaneCars != null){
                for (Car newLaneCar : newLaneCars){
                        if (!(newLaneCar.getBottomValue() <y - 7 || newLaneCar.getTopValue()>this.getBottomValue() + 7)){
                            return;
                        }
                }
        }
            x= x + 40*side;
        }
        else{
            if (newLaneCars != null){
                for (Car newLaneCar : newLaneCars){
                    if(newLaneCar.getRightValue() < this.getLeftValue() - 7 || newLaneCar.getLeftValue() > this.getRightValue() + 7 ){ // checks if there's another car where this car needs to go
                        // car blocking    
                    return;
                    }
                }
        }
            y = y + 40*side;
        }
        switchLanes(oldLane, newLane);
    }

    public void turn(int oldLane, int newLane) throws InterruptedException{ //turning
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
        System.out.println("turned");
        switchLanes(oldLane, newLane);
        passed = true;

    }
    public void pass(int oldLane, int newLane) throws InterruptedException{ //pass the intersecton
        switchLanes(oldLane, newLane);
        passed = true;
    }

    public void drive() throws InterruptedException{
        while (!simulation.isAllDone()){ 
            Thread.sleep(1000);
            
            delayNum++;
            
            move();
            // controlling speed:
            if (checkIfCarShouldStop() || checkCarFront()){  // light is red or car in front
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

            //see if car has already passed intersection
            if((isHorizontal && getRightValue() > light.getRightValue()+5) ||
               (!isHorizontal && getTopValue() < light.getTopValue()-5))
            {
                passed = true;
            }

            // at intersection, light is green
            if (!checkIfCarShouldStop()){
                accelerate();
                move();

                if (lane ==2 && y <= 250){
                    turn(2, 1);
                }
                else if (lane == 5 && x >= 350){
                    turn (5, 6);
                }
                else if (lane == 4 && x >= 300 ){
                    pass(4,1);
               }
                else if (lane == 3 && y <= 250){
                    pass (3,6);
                }
                
            }
            if (passed){
                //System.out.println("passed");
                if (isHorizontal && isSpecial){
                    System.out.println("car A: (" + x + "," + y + ")");

                }
                if (x>=800){ //end limit for horizontal cars
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
                else if(y<= -50){ // end limit for vertical cars
                    System.out.println("done!" + lane);
                    if (isSpecial){ // Car B done
                        System.out.println("car b donee");
                        simulation.setBDone();
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