import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Quadtree 
{
    private static final int MAX_OBJECTS = 10;
    private static final int MAX_LEVELS = 25;

    private int level;
    private List<Particle> objects;
    private Rectangle bounds;
    private Quadtree[] nodes;

    public Quadtree(int level, Rectangle bounds) 
    {
        this.level = level;
        this.objects = new ArrayList<>(MAX_OBJECTS);
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
        int subWidth = bounds.width / 2;
        int subHeight = bounds.height / 2;
        int x = bounds.x;
        int y = bounds.y;

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Particle p) 
    {
        double verticalMidpoint = bounds.x + bounds.width / 2.0;
        double horizontalMidpoint = bounds.y + bounds.height / 2.0;

        boolean topQuadrant = p.getY() < horizontalMidpoint && p.getY() + p.getDiameter() < horizontalMidpoint;
        boolean bottomQuadrant = p.getY() > horizontalMidpoint;

        if (p.getX() < verticalMidpoint && p.getX() + p.getDiameter() < verticalMidpoint) 
            return topQuadrant ? 1 : bottomQuadrant ? 2 : -1;
        else if (p.getX() > verticalMidpoint) 
            return topQuadrant ? 0 : bottomQuadrant ? 3 : -1;

        return -1;
    }

    public void insert(Particle p) 
    {
        if (nodes[0] != null) 
        {
            int index = getIndex(p);

            if (index != -1) 
            {
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
