import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Quadtree
{
    private final int MAX_OBJECTS = 5;
    private final int MAX_LEVELS = 5;

    private int level;
    private List<Particle> objects;
    private Rectangle bounds;
    private Quadtree[] nodes;

    public Quadtree(int level, Rectangle bounds) 
    {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.nodes = new Quadtree[4];
    }

    public void clear() 
    {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) 
        {
            if (nodes[i] != null) 
            {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() 
    {
        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Particle p) 
    {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        boolean topQuadrant = (p.getY() < horizontalMidpoint && p.getY() + p.getDiameter() < horizontalMidpoint);
        boolean bottomQuadrant = (p.getY() > horizontalMidpoint);

        if (p.getX() < verticalMidpoint && p.getX() + p.getDiameter() < verticalMidpoint) 
        {
            if (topQuadrant)
                index = 1;
            else if (bottomQuadrant)
                index = 2;
        } 
        else if (p.getX() > verticalMidpoint) 
        {
            if (topQuadrant)
                index = 0;
            else if (bottomQuadrant)
                index = 3;
        }

        return index;
    }

    public void insert(Particle p) 
    {
        if (nodes[0] != null) 
        {
            int index = getIndex(p);

            if (index != -1) {
                nodes[index].insert(p);
                return;
            }
        }

        objects.add(p);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) 
        {
            if (nodes[0] == null)
                split();

            int i = 0;
            while (i < objects.size()) 
            {
                int index = getIndex(objects.get(i));
                if (index != -1)
                    nodes[index].insert(objects.remove(i));
                else
                    i++;
            }
        }
    }

    public List<Particle> retrieve(List<Particle> returnObjects, Particle p) 
    {
        int index = getIndex(p);
        if (index != -1 && nodes[0] != null) 
            nodes[index].retrieve(returnObjects, p);

        returnObjects.addAll(objects);

        return returnObjects;
    }
}