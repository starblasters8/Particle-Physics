import java.util.ArrayList;
import java.awt.Color;

public class ParticleGroup 
{
    private double x, y; // X and Y coordinates of top left corner
    private double w, h; // Width and height
    private ArrayList<Particle> particles; // Particles in the group
    private Color c; // Color
    private double r, m; // Radius and mass

    public ParticleGroup(double x, double y, double w, double h, double r, double m, Color c)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.m = m;
        this.c = c;

        this.particles = new ArrayList<Particle>();
    }

    public void update()
    {
        for(Particle p : particles) p.update();
    }

    public void randomizeMass(double ranBy)
    {for(Particle p : particles)    p.setM(p.getM() + ((Math.random() * ranBy) - (ranBy/2)));}

    // Standard getters and setters
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public double getW() {return this.w;}
    public double getH() {return this.h;}
    public double getR() {return this.r;}
    public double getM() {return this.m;}
    public Color getC() {return this.c;}
    public ArrayList<Particle> getParticles() {return this.particles;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setW(double w) {this.w = w;}
    public void setH(double h) {this.h = h;}
    public void setR(double r) {this.r = r;}
    public void setM(double m) {this.m = m;}
    public void setC(Color c) {this.c = c;}
    public void setParticles(ArrayList<Particle> particles) {this.particles = particles;}
}
