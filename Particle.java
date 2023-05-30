import java.awt.Color;
import java.awt.Graphics2D;

public class Particle 
{
    protected double x, y; // X and Y coordinates of top left corner
    protected double vx = 0, vy = 0; // X and Y velocities
    protected double ax = 0, ay = 0; // X and Y accelerations
    protected double radius, mass; // Radius and mass
    protected double diameter; // Diameter
    protected Color c; // Color
    protected double elasticity = 1; // Elasticity (1 = perfectly elastic, 0 = perfectly inelastic)
    protected double boundW, boundH; // Width and height of the screen/max bounding box
    protected double boundX, boundY; // X and Y coordinates of top left corner of bounding box
    protected double damping = 0.01; // Damping factor to simulate air resistance
    protected double minVelocity = 0.15; // Minimum velocity threshold

    public Particle(double x, double y, double radius, double mass, double elasticity, Color c, double boundX, double boundY, double boundW, double boundH) 
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

        // Calculate acceleration based on force
        double fy = mass * 9.81; // Gravity
        ay = fy / mass;

        // Update velocity based on acceleration
        vy += ay;

        // Apply damping to simulate air resistance
        vx *= (1 - damping);
        vy *= (1 - damping);

        // Stop moving if velocity is below the threshold
        if (Math.abs(vx) < minVelocity) vx = 0;
        if (Math.abs(vy) < minVelocity) vy = 0;

        // Update position based on velocity
        x += vx;
        y += vy;
    }

    public boolean isColliding(Particle other) // Returns true if this particle is colliding with the other particle
    {
        double dx = other.getX() - this.x;
        double dy = other.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDistance = this.radius + other.getRadius();

        return distance < minDistance;
    }

    public void resolveCollision(Particle other) // Resolves the collision between this particle and the other particle
    {
        double dx = other.getX() - this.x;
        double dy = other.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDistance = this.radius + other.getRadius();

        double nx = (other.getX() - this.x) / distance;
        double ny = (other.getY() - this.y) / distance;
        double p = 2 * (this.vx * nx + this.vy * ny - other.getVX() * nx - other.getVY() * ny) / (this.mass + other.getMass());
        double w = minDistance - distance + 1;

        this.x -= (w * this.mass / (this.mass + other.getMass())) * nx;
        this.y -= (w * this.mass / (this.mass + other.getMass())) * ny;
        other.setX(other.getX() + (w * other.getMass() / (this.mass + other.getMass())) * nx);
        other.setY(other.getY() + (w * other.getMass() / (this.mass + other.getMass())) * ny);

        double avgElasticity = (this.elasticity + other.getElasticity()) / 2;

        this.vx -= p * this.mass * nx * avgElasticity;
        this.vy -= p * this.mass * ny * avgElasticity;
        other.setVX(other.getVX() + p * other.getMass() * nx * avgElasticity);
        other.setVY(other.getVY() + p * other.getMass() * ny * avgElasticity);

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
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getVX() {
        return this.vx;
    }

    public double getVY() {
        return this.vy;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getDiameter() {
        return this.diameter;
    }

    public double getMass() {
        return this.mass;
    }

    public Color getC() {
        return this.c;
    }

    public double getElasticity() {
        return this.elasticity;
    }

    public double getBoundW() {
        return this.boundW;
    }

    public double getBoundH() {
        return this.boundH;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVX(double vx) {
        this.vx = vx;
    }

    public void setVY(double vy) {
        this.vy = vy;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.diameter = radius * 2;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
        this.radius = diameter / 2;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public void setElasticity(double elasticity) {
        this.elasticity = elasticity;
    }

    public void setBoundW(double boundW) {
        this.boundW = boundW;
    }

    public void setBoundH(double boundH) {
        this.boundH = boundH;
    }
}