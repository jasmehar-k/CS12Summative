import javax.swing.JPanel;

public class TrafficObject extends JPanel{
    int x;
    int y;
    int width;
    int length;
    static Intersection intersection;
    public TrafficObject(int x, int y, int width, int length) {  // width = hor, length = vert
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getLength() {
        return length;
    }
    public int getRightValue(){
        return x-width;
    }
    public int getLeftValue(){
        return x;
    }
    public int getTopValue(){
        return y;
    }
    public int getBottomValue(){
        return y+length;
    }
    public void setX(int xVal){
        x = xVal;
    }
    public void setY(int yVal){
        y = yVal;
    }
    public void rotate(){ //rotates an object by 90 degrees about (x,bottomValye)
        setY(getBottomValue()-width);
        int oldL = length;
        length = width;
        width = oldL;
        // change image here
    }

}