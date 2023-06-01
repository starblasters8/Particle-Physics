import java.awt.Color;
import java.awt.Graphics2D;

public class Particle 
{
    protected float x, y; // X and Y coordinates of top left corner
    protected float vx = 0, vy = 0; // X and Y velocities
    protected float ax = 0, ay = 0; // X and Y accelerations
    protected float radius, mass; // Radius and mass
    protected float diameter; // Diameter
    protected Color c; // Color
    protected float elasticity = 1; // Elasticity (1 = perfectly elastic, 0 = perfectly inelastic)
    protected float boundW, boundH; // Width and height of the screen/max bounding box
    protected float boundX, boundY; // X and Y coordinates of top left corner of bounding box
    protected float damping = 0.01f; // Damping factor to simulate air resistance
    protected static final float MIN_VELOCITY = 0.01f; // Minimum velocity threshold

    public Particle(float x, float y, float radius, float mass, float elasticity, Color c, float boundX, float boundY, float boundW, float boundH) 
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.diameter = radius * 2;
        this.mass = mass;
        this.c = c;
        this.elasticity = elasticity;
        this.boundX = boundX;
        this.boundY = boundY;
        this.boundW = boundW;
        this.boundH = boundH;
    }

    public void update() 
    {
        boundCollision();

        vy += 9.81; // Gravity

        // Apply damping to simulate air resistance
        vx *= (1 - damping);
        vy *= (1 - damping);

        // Stop moving if velocity is below the threshold
        if (Math.abs(vx) < MIN_VELOCITY) vx = 0;
        if (Math.abs(vy) < MIN_VELOCITY) vy = 0;

        // Update position based on velocity
        x += vx;
        y += vy;
    }

    public boolean isColliding(Particle other) // Returns true if this particle is colliding with the other particle
    {
        float dx = other.getX() - this.x;
        float dy = other.getY() - this.y;
        float distance = (float)(Math.sqrt(dx * dx + dy * dy));
        float minDistance = this.radius + other.getRadius();

        return distance < minDistance;
    }


    public void resolveCollision(Particle other) // Resolves the collision between this particle and the other particle
    {
        // Calculate the distance between the two particles
        float dx = other.getX() - this.x;
        float dy = other.getY() - this.y;
        float distance = (float)(Math.sqrt(dx * dx + dy * dy));

        // Calculate the minimum distance required for the particles to not overlap
        float minDistance = this.radius + other.getRadius();

        // Calculate the normal vector between the two particles
        float nx = (other.getX() - this.x) / distance;
        float ny = (other.getY() - this.y) / distance;

        // Calculate the impulse required to separate the particles
        float p = 2 * (this.vx * nx + this.vy * ny - other.getVX() * nx - other.getVY() * ny) / (this.mass + other.getMass());

        // Calculate the amount of overlap between the particles
        float w = minDistance - distance + 1;

        // Move the particles apart by an amount proportional to their masses
        this.x -= (w * this.mass / (this.mass + other.getMass())) * nx;
        this.y -= (w * this.mass / (this.mass + other.getMass())) * ny;
        other.setX(other.getX() + (w * other.getMass() / (this.mass + other.getMass())) * nx);
        other.setY(other.getY() + (w * other.getMass() / (this.mass + other.getMass())) * ny);

        // Apply the impulse to the particles
        float avgElasticity = (this.elasticity + other.getElasticity()) / 2;
        this.vx -= p * this.mass * nx * avgElasticity;
        this.vy -= p * this.mass * ny * avgElasticity;
        other.setVX(other.getVX() + p * other.getMass() * nx * avgElasticity);
        other.setVY(other.getVY() + p * other.getMass() * ny * avgElasticity);

        // Ensure the particles stay within the bounds of the simulation
        this.boundCollision();
        other.boundCollision();
    }

    public void boundCollision() // Bounce off walls and check for collision with walls
    {
        // Check for collision with left and right walls
        if (x <= boundX || x + diameter >= boundW) 
        {
            vx = -vx * elasticity; // Reverse the x velocity and apply elasticity
            if (x <= boundX) x = boundX; // Move the particle inside the bounds if it's outside
            if (x + diameter >= boundW) x = boundW - diameter; // Move the particle inside the bounds if it's outside
        }

        // Check for collision with top and bottom walls
        if (y <= boundY || y + diameter >= boundH) 
        {
            vy = -vy * elasticity; // Reverse the y velocity and apply elasticity
            if (y <= boundY) y = boundY; // Move the particle inside the bounds if it's outside
            if (y + diameter >= boundH) y = boundH - diameter; // Move the particle inside the bounds if it's outside
        }
    }

    public void draw(Graphics2D g, boolean drawOutline) 
    {
        g.setColor(this.c);
        g.fillOval((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(diameter), (int) Math.floor(diameter));
        if (drawOutline) 
        {
            g.setColor(Color.WHITE);
            g.drawOval((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(diameter), (int) Math.floor(diameter));
        }
    }

    // Standard getters and setters
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getVX() {
        return this.vx;
    }

    public float getVY() {
        return this.vy;
    }

    public float getRadius() {
        return this.radius;
    }

    public float getDiameter() {
        return this.diameter;
    }

    public float getMass() {
        return this.mass;
    }

    public Color getC() {
        return this.c;
    }

    public float getElasticity() {
        return this.elasticity;
    }

    public float getBoundW() {
        return this.boundW;
    }

    public float getBoundH() {
        return this.boundH;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setVX(float vx) {
        this.vx = vx;
    }

    public void setVY(float vy) {
        this.vy = vy;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.diameter = radius * 2;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
        this.radius = diameter / 2;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public void setElasticity(float elasticity) {
        this.elasticity = elasticity;
    }

    public void setBoundW(float boundW) {
        this.boundW = boundW;
    }

    public void setBoundH(float boundH) {
        this.boundH = boundH;
    }
}