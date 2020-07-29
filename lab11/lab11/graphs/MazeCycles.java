package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound = false;
    private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    private int dfs(int v) {
        marked[v] = true;
        announce();
        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                //distTo[w] = distTo[v] + 1;
                announce();
                int tmp = dfs(w);
                if (tmp == -1) {
                    edgeTo[w] = Integer.MAX_VALUE;
                    //distTo[w] = Integer.MAX_VALUE;
                    announce();
                }
                if (cycleFound) {
                    if (tmp == w) {
                        return -1;
                    } else {
                        return tmp;
                    }
                }
            } else {
                if (edgeTo[v] != w) {
                    cycleFound = true;
                    edgeTo[w] = v;
                    announce();
                    return w;
                }
            }
        }
        return -1;
    }

    @Override
    public void solve() {
        dfs(0);
    }

    // Helper methods go here
}

