package A05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KdTreeST<Value> {
    private int size;
    private Node root;

    private class Node {
        private Point2D p; // the point
        private Value val; // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to th//
        // node
        private Node left; // the left/bottom subtree
        private Node right;

        public Node(Point2D point, Value value, RectHV rectangle) {
            p = point;
            val = value;
            rect = rectangle;

        }

        public List<Node> getChildren() {
            return new ArrayList<Node>(Arrays.asList(left, right));
        }

        public Node left() {
            return left;
        }

        public Node right() {
            return right;
        }
    }

    public KdTreeST() {
        root = null;
        size = 0;
    }

    /**
     * Is empty method
     *
     * @return
     */
    public boolean isEmpty() {
        return size() == 0;

    }

    /**
     * size method
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * put method
     *
     * @param p
     * @param val
     */
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("can't be null");
        root = put(root, p, val, true);
    }

    /**
     * Node private helper method recursivly places new point based on cases. If none of the cases are found or same x coordinate, places right
     *
     * @param node
     * @param p
     * @param val
     * @param horz
     * @return
     */
    private Node put(Node node, Point2D p, Value val, boolean horz) {
        // for root node
        if (node == null) {
            size++;
            return new Node(p, val,
                    new RectHV(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
        }


        double cmp = compareCoord(node, p, horz);
        if (cmp < 0)
            node.left = put(node.left, p, val, !horz);
        else if (cmp > 0)
            node.right = put(node.right, p, val, !horz);
        else if (node.p.equals(p))
            node.val = val;
        else
            node.right = put(node.right, p, val, !horz);

        return node;
    }

    /**
     * compares the prior node point and the new point
     *
     * @param node
     * @param p
     * @param horz
     * @return
     */
    private double compareCoord(KdTreeST<Value>.Node node, Point2D p, boolean horz) {
        if (horz) {

            return p.x() - node.p.x();

        } else {

            return p.y() - node.p.y();
        }

    }

    /**
     * get method
     *
     * @param p
     * @return
     */
    public Value get(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        return get(root, p, true);
    }

    /**
     * get helper method, recrusivly finds point
     *
     * @param node
     * @param p
     * @param horz
     * @return
     */
    private Value get(Node node, Point2D p, boolean horz) {

        if (node == null)
            return null;

        double cmp = compareCoord(node, p, horz);
        if (cmp < 0)
            return get(node.left, p, !horz);
        else if (cmp > 0)
            return get(node.right, p, !horz);
        else if (node.p.equals(p))
            return node.val;
        else
            return get(node.right, p, !horz);

    }

    /**
     * contains method
     *
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        return get(p) != null;
    }

    /**
     * returns range of RectHV in a Que
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.NullPointerException();
        Queue<Point2D> rangequeue = new Queue<Point2D>();
        range(rect, rangequeue, root);
        return rangequeue;

    }

    /**
     * recrusivly enques points if their are with in the range
     *
     * @param rect
     * @param rangequeue
     * @param node
     */
    private void range(RectHV rect, Queue<Point2D> rangequeue, KdTreeST<Value>.Node node) {
        if (node == null)
            return;
        if (!rect.intersects(node.rect))
            return;
        if (rect.contains(node.p))
            rangequeue.enqueue(node.p);
        range(rect, rangequeue, node.left);
        range(rect, rangequeue, node.right);

    }

    /**
     * iterable that returns all points using a queue
     *
     * @return
     */
    public Iterable<Point2D> points() {

        Queue<Point2D> points = new Queue<Point2D>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null)
                continue;
            points.enqueue(x.p);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return points;

    }


    public Point2D nearest(Point2D p) {
        if(p == null)
            throw new NullPointerException("Point value cannot be null");
        // recursively search for nearest
        return nearestNode(p, root, p.distanceSquaredTo(root.p)).p;
    }

    private Node nearestNode(Point2D p, Node parent, double currentDistance) {
        // for each child of root check distance
        double leftDistance = p.distanceSquaredTo(parent.left().p);
        double rightDistance = p.distanceSquaredTo(parent.right.p);

        if (leftDistance > currentDistance && rightDistance > currentDistance)
            return parent;

        if (leftDistance == currentDistance && rightDistance == currentDistance)
            return parent.left();

        if (leftDistance < currentDistance && leftDistance < rightDistance)
            return nearestNode(p, parent.left(), leftDistance);

        if(rightDistance < currentDistance)
            return nearestNode(p, parent.right(), rightDistance);

        return parent;
    }
}
