import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {

    private Map<Long, List<Edge>> adj;
    private Map<Long, Node> vertices;
    private Map<Long, Way> ways;
    private List<Edge> edgeCaching;

    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        adj = new HashMap<>();
        vertices = new HashMap<>();
        ways = new HashMap<>();
        edgeCaching = new LinkedList<>();

        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public int V() {
        return vertices.size();
    }


    public static class Node {
        long id;
        double lat, lon;
        String name;

        public Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Edge {
        long id, v, w;

        public Edge(long id, long v, long w) {
            this.v = v;
            this.w = w;
            this.id = id;
        }

        public long either() {
            return v;
        }
        public long other(long v) {
            if (v == this.v) return w;
            return this.v;
        }
    }

    public static class Way {
        long id;
        boolean isValid;
        String name = "";

        public Way(long id) {
            this.id = id;
        }

        public void setValid(boolean valid) {
            isValid = valid;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterator<Long> iter = vertices.keySet().iterator();
        while (iter.hasNext()) {
            long v = iter.next();
            if (degree(v) == 0) {
                iter.remove();
                adj.remove(v);
            }
        }
    }

    public long degree(long v) {
        return adj.get(v).size();
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return vertices.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        List<Long> ret = new LinkedList<>();
        for (Edge e : adj.get(v)) {
            ret.add(e.other(v));
        }
        return ret;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        if(vertices.size() == 0) {
            System.out.println("Nothing inside the Graph, cannot find the closest vertex.");
            return 0;
        }
        double min = Double.MAX_VALUE;
        long minId = 0;
        for (Node x : vertices.values()) {
            double dis = distance(x.lon, x.lat, lon, lat);
            if (dis < min) {
                min = dis;
                minId = x.id;
            }
        }
        return minId;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertices.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertices.get(v).lat;
    }

    void addNode(long id, double lat, double lon) {
        vertices.put(id, new Node(id, lat, lon));
        adj.put(id, new LinkedList<>());
    }

    void addEdge(long id, long v, long w) {
        Edge e = new Edge(id, v, w);
        adj.get(v).add(e);
        adj.get(w).add(e);
    }

    void addEdge(Edge e) {
        long v = e.either();
        adj.get(v).add(e);
        adj.get(e.other(v)).add(e);
    }

    void addCachedEdges() {
        for (Edge e : edgeCaching) {
            if (ways.get(e.id).isValid)
                addEdge(e);
        }
        edgeCaching = new LinkedList<>();
    }

    void cacheEdge(long id, long v, long w) {
        Edge e = new Edge(id, v, w);
        edgeCaching.add(e);
    }

    void addWay(long id) {
        ways.put(id, new Way(id));
    }

    void validateWay(long id, boolean valid) {
        ways.get(id).setValid(valid);
    }

    void setWayName(long id, String name) {
        ways.get(id).setName(name);
    }

    void setLocationName(long id, String name) {
        vertices.get(id).setName(name);
    }

    long getWayId(long v, long w) {
        for (Edge e : adj.get(v))
            if (e.other(v) == w)
                return e.id;
        return 0;
    }

    String getWayName(long id) {
        return ways.get(id).name;
    }
}
