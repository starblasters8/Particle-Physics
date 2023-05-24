import java.awt.Color;
import java.awt.Graphics2D;

public class ParticleGroup 
{
    protected double x, y; // X and Y coordinates of top left corner
    protected double w, h; // Width and height
    protected Particle[][] particles; // Particles in the group
    protected Color c; // Color
    protected double radius, mass; // Radius and mass
    protected double diameter; // Diameter
    protected final double GRAVITY = 9.81; // Gravitational constant

    public ParticleGroup(double x, double y, double w, double h, double radius, double mass, Color c)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.radius = radius;
        this.mass = mass;
        this.diameter = radius*2;
        this.c = c;

        particles = new Particle[(int)Math.floor(w/diameter)][(int)Math.floor(h/diameter)];
        generateParticles();
    }

    public void generateParticles()
    {
        for(int i = 0; i < particles.length; i++)
            for(int j = 0; j < particles[i].length; j++)
                particles[i][j] = new Particle(x + (i*diameter), y + (j*diameter), radius, mass, c);
    }

    public void updateAll()
    { 
        for(Particle[] p : particles)
        {
            for(Particle p2 : p)
            {
                p2.setVY(p2.getVY() + (GRAVITY * p2.getMass())); // Apply gravity
                p2.update();
            }
        }

        fitBoundingBox();
    }
    
    public void drawAll(Graphics2D g)
    {
        for(Particle[] p : particles)
            for(Particle p2 : p)
                p2.draw(g);
    }

    public void fitBoundingBox() // Fits the bounding box to the particles
    {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for(Particle[] p : particles)
        {
            for(Particle p2 : p)
            {
                if(p2.getX() < minX) minX = p2.getX();
                if(p2.getY() < minY) minY = p2.getY();
                if(p2.getX()+p2.getDiameter() > maxX) maxX = p2.getX();
                if(p2.getY()+p2.getDiameter() > maxY) maxY = p2.getY();
            }
        }

        this.x = minX;
        this.y = minY;
        this.w = maxX - minX;
        this.h = maxY - minY;
    }

    public void drawBoundingBox(Graphics2D g, Color c)
    {
        g.setColor(c);
        g.drawRect((int)x, (int)y, (int)w, (int)h);
    }

    public void randomizeMass(double ranBy)
    { 
        for(Particle[] p : particles)
            for(Particle p2 : p)
                p2.setMass(p2.getMass() + ((Math.random() * ranBy) - (ranBy/2))); 
    }

    public void randomizeRadius(double ranBy)
    { 
        for(Particle[] p : particles)
            for(Particle p2 : p)
                p2.setRadius(p2.getRadius() + ((Math.random() * ranBy) - (ranBy/2))); 
    }

    public int totalParticles() { return particles.length * particles[0].length; }

    public double totalParticleMass() 
    { 
        double total = 0;
        for(Particle[] p : particles)
            for(Particle p2 : p)
                total += p2.getMass();
        return total;
    }

    public double averageParticleMass() { return totalParticleMass() / totalParticles(); }

    // Standard getters and setters
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public double getW() {return this.w;}
    public double getH() {return this.h;}
    public double getRadius() {return this.radius;}
    public double getDiameter() {return this.diameter;}
    public double getMass() {return this.mass;}
    public Color getC() {return this.c;}
    public double getGravity() { return this.GRAVITY; }
    public Particle[][] getParticles() {return this.particles;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setW(double w) {this.w = w;}
    public void setH(double h) {this.h = h;}
    public void setRadius(double radius) {this.radius = radius; this.diameter = radius*2;}
    public void setDiameter(double diameter) {this.diameter = diameter; this.radius = diameter/2;}
    public void setMass(double mass) {this.mass = mass;}
    public void setC(Color c) {this.c = c;}
    public void setParticles(Particle[][] particles) {this.particles = particles;}
}
