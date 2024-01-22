import javax.swing.JPanel;
import javax.imageio.ImageIO;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
 
class SimulationPanel extends JPanel {
    static SimulationPanel sPanel = new SimulationPanel();

    BufferedImage bg;
    BufferedImage orgBgImage;
    BufferedImage greenLight;
    BufferedImage redLight;
   

    public SimulationPanel() {
        try {
            orgBgImage = ImageIO.read(getClass().getResource("[ICS4U0] Traffic Lanes.png"));

            bg = new BufferedImage(orgBgImage.getWidth(), orgBgImage.getHeight(), orgBgImage.getType());
            greenLight = ImageIO.read(getClass().getResource("greenLight.png"));
            redLight = ImageIO.read(getClass().getResource("redLight.png"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
     
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(bg,0,0,bg.getWidth(),bg.getHeight(),null);
    } 
    
    public void UpdateCars(ArrayList<Car> cars)
    {
        try{
            Graphics g = bg.getGraphics();

            g.clearRect(0,0,bg.getWidth(),bg.getHeight());
            g.drawImage(orgBgImage,0,0,bg.getWidth(),bg.getHeight(),null);

            // draw traffic light
            if (cars.get(0).light.checkIsGreen()){
                g.drawImage(greenLight, 330, 235, 40,40,null);
            }
            else{
                g.drawImage(redLight, 330, 235, 40,40,null);
            }
            if (cars.get(1).light.checkIsGreen()){
                g.drawImage(greenLight, 390, 290, 40,40,null);
            }
            else{
                g.drawImage(redLight, 390, 290, 40,40,null);
            }
            for(Car car : cars)
            {
                BufferedImage carImage = ImageIO.read(getClass().getResource(car.getImagePath()));
                g.drawImage(carImage, car.getX(), car.getY() , null );
            }

            g.dispose();

            repaint();
         } catch (Exception e){
             System.out.println("Image cannot be found!");
         }
    }

    public static SimulationPanel GetInstance()
    {
        return sPanel;  
    }
}
