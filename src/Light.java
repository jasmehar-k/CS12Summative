public class Light extends TrafficObject{
    boolean isGreen;
    int time; // time it is green for, seconds

    public Light(int x, int y, int width, int length, boolean isGreen, int time) {
        super(x, y, width, length);
        this.isGreen = isGreen;
        this.time = time;
    }

    public boolean checkIsGreen() {
        return isGreen;
    }

    public void setValue(boolean isGreen) {
        this.isGreen = isGreen;
    }
    
}
