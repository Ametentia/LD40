package com.pixeldot.ld40.Util;

import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;


class Node implements Comparable<Node>{
    private Vector2 location;
    private float weight;

    public Vector2 getLocation() {return location; }
    public float getWeight() {return weight; }

    public Node(int x, int y, float weight) {
        location = new Vector2(x, y);
        this.weight = weight;
    }
    public Node(Vector2 start, float weight) {
        this((int)start.x, (int)start.y, weight);
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public int compareTo(Node T) {
        if(T.weight < getWeight())
            return 1;
        return -1;
    }
}

/**
 * @author Matthew Threlfall
 */
public class PathFinder {
    private float manhattan(Vector2 start, Vector2 end) {
        return (end.x-start.x) + (end.y - start.y);
    }

    public Vector2[] aStar(boolean[][] roads, Vector2 start, Vector2 end) {
        PriorityQueue<Node> nextNode = new PriorityQueue<Node>();
        Node[][] nodes = new Node[roads.length][roads[0].length];
        for(int i = 0; i < roads.length; i++) {
            for(int j = 0; j < roads[0].length; j ++) {
                nodes[i][j] = new Node(i, j, Float.MAX_VALUE);
            }
        }
        nextNode.add(new Node(start,0));
        boolean finished = false;
        while(nextNode.peek() != null && !finished) {
            Node currentNode = nextNode.poll();
            //update the weights for the current location
            for(int i = 0; i < 9; i ++) {
                int j = i/3;
                if(i%3 == (int)end.x && j == (int)end.y) {
                    finished = true;
                }
                if(i%3!=1 && j!=1) {
                    int x,y;
                    x = (int)currentNode.getLocation().x;
                    y = (int)currentNode.getLocation().y;
                    float distance = currentNode.getWeight();
                    distance += 1 + manhattan(new Vector2(x + i%3, y+j), end);
                    if(distance < nodes[i%3][j].getWeight()) {
                         nextNode.add(new Node(x+i%3, y+j, distance));
                         nodes[i%3][j].setWeight(distance);
                    }
                }
            }
        }

        return new Vector2[1];
    }
}


