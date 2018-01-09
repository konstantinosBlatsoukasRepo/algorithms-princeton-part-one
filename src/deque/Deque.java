package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by kon on 14/10/2017.
 */
public class Deque<Item> implements Iterable<Item> {

    private Node firstNode;
    private Node lastNode;
    private Node firstSentinelNode;
    private Node lastSentinelNode;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    public Deque() {
        firstNode = new Node();
        lastNode = new Node();
        firstSentinelNode = new Node();
        lastSentinelNode = new Node();
        firstSentinelNode.next = lastSentinelNode;
        lastSentinelNode.previous = firstSentinelNode;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if(item == null) {
            throw new IllegalArgumentException("Item must contains a value");
        }
        Node freshNode = new Node();
        freshNode.item = item;
        freshNode.previous = firstSentinelNode;
        if(isEmpty()) {
            lastSentinelNode.previous = freshNode;
            freshNode.next = lastSentinelNode;
            lastNode = freshNode;
        } else {
            Node oldFirst = firstNode;
            freshNode.next = oldFirst;
            oldFirst.previous = freshNode;
        }
        firstSentinelNode.next = freshNode;
        firstNode = freshNode;
        size++;
    }

    public void addLast(Item item) {
        if(item == null) {
            throw new IllegalArgumentException("Item must contains a value");
        }
        Node freshNode = new Node();
        freshNode.item = item;
        freshNode.next = lastSentinelNode;
        if(isEmpty()) {
            lastSentinelNode.previous = freshNode;
            freshNode.next = lastSentinelNode;
            firstNode = freshNode;
        } else {
            Node oldLast = lastNode;
            freshNode.previous = oldLast;
            oldLast.next = freshNode;
        }
        lastSentinelNode.previous = freshNode;
        lastNode = freshNode;
        size++;
    }

    public Item removeFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty");
        }
        Item removedItem = firstNode.item;
        if(size == 1) {
            firstNode = null;
            lastNode = null;
            firstSentinelNode.next = lastSentinelNode;
        } else {
            firstNode.next.previous = firstSentinelNode;
            firstSentinelNode.next = firstNode.next;
            firstNode = firstNode.next;
        }
        lastSentinelNode.previous = firstSentinelNode;
        size--;
        return removedItem;
    }

    public Item removeLast() {
        if(isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty");
        }
        Item removedItem = lastNode.item;
        if(size == 1) {
            firstNode = null;
            lastNode = null;
            lastSentinelNode.next = firstSentinelNode;
        } else {
            lastNode.previous.next = lastSentinelNode;
            lastSentinelNode.previous = lastNode.previous;
            lastNode = lastNode.previous;
        }
        lastSentinelNode.previous = lastSentinelNode;
        size--;
        return removedItem;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = firstNode;

        public boolean hasNext() {
            return current != null && current.item != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> strings = new Deque<>();
        strings.addFirst("a");
        strings.addFirst("a");
        strings.addFirst("a");
        strings.addFirst("a");
        for(String string:strings) {
            System.out.println(string);
        }
    }
}