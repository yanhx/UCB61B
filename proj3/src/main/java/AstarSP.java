import java.util.*;

public class AstarSP {
    private Map<Long, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private Map<Long, Long> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private PriorityQueue<Long> pq;    // priority queue of vertices
    private GraphDB G;
    private long s,t;

    public AstarSP(GraphDB G, long s, long t) {
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();;
        this.G = G;
        this.s = s;
        this.t = t;

        for (long v : G.vertices())
            distTo.put(v, Double.POSITIVE_INFINITY);
        distTo.put(s, 0.0);

        // relax vertices in order of distance from s
        pq = new PriorityQueue<Long>(G.V(), new Comparator<Long>() {
            @Override
            public int compare(Long w, Long v) {
                double wCost = distTo.get(w) + G.distance(w, t),
                        vCost = distTo.get(v) + G.distance(v, t);
                if (wCost < vCost) {
                    return -1;
                } else if (wCost > vCost) {
                    return 1;
                }
                return 0;
            }
        });
        pq.add(s);
        while (!pq.isEmpty()) {
            long v = pq.poll();
            for (long w : G.adjacent(v))
                relax(v, w);
        }

    }

    // relax edge e and update pq if changed
    private void relax(long v, long w) {
        double newDist = distTo.get(v) + G.distance(v, w);
        if (distTo.get(w) > newDist) {
            distTo.put(w, newDist);
            edgeTo.put(w, v);
            pq.remove(w);
            pq.add(w);
        }
    }

    public List<Long> pathTo(long v) {
        LinkedList<Long> path = new LinkedList<Long>();
        if (edgeTo.get(v) == null)
            return path;
        while (v != s) {
            path.addFirst(v);
            v = edgeTo.get(v);
        }
        path.addFirst(s);
        return path;
    }

}
