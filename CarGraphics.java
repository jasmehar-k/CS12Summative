import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class CarGraphics extends JPanel {
    public int speed;
    public int width;
    public int length;
    public Rectangle car;
    public boolean isHorizontal;
    public boolean isSpecial;
    public int x;
    public int y;
    Color grey = new Color(100,100,100);
    Color red = new Color (255, 0,0);
    Color blue = new Color (0,0,255);
    //public MainFrame app;

    public CarGraphics(int x, int y, boolean isHorizontal, boolean isSpecial){
        this.x = x;
        this.y = y;
        if (isHorizontal){
            this.width = 50;
            this.length = 25;
        }
        else{
            this.width = 25;
            this.length = 50;
        }
        car = new Rectangle(x,y,this.width,this.length);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the car at its current position
        draw(g);
    }

    public void draw(Graphics g){
        if (!isSpecial){
            g.setColor(grey);
        }
        else if (isHorizontal){
            g.setColor(red);
        }
        else{
            g.setColor(blue);
        }
        g.fillRect(x, y, width, length);

    }
 }
