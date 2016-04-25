package A05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;

public class PointST<Value> {

    private RedBlackBST<Point2D, Value> tree;

    public PointST() {
        tree = new RedBlackBST<Point2D, Value>();
        // construct an empty symbol table of points
    }

    public static void main(String[] args) {

    }

    public boolean isEmpty() {
        return tree.isEmpty();
        // is the symbol table empty?
    }

    public int size() {
        return tree.size();
        // number of points
    }

    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new java.lang.NullPointerException();

        tree.put(p, val);
    }

    public Value get(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        return tree.get(p);
        // value associated with point p
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        return tree.contains(p);
        // does the symbol table contain point p?
    }

    public Iterable<Point2D> points() {
        return tree.keys();
        // all points in the symbol table
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.NullPointerException();
        Queue<Point2D> queue = new Queue<>();
        for (Point2D el : tree.keys())
            if (rect.contains(el))
                queue.enqueue(el);
        return queue;
        // all points that are inside the rectangle
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        Point2D goal = null;
        double distancegoal = 0;
        for (Point2D point : tree.keys()) {
            if (goal == null) {
                goal = point;
                distancegoal = point.distanceSquaredTo(p);
            } else if (point.distanceSquaredTo(p) < distancegoal) {
                goal = point;
                distancegoal = point.distanceSquaredTo(p);
            }
        }
        return goal;

        // a nearest neighbor to point p; null if the symbol table is empty
    }

}
