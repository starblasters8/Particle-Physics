import java.util.ArrayList;

public class Quadtree
{
    private double x, y, w, h; // X and Y coordinates of top left corner, width and height of the boundary
    private int level = 1; // Level of this quadtree

    private int maxParticles = 4; // Maximum number of particles in a quadtree before it splits
    private int maxLevel = 5; // Maximum number of levels in a quadtree
    
    public Quadtree(double x, double y, double w, double h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public ArrayList<Particle[]> doQuadTree(Particle[] particles)
    {
        ArrayList<Particle[]> particleGroups = new ArrayList<Particle[]>();
        if(level <= maxLevel && particles.length > 4)
        {
            
        }

        else
            particleGroups.add(particles);
            
        return particleGroups;
    }



    public int getMaxLevel(){return maxLevel;}
    public void setMaxLevel(int maxLevel){this.maxLevel = maxLevel;}
    public int getMaxParticles(){return maxParticles;}
    public void setMaxParticles(int maxParticles){this.maxParticles = maxParticles;}
}