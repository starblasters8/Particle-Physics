import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Showcase extends JPanel
{
    private int maxFPS = 30;
    private ArrayList<ParticleGroup> pGroups = new ArrayList<ParticleGroup>();

    public Showcase(int w, int h)
    {
        this.setPreferredSize(new Dimension(w,h));
        setBackground(Color.BLACK);

        Timer timer = new Timer(1000/maxFPS, new ActionListener() {public void actionPerformed(ActionEvent e) 
        {   repaint();  }});    timer.start();

        pGroups.add(new ParticleGroup(50,50,400,400,10,1,Color.RED));
        for(ParticleGroup p : pGroups)
            p.randomizeMass(0.5);
    }

    public void paintComponent(Graphics gTemp)
    {
        // Setup
        super.paintComponent(gTemp);
        Graphics2D g = (Graphics2D)gTemp;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing
 
        for(ParticleGroup p : pGroups)
        {
            p.updateAll();
            p.drawAll(g);
            p.drawBoundingBox(g, Color.WHITE);
        }
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
