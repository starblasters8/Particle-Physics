import java.awt.Color;
import java.awt.Graphics2D;

public class ParticleGroup 
{
    private double x, y; // X and Y coordinates of top left corner
    private double w, h; // Width and height
    private Particle[][] particles; // Particles in the group
    private Color c; // Color
    private double radius, mass; // Radius and mass
    private final double GRAVITY = 9.81; // Gravitational constant

    public ParticleGroup(double x, double y, double w, double h, double radius, double mass, Color c)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.radius = radius;
        this.mass = mass;
        this.c = c;

        particles = new Particle[(int)(w/radius)][(int)(h/radius)];
        generateParticles();
    }

    private void generateParticles()
    {
        for(int i = 0; i < particles.length; i++)
            for(int j = 0; j < particles[i].length; j++)
                particles[i][j] = new Particle(x + (i * radius*2), y + (j * radius*2), radius, mass, c);
    }

    public void update()
    { 
        for(Particle[] p : particles)
            for(Particle p2 : p)
                p2.update();
    }
    
    public void drawAll(Graphics2D g)
    {
        for(Particle[] p : particles)
            for(Particle p2 : p)
                p2.draw(g);
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
    public double getMass() {return this.mass;}
    public Color getC() {return this.c;}
    public double getGravity() { return this.GRAVITY; }
    public Particle[][] getParticles() {return this.particles;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setW(double w) {this.w = w;}
    public void setH(double h) {this.h = h;}
    public void setRadius(double radius) {this.radius = radius;}
    public void setMass(double mass) {this.mass = mass;}
    public void setC(Color c) {this.c = c;}
    public void setParticles(Particle[][] particles) {this.particles = particles;}
}
