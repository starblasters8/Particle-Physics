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

    public Particle(double x, double y, double radius, double mass, double elasticity, Color c, double boundW, double boundH, double boundX, double boundY) 
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.diameter = radius * 2;
        this.mass = mass;
        this.c = c;
        this.elasticity = elasticity;
        this.boundW = boundW;
        this.boundH = boundH;
        this.boundX = boundX;
        this.boundY = boundY;
    }

    public void update() 
    {
        checkBoundCollision();

        // Calculate acceleration based on force
        double fx = 0;
        double fy = mass * 9.81; // Gravity
        ax = fx / mass;
        ay = fy / mass;

        // Update velocity based on acceleration
        vx += ax;
        vy += ay;

        // Apply damping to simulate air resistance
        vx *= (1 - damping);
        vy *= (1 - damping);

        // Update position based on velocity
        x += vx;
        y += vy;
    }

    public void checkBoundCollision() 
    {
        if (x + diameter + vx > boundW) 
        {
            x = boundW - diameter;
            vx = -vx * elasticity;
        } 
        else if (x + vx < boundX) 
        {
            x = boundX;
            vx = -vx * elasticity;
        }

        if (y + diameter + vy > boundH) 
        {
            y = boundH - diameter;
            vy = -vy * elasticity;
        } 
        else if (y + vy < boundY) 
        {
            y = boundY;
            vy = -vy * elasticity;
        }
    }

    public boolean checkParticleCollision(Particle p) 
    {
        // Predict the particles' positions after the next update
        double nextX = x + vx;
        double nextY = y + vy;
        double nextPX = p.x + p.vx;
        double nextPY = p.y + p.vy;

        // Calculate the distance between the predicted positions
        double dx = nextPX - nextX;
        double dy = nextPY - nextY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check if the predicted positions will collide
        return distance < radius + p.radius;
    }

    public void doParticleCollision(Particle p) {
        double dx = p.x - x;
        double dy = p.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double nx = dx / distance;
        double ny = dy / distance;
        double tx = -ny;
        double ty = nx;

        // Project velocities onto normal and tangent axes
        double v1n = nx * vx + ny * vy;
        double v1t = tx * vx + ty * vy;
        double v2n = nx * p.vx + ny * p.vy;
        double v2t = tx * p.vx + ty * p.vy;

        // Calculate new normal velocities based on conservation of momentum and energy
        double m1 = mass;
        double m2 = p.mass;
        double u1n = v1n * (m1 - m2) / (m1 + m2) + v2n * 2 * m2 / (m1 + m2);
        double u2n = v2n * (m2 - m1) / (m1 + m2) + v1n * 2 * m1 / (m1 + m2);

        // Calculate new tangent velocities
        double u1t = v1t;
        double u2t = v2t;

        // Convert normal and tangent velocities back to x and y velocities
        vx = u1n * nx + u1t * tx;
        vy = u1n * ny + u1t * ty;
        p.vx = u2n * nx + u2t * tx;
        p.vy = u2n * ny + u2t * ty;

        update();
        p.update();
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