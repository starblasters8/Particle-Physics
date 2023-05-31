import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Demo extends JPanel
{
    private int maxFPS = 30;
    private ArrayList<ParticleGroup> pGroups = new ArrayList<ParticleGroup>();
    private double boundX, boundY, boundW, boundH;

    public Demo(int w, int h)
    {
        this.setPreferredSize(new Dimension(w,h));
        setBackground(Color.BLACK);

        boundX = 0;
        boundY = 0;
        boundW = w;
        boundH = h;

        Timer timer = new Timer(1000/maxFPS, new ActionListener() {public void actionPerformed(ActionEvent e) 
        {   repaint();  }});    timer.start();

        pGroups.add(new ParticleGroup(50,50,400,400,15,10,0.80,Color.RED, boundX,boundY,boundW,boundH));
        for(ParticleGroup p : pGroups)
        {
            p.randomizeMass(3);
            p.randomizeVX(5);
            p.randomizeElasticity(0.1);
        }

        // Create a ScheduledExecutorService to run the calculations on a separate thread
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() 
        {
            public void run() {
                for (ParticleGroup p : pGroups) {
                    p.updateAll();
                }
            }
        }, 0, 1000 / maxFPS, TimeUnit.MILLISECONDS);
    }

    public void paintComponent(Graphics gTemp)
    {
        // Setup
        super.paintComponent(gTemp);
        Graphics2D g = (Graphics2D)gTemp;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing

        for(ParticleGroup p : pGroups)
        {
            p.drawAll(g, true, true);
            p.drawBoundingBox(g, Color.WHITE);
        }
    }

    // JFrame
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Particle Physics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Demo(800,800));
        frame.pack();
        frame.setVisible(true);
    }
}