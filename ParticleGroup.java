import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ParticleGroup 
{
    private float x, y; // X and Y coordinates of top left corner
    private float w, h; // Width and height
    private Particle[] particles; // Particles in the group
    private Color c; // Color
    private float radius, mass; // Radius and mass
    private float diameter; // Diameter

    private float elasticity; // Elasticity (1 = perfectly elastic, 0 = perfectly inelastic)
    private float boundW, boundH; // Width and height of the screen/max bounding box
    private float boundX, boundY; // X and Y coordinates of top left corner of bounding box

    public ParticleGroup(float x, float y, float w, float h, float radius, float mass, float elasticity, Color c, float boundX, float boundY, float boundW, float boundH)
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
        float spacing = 1.2f; // Increase spacing between particles
        ArrayList<Particle> particleList = new ArrayList<Particle>();

        float startX = x + radius;
        float startY = y + radius;

        boolean shiftRow = false;

        for (float yPos = startY; yPos + radius <= y + h; yPos += diameter * Math.sqrt(3) / 2 * spacing) 
        {
            float currentX = startX;
            if (shiftRow)
                currentX += diameter * spacing / 2;

            for (float xPos = currentX; xPos + radius <= x + w; xPos += diameter * spacing) 
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
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;

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

    public void randomizeVX(float ranBy)
    { 
        for(Particle p : particles)
            p.setVX((float)(p.getVX() + ((Math.random() * ranBy) - (ranBy/2)))); 
    }

    public void randomizeMass(float ranBy)
    { 
        for(Particle p : particles)
            p.setMass((float)(p.getMass() + ((Math.random() * ranBy) - (ranBy/2)))); 
    }

    public void randomizeRadius(float ranBy)
    { 
        for(Particle p : particles)
            p.setRadius((float)(p.getRadius() + ((Math.random() * ranBy) - (ranBy/2)))); 
    }

    public void randomizeElasticity(float ranBy)
    { 
        for(Particle p : particles)
            p.setElasticity((float)(p.getElasticity() + ((Math.random() * ranBy) - (ranBy/2)))); 
    }

    public int totalParticles() { return particles.length; }

    public float totalParticleMass() 
    { 
        float total = 0;
        for(Particle p : particles)
            total += p.getMass();
        return total;
    }

    public float averageParticleMass() { return totalParticleMass() / totalParticles(); }

    // Standard getters and setters
    public float getX() {return this.x;}
    public float getY() {return this.y;}
    public float getW() {return this.w;}
    public float getH() {return this.h;}
    public float getRadius() {return this.radius;}
    public float getDiameter() {return this.diameter;}
    public float getMass() {return this.mass;}
    public Color getC() {return this.c;}
    public Particle[] getParticles() {return this.particles;}
    public float getElasticity() {return this.elasticity;}
    public float getBoundW() {return this.boundW;}
    public float getBoundH() {return this.boundH;}

    public void setX(float x) {this.x = x;}
    public void setY(float y) {this.y = y;}
    public void setW(float w) {this.w = w;}
    public void setH(float h) {this.h = h;}
    public void setRadius(float radius) {this.radius = radius; this.diameter = radius*2;}
    public void setDiameter(float diameter) {this.diameter = diameter; this.radius = diameter/2;}
    public void setMass(float mass) {this.mass = mass;}
    public void setC(Color c) {this.c = c;}
    public void setParticles(Particle[] particles) {this.particles = particles;}
    public void setBoundW(float boundW) {this.boundW = boundW;}    
    public void setBoundH(float boundH) {this.boundH = boundH;}
}
