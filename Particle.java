import java.awt.Color;
import java.awt.Graphics2D;

public class Particle 
{
    private double x, y; // X and Y coordinates of top left corner
    private double vx=0, vy=0; // X and Y velocities
    private double radius, mass; // Radius and mass
    private double diameter; // Diameter
    private Color c; // Color

    public Particle(double x, double y, double radius, double mass, Color c)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.diameter = radius*2;
        this.mass = mass;
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
        g.fillOval((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(diameter), (int)Math.floor(diameter));
    }


    // Standard getters and setters
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public double getVX() {return this.vx;}
    public double getVY() {return this.vy;}
    public double getRadius() {return this.radius;}
    public double getDiameter() {return this.diameter;}
    public double getMass() {return this.mass;}
    public Color getC() {return this.c;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVX(double vx) {this.vx = vx;}
    public void setVY(double vy) {this.vy = vy;}
    public void setRadius(double radius) {this.radius = radius; this.diameter = radius*2;}
    public void setDiameter(double diameter) {this.diameter = diameter; this.radius = diameter/2;}
    public void setMass(double mass) {this.mass = mass;}
    public void setC(Color c) {this.c = c;}
}
