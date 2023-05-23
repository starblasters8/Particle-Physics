import java.awt.Color;
import java.awt.Graphics2D;

public class Particle 
{
    private double x, y; // X and Y coordinates of top left corner
    private double vx, vy; // X and Y velocities
    private double r, m; // Radius and mass
    private Color c; // Color

    public Particle(double x, double y, double r, double m, Color c)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        this.m = m;
        this.c = c;
    }

    public void update()
    {
        this.x += this.vx;
        this.y += this.vy;
    }

    public void draw(Graphics2D g)
    {
        g.setColor(this.c);
        g.fillOval((int)x, (int)y, (int)(r*2), (int)(r*2));
    }


    // Standard getters and setters
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public double getVX() {return this.vx;}
    public double getVY() {return this.vy;}
    public double getR() {return this.r;}
    public double getM() {return this.m;}
    public Color getC() {return this.c;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVX(double vx) {this.vx = vx;}
    public void setVY(double vy) {this.vy = vy;}
    public void setR(double r) {this.r = r;}
    public void setM(double m) {this.m = m;}
    public void setC(Color c) {this.c = c;}
}
