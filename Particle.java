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
        double fy = mass * 9.81; // Gravity
        ay = fy / mass;

        // Update velocity based on acceleration
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

    public void particleCollision(Particle p) 
    {
        double dx = p.getX() - this.x;
        double dy = p.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.radius + p.getRadius()) 
        {
            // Calculate new velocities
            double angle = Math.atan2(dy, dx);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            // Rotate this particle's velocity
            double vx1 = this.vx * cos + this.vy * sin;
            double vy1 = this.vy * cos - this.vx * sin;

            // Rotate the other particle's velocity
            double vx2 = p.getVX() * cos + p.getVY() * sin;
            double vy2 = p.getVY() * cos - p.getVX() * sin;

            // Calculate new velocities based on elastic collision formula
            double v1f = ((this.mass - p.getMass()) * vx1 + 2 * p.getMass() * vx2) / (this.mass + p.getMass());
            double v2f = ((p.getMass() - this.mass) * vx2 + 2 * this.mass * vx1) / (this.mass + p.getMass());

            // Rotate the velocities back
            this.vx = v1f * cos - vy1 * sin;
            this.vy = vy1 * cos + v1f * sin;
            p.setVX(v2f * cos - vy2 * sin);
            p.setVY(vy2 * cos + v2f * sin);

            // Move the particles so they are no longer colliding
            double overlap = 0.5 * (this.radius + p.getRadius() - distance + 1);
            this.x -= overlap * cos;
            this.y -= overlap * sin;
            p.setX(p.getX() + overlap * cos);
            p.setY(p.getY() + overlap * sin);
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