import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap; // import the HashMap class
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Intersection {
    ConcurrentHashMap<Integer,TreeSet<Car>> cars = new ConcurrentHashMap<Integer, TreeSet<Car>>();
    //horizontal roads; sorted by x value left to right (ascending)
    TreeSet<Car> lane0 = new TreeSet<>(Comparator.comparingInt(Car::getX));
    TreeSet<Car> lane1 = new TreeSet<>(Comparator.comparingInt(Car::getX));
    TreeSet<Car> lane4 = new TreeSet<>(Comparator.comparingInt(Car::getX));
    TreeSet<Car> lane5 = new TreeSet<>(Comparator.comparingInt(Car::getX));
    //vertical roads; sorted by y value bottom to top (descending)
    TreeSet<Car> lane2 = new TreeSet<>(Comparator.comparingInt(Car::getY).reversed());
    TreeSet<Car> lane3 = new TreeSet<>(Comparator.comparingInt(Car::getY).reversed());
    TreeSet<Car> lane6 = new TreeSet<>(Comparator.comparingInt(Car::getY).reversed());
    TreeSet<Car> lane7 = new TreeSet<>(Comparator.comparingInt(Car::getY).reversed());

    public Intersection(){
        cars.put(0,lane0);
        cars.put(1,lane1);
        cars.put(2,lane2);
        cars.put(3,lane3);
        cars.put(4,lane4);
        cars.put(5,lane5);
        cars.put(6,lane6);
        cars.put(7,lane7);
    }

    public synchronized TreeSet<Car> getCars(int laneNum) {  // outputs all the cars in a specified lane as a tree set
        return cars.get(laneNum); 
    }
    public synchronized void addCar (Car car, int laneNum){ // adds a car to a specific lane
        TreeSet<Car> lane = cars.get(laneNum); // O (log n)
        lane.add(car); // O(log n)
        cars.put(laneNum, lane);
        System.out.println(cars.get(laneNum).contains(car));
    }
    public synchronized void removeCar (Car car, int laneNum){ // removes a car from its lane
        TreeSet<Car> lane = cars.get(laneNum);
        if (lane.contains(car)){
            lane.remove(car);
            cars.put(laneNum, lane);
        }
    }
    public void switchLanes (Car car, int oldLane, int newLane){
        removeCar(car, oldLane);
        addCar (car, newLane);
    }
}