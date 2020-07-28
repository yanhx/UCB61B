package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<Node> pq;
    private int moves;
    private Node last;
    //public int nodeCnt;

    private static class Node implements Comparable<Node> {
        public WorldState worldState;
        public int movesDone, priority;
        public Node prev;

        public Node(WorldState worldState, int movesDone, Node prev) {
            this.worldState = worldState;
            this.movesDone = movesDone;
            this.prev = prev;
        }

        @Override
        public int compareTo(Node node) {
            return movesDone + worldState.estimatedDistanceToGoal() - node.movesDone - node.worldState.estimatedDistanceToGoal();
        }
    }

    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        //nodeCnt++;

        while (!pq.isEmpty()) {
            Node x = pq.delMin();
            if (x.worldState.isGoal()) {
                moves = x.movesDone;
                last = x;
                break;
            }
            for (WorldState worldState : x.worldState.neighbors()) {
                if (x.prev != null && worldState.equals(x.prev.worldState)) continue;
                pq.insert(new Node(worldState, x.movesDone + 1, x));
                //nodeCnt++;
            }
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        Stack<WorldState> s = new Stack<>();
        Node x = last;
        while (x != null) {
            s.push(x.worldState);
            x = x.prev;
        }
        return s;
    }
}