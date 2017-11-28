//Suraj Jagadeesh and Allen Putich

package datastructures.sorting;

import misc.BaseTest;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    private static final int MAX = 100;
	
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
    public void testInOrderSort() {
    	List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = 0; i < MAX; i++) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(5, nums));
    }
    
    @Test(timeout=SECOND)
    public void testReverseOrderSort() {
    	List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > 0; i--) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(5, nums));
    }
    
    @Test(timeout=SECOND)
    public void testHalfSortedSort() {
    	List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > 50; i--) {
    		expected.add(i);
    		nums.add(i);
    	}
    	for (int i = 0; i < 51; i++) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(5, nums));
    }
    
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegativeNumbers() {
    	List<Integer> expected = new ArrayList<>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > 50; i--) {
    		expected.add(i);
    		nums.add(i);
    	}
    	for (int i = 0; i < 51; i++) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(5, nums));
    }
    
    @Test(timeout=SECOND)
    public void testKMoreThanSize() {
    	List<Integer> expected = new ArrayList<Integer>();
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > 0; i--) {
    		expected.add(i);
    		nums.add(i);
    	}
    	
    	this.assertProperlySorted(expected, Searcher.topKSort(nums.size()+5, nums));
    }
    
    @Test(timeout=SECOND)
    public void testKisZero() {
    	IList<Integer> nums = new DoubleLinkedList<>();
    	for (int i = MAX; i > 0; i--) {
    		nums.add(i);
    	}
    	
    	IList<Integer> actual = Searcher.topKSort(0, nums);
    	assertTrue(actual.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testSortEmptyList() {
    	IList<Integer> actual = Searcher.topKSort(5, new DoubleLinkedList<>());
    	assertTrue(actual.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testSortListWithOneElement() {
    	IList<Integer> nums = new DoubleLinkedList<>();
    	nums.add(0);
    	
    	List<Integer> expected = new ArrayList<Integer>();
    	expected.add(0);
    	this.assertProperlySorted(expected, Searcher.topKSort(5, nums));	
    }
    
    @Test(timeout=SECOND)
    public void testDuplicateLists() {
    	IList<Integer> nums = new DoubleLinkedList<>();
    	List<Integer> expected = new ArrayList<Integer>();
    	
    	for (int i = 0; i < MAX; i++) {
    		nums.add(0);
    		expected.add(0);
    	}
    	for (int i = 0; i < MAX; i++) {
    		nums.add(1);
    		expected.add(1);
    	}
    	this.assertProperlySorted(expected, Searcher.topKSort(MAX * 2, nums));
    }
    
    @Test(timeout=SECOND)
    public void testRandomNumbers() {
    	IList<Integer> nums = new DoubleLinkedList<>();
    	List<Integer> expected = new ArrayList<Integer>();
    	Random r = new Random();
    	for (int i = 0; i < MAX; i++) {
    		int value = r.nextInt(100) - 50;
    		nums.add(value);
    		expected.add(value);
    	}
    	this.assertProperlySorted(expected, Searcher.topKSort(10, nums));
    }
    
    @Test(timeout=SECOND)
    public void testKNegativeThrowsException() {
    	IList<Integer> nums = new DoubleLinkedList<>();
    	List<Integer> expected = new ArrayList<Integer>();
    	Random r = new Random();
    	for (int i = 0; i < MAX; i++) {
    		int value = r.nextInt(100) - 50;
    		nums.add(value);
    		expected.add(value);
    	}
        try {
        	this.assertProperlySorted(expected, Searcher.topKSort(-5, nums));
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
}
