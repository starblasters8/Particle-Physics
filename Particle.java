import java.awt.Color;
import java.awt.Graphics2D;

public class Particle 
{
    protected double x, y; // X and Y coordinates of top left corner
    protected double vx=0, vy=0; // X and Y velocities
    protected double radius, mass; // Radius and mass
    protected double diameter; // Diameter
    protected Color c; // Color
    protected double elasticity = 1; // Elasticity (1 = perfectly elastic, 0 = perfectly inelastic)
    protected double boundW, boundH; // Width and height of the screen/max bounding box
    protected double boundX, boundY; // X and Y coordinates of top left corner of bounding box
    
    public Particle(double x, double y, double radius, double mass, double elasticity, Color c, double boundW, double boundH, double boundX, double boundY)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.diameter = radius*2;
        this.mass = mass;
        this.c = c;
        this.elasticity = elasticity;
        this.boundW = boundW;
        this.boundH = boundH;
    }

    public void update()
    {
        checkBoundCollision();
        this.x += this.vx;
        this.y += this.vy;
    }

    public void checkBoundCollision()
    {
        if(this.x+diameter+this.vx > boundW || this.x+this.vx < boundX)
            this.vx = -this.vx*elasticity;

        if(this.y+diameter+this.vy > boundH || this.y+this.vy < boundY)
            this.vy = -this.vy*elasticity;
    }

    public boolean checkParticleCollision(Particle p)
    {
        if(this.x + this.diameter > p.x && this.x < p.x + p.diameter && this.y + this.diameter > p.y && this.y < p.y + p.diameter)
            return true;
        else
            return false;
    }

    public void doParticleCollision(Particle p) // Collide the two particles based on their velocities and mass
    {
        
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
    public double getElasticity() {return this.elasticity;}
    public double getBoundW() {return this.boundW;}
    public double getBoundH() {return this.boundH;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVX(double vx) {this.vx = vx;}
    public void setVY(double vy) {this.vy = vy;}
    public void setRadius(double radius) {this.radius = radius; this.diameter = radius*2;}
    public void setDiameter(double diameter) {this.diameter = diameter; this.radius = diameter/2;}
    public void setMass(double mass) {this.mass = mass;}
    public void setC(Color c) {this.c = c;}
    public void setElasticity(double elasticity) {this.elasticity = elasticity;}
    public void setBoundW(double boundW) {this.boundW = boundW;}
    public void setBoundH(double boundH) {this.boundH = boundH;}
}
