package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kon on 14/10/2017.
 */
public class DequeTest {

    @Test
    public void testAddFirst() {
        Deque<String> deque = new Deque<>();
        String aStringItem = "first";
        String bStringItem = "second";
        String cStringItem = "third";
        deque.addFirst(aStringItem);
        deque.addFirst(bStringItem);
        deque.addFirst(cStringItem);
        assertEquals(3, deque.size());
    }

    @Test
    public void testAddLast() {
        Deque<String> deque = new Deque<>();
        String aStringItem = "first";
        String bStringItem = "second";
        String cStringItem = "third";
        deque.addLast(aStringItem);
        deque.addLast(bStringItem);
        deque.addLast(cStringItem);
        assertEquals(3, deque.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCheckAtAddition() {
        Deque<String> deque = new Deque<>();
        deque.addLast(null);
    }

    @Test
    public void testIsEmpty() {
        Deque<String> emptyDeque = new Deque<>();
        Deque<String> deque = new Deque<>();
        String aStrngItem = "Kostas";
        deque.addLast(aStrngItem);
        assertTrue(emptyDeque.isEmpty());
        assertFalse(deque.isEmpty());
    }

    @Test
    public void testRemoveFirstNode() {
        Deque<String> deque = new Deque<>();
        String aStrngItem = "First";
        deque.addFirst(aStrngItem);
        String removedItem = deque.removeFirst();
        assertEquals("First", removedItem);
    }

    @Test
    public void testRemoveLastNode() {
        Deque<String> deque = new Deque<>();
        String aStringItem = "First";
        String bStringItem = "Second";
        deque.addFirst(aStringItem);
        deque.addFirst(bStringItem);
        String removedItem = deque.removeLast();
        assertEquals("First", removedItem);
    }

    @Test
    public void testIteratorDequeque() {
        Deque<String> deque = new Deque<>();
        String aStrngItem = "First";
        String bStrngItem = "Second";
        deque.addFirst(aStrngItem);
        deque.addFirst(bStrngItem);
        for(String currentItem:deque) {
            System.out.println(currentItem);
            for(String innerCurrentItem:deque) {
                System.out.println(innerCurrentItem);
            }
        }
    }

    @Test
    public void testRand() {
        System.out.println(StdRandom.uniform(2));
        System.out.println(StdRandom.uniform(2));
        System.out.println(StdRandom.uniform(2));
        System.out.println(StdRandom.uniform(2));
        System.out.println(StdRandom.uniform(2));
        System.out.println(StdRandom.uniform(2));
    }
}