import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ParticleGroup 
{
    protected double x, y; // X and Y coordinates of top left corner
    protected double w, h; // Width and height
    protected Particle[] particles; // Particles in the group
    protected Color c; // Color
    protected double radius, mass; // Radius and mass
    protected double diameter; // Diameter

    protected double elasticity; // Elasticity (1 = perfectly elastic, 0 = perfectly inelastic)
    protected double boundW, boundH; // Width and height of the screen/max bounding box
    protected double boundX, boundY; // X and Y coordinates of top left corner of bounding box

    public ParticleGroup(double x, double y, double w, double h, double radius, double mass, double elasticity, Color c, double boundX, double boundY, double boundW, double boundH)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.radius = radius;
        this.mass = mass;
        this.diameter = radius*2;
        this.c = c;
        this.elasticity = elasticity;
        this.boundX = boundX;
        this.boundY = boundY;
        this.boundW = boundW;
        this.boundH = boundH;

        generateParticles();
    }

    public void generateParticles() // Generates as many particles as possible in the given space (in a honeycomb pattern) with the given spacing and adds them to the array "particles"
    {
        double spacing = 1.2; // Increase spacing between particles
        ArrayList<Particle> particleList = new ArrayList<Particle>();

        double startX = x + radius;
        double startY = y + radius;

        boolean shiftRow = false;

        for (double yPos = startY; yPos + radius <= y + h; yPos += diameter * Math.sqrt(3) / 2 * spacing) 
        {
            double currentX = startX;
            if (shiftRow)
                currentX += diameter * spacing / 2;

            for (double xPos = currentX; xPos + radius <= x + w; xPos += diameter * spacing) 
            {
                Particle p = new Particle(xPos, yPos, radius, mass, elasticity, c, boundX, boundY, boundW, boundH);
                particleList.add(p);
            }

            shiftRow = !shiftRow;
        }

        particles = new Particle[particleList.size()];
        particles = particleList.toArray(particles);
    }

    public void updateAll()
    { 
        for(Particle p : particles)
            p.update();

        allParticleCollision();
    }

    public void allParticleCollision() 
    {
        Quadtree quadtree = new Quadtree(0, new Rectangle((int) boundX, (int) boundY, (int) boundW, (int) boundH));

        for (Particle p : particles) quadtree.insert(p);

        boolean collisionResolved;
        do {
            collisionResolved = false;
            for (int i = 0; i < particles.length; i++) 
            {
                List<Particle> returnObjects = new ArrayList<>();
                quadtree.retrieve(returnObjects, particles[i]);

                for (int j = 0; j < returnObjects.size(); j++) 
                {
                    Particle p1 = particles[i];
                    Particle p2 = returnObjects.get(j);

                    if (p1 != p2 && p1.isColliding(p2)) 
                    {
                        p1.resolveCollision(p2);
                        collisionResolved = true;
                    }
                }
            }
        } while (collisionResolved);
    }
    
    public void drawAll(Graphics2D g, boolean drawOutline, boolean drawBounds)
    {
        for(Particle p : particles)
            p.draw(g, drawOutline);

        if(drawBounds)
        {
            g.setColor(Color.RED);
            g.drawRect((int)boundX, (int)boundY, (int)(boundW-boundX), (int)(boundH-boundY));
        }
    }

    public void fitBoundingBox() // Fits the bounding box to the particles
    {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for(Particle p : particles)
        {
            if(p.getX() < minX) minX = p.getX();
            if(p.getY() < minY) minY = p.getY();
            if(p.getX()+p.getDiameter() > maxX) maxX = p.getX()+p.getDiameter();
            if(p.getY()+p.getDiameter() > maxY) maxY = p.getY()+p.getDiameter();
        }

        this.x = minX;
        this.y = minY;
        this.w = maxX - minX;
        this.h = maxY - minY;
    }

    public void drawBoundingBox(Graphics2D g, Color c)
    {
        fitBoundingBox();
        g.setColor(c);
        g.drawRect((int)x, (int)y, (int)w, (int)h);
    }

    public void randomizeVX(double ranBy)
    { 
        for(Particle p : particles)
            p.setVX(p.getVX() + ((Math.random() * ranBy) - (ranBy/2))); 
    }

    public void randomizeMass(double ranBy)
    { 
        for(Particle p : particles)
            p.setMass(p.getMass() + ((Math.random() * ranBy) - (ranBy/2))); 
    }

    public void randomizeRadius(double ranBy)
    { 
        for(Particle p : particles)
            p.setRadius(p.getRadius() + ((Math.random() * ranBy) - (ranBy/2))); 
    }

    public void randomizeElasticity(double ranBy)
    { 
        for(Particle p : particles)
            p.setElasticity(p.getElasticity() + ((Math.random() * ranBy) - (ranBy/2))); 
    }

    public int totalParticles() { return particles.length; }

    public double totalParticleMass() 
    { 
        double total = 0;
        for(Particle p : particles)
            total += p.getMass();
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
    public Particle[] getParticles() {return this.particles;}
    public double getElasticity() {return this.elasticity;}
    public double getBoundW() {return this.boundW;}
    public double getBoundH() {return this.boundH;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setW(double w) {this.w = w;}
    public void setH(double h) {this.h = h;}
    public void setRadius(double radius) {this.radius = radius; this.diameter = radius*2;}
    public void setDiameter(double diameter) {this.diameter = diameter; this.radius = diameter/2;}
    public void setMass(double mass) {this.mass = mass;}
    public void setC(Color c) {this.c = c;}
    public void setParticles(Particle[] particles) {this.particles = particles;}
    public void setBoundW(double boundW) {this.boundW = boundW;}    
    public void setBoundH(double boundH) {this.boundH = boundH;}
}
