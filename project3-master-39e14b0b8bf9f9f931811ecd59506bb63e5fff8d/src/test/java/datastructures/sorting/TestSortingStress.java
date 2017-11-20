//Suraj Jagadeesh and Allen Putich

package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest { 
	private static final int MAX = 1000000;
	
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
	}
	
	protected <T> void assertHeapsMatch(T[] expected, IPriorityQueue<Integer> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());
        
        for (int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i], actual.removeMin());
            } catch (Exception ex){
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }
	
	protected <T> void assertProperlySorted(List<Integer> expected, IList<Integer> actual) {
        assertEquals(expected.size() == 0, actual.isEmpty());
        Collections.sort(expected);
        int diff = expected.size() - actual.size() - 1;
        
        for (int i = actual.size(); i > 0; i--) {
        	Integer value = actual.remove();
            try {
                assertEquals(expected.get(i + diff), value);
            } catch (Exception ex){
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        value);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }
	
	@Test(timeout=SECOND)
	public void testStressInsertHeap() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = MAX; i > 0; i--) {
			heap.insert(i);
			assertEquals(MAX + 1 - i, heap.size());
		}
	}
	
	public void testInsertAndPeek() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = MAX; i > 0; i--) {
			heap.insert(i);
			assertEquals(i, heap.peekMin());
			assertEquals(MAX + 1 - i, heap.size());
		}
	}
	
	@Test(timeout=2* SECOND)
    public void alternatingInsertAndRemove() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	Integer[] expected = new Integer[MAX];
    	
    	for (int i = 0; i < MAX; i++) {
    		heap.insert(i * 2);
    		heap.insert(i * 2 + 1);
    		expected[i] = i + MAX;
    		assertEquals(i, heap.removeMin());
    		
    	}
    	this.assertHeapsMatch(expected, heap);
    }
	
	@Test(timeout=SECOND)
	public void insertDuplicates() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		Integer[] expected = new Integer[MAX];
    	for (int i = 0; i < MAX; i++) {
    		heap.insert(1);
    		expected[i] = 1;
    		assertEquals(i + 1, heap.size());
    	}
    	this.assertHeapsMatch(expected, heap);
	}
	
	@Test(timeout=SECOND)
	public void testSortSorted() {
		List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = 0; i < MAX; i++) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX / 2, nums));
	}
	
	@Test(timeout=SECOND)
	public void testSortReverseSorted() {
		List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > 0; i--) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX / 2, nums));
	}
	
	@Test(timeout=SECOND)
	public void testSortHalfSorted() {
		List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > MAX / 2; i--) {
    		expected.add(i);
    		nums.add(i);
    	}
    	for (int i = 0; i < MAX / 2; i++) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX / 2, nums));
	}
	
	@Test(timeout=SECOND)
	public void testStressDuplicates() {
		List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = 0; i < MAX; i++) {
    		expected.add(1);
    		nums.add(0);
    	}
    	for (int i = 0; i < MAX; i++) {
    		expected.add(0);
    		nums.add(1);
    	}
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX, nums));
	}
	
	@Test(timeout=SECOND)
	public void testSortRandom() {
		IList<Integer> nums = new DoubleLinkedList<>();
    	List<Integer> expected = new ArrayList<Integer>();
    	Random r = new Random();
    	for (int i = 0; i < MAX; i++) {
    		int value = r.nextInt(100) - 50;
    		nums.add(value);
    		expected.add(value);
    	}
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX/2, nums));
	}
	
	@Test(timeout=SECOND)
	public void testPartiallySorted() {
		List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = 0; i < MAX; i++) {
    		expected.add(1);
    		expected.add(0);
    		nums.add(0);
    		nums.add(0);
    	}
    	for (int i = 0; i < MAX; i++) {
    		expected.add(0);
    		expected.add(1);
    		nums.add(1);
    		nums.add(1);
    	}
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX, nums));
	}
	
}
