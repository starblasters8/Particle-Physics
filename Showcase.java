import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Showcase extends JPanel
{
    private int maxFPS = 30;

    public Showcase(int w, int h)
    {
        this.setPreferredSize(new Dimension(w,h));
        setBackground(Color.BLACK);

        Timer timer = new Timer(1000/maxFPS, new ActionListener() {public void actionPerformed(ActionEvent e) 
            {   repaint();  }});    timer.start();
    }

    public void paintComponent(Graphics gTemp)
    {
        // Setup
        super.paintComponent(gTemp);
        Graphics2D g = (Graphics2D)gTemp;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing
 

    }

    // JFrame
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Particle Physics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Showcase(800,800));
        frame.pack();
        frame.setVisible(true);
    }
}
