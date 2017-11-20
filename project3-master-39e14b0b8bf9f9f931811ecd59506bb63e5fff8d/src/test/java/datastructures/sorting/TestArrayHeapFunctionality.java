//Suraj Jagadeesh and Allen Putich

package datastructures.sorting;

import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    private static final int MAX = 100;
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    public IPriorityQueue<Integer> makeBasicHeap() {
       IPriorityQueue<Integer> heap = this.makeInstance();
       for (int i = 1; i <= 5; i++) {
    	   heap.insert(i);
       }
       return heap;
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
    
    @Test(timeout=SECOND)
    public void checkEmptyHeapsMatch() {
    	Integer[] expected = new Integer[0];
        IPriorityQueue<Integer> actual = this.makeInstance();       
        this.assertHeapsMatch(expected, actual);
    }
    
    @Test(timeout=SECOND)
    public void testBasicSize() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(3);
    	assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void checkInsertAndRemoveBasicHeap() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	Integer[] expected = new Integer[] {1, 2, 3, 4, 5};
    	this.assertHeapsMatch(expected, heap);
    }
    
    @Test(timeout=SECOND)
    public void testRepeatedInsertAndRemove() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	Integer[] expected = new Integer[MAX];
    	for (int i = 0; i < MAX; i++) {
    		heap.insert(i);
    		expected[i] = i;
    		assertEquals(i + 1, heap.size());
    	}
    	this.assertHeapsMatch(expected, heap);
    }
    
    @Test(timeout=SECOND)
    public void testEmptyInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        this.assertHeapsMatch(new Integer[] {3}, heap);
    }
    
    @Test(timeout=SECOND)
    public void testInsertSize() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for (int i = 1; i < MAX; i++) {
    		heap.insert(i);
    		assertEquals(i, heap.size());
    	}
    	
    	heap = this.makeInstance();
    	for (int i = MAX; i > 0; i--) {
    		heap.insert(i);
    		assertEquals((MAX - i + 1), heap.size());
    	}
    }
    
    @Test(timeout=SECOND)
    public void checkNegativeBasicInsertAndRemove() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(0);
    	heap.insert(-1);
    	heap.insert(1);
    	heap.insert(-2);
    	heap.insert(-3);
    	
    	Integer[] expected = new Integer[] {-3, -2, -1, 0, 1};
    	this.assertHeapsMatch(expected, heap);
    }
    
    @Test(timeout=SECOND)
    public void testEmptyInsertNullThrowsException() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
        try {
        	heap.insert(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertDuplicateSize() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for (int i = 1; i < MAX; i++) {
    		heap.insert(1);
    		assertEquals(i, heap.size());
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInsertNullThrowsException() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
        try {
        	heap.insert(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    public void testRemoveBasicSize() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	for (int i = 5; i > 0; i--) {
        	heap.removeMin();
        	assertEquals(i - 1, heap.size());
        }
    }
    
    //check remove empty
    @Test(timeout=SECOND)
    public void testRemoveEmptyThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
    	
        try {
            heap.removeMin();           
            fail("EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //Do nothing. This is desired.
        } 
        
        heap = this.makeBasicHeap();
        
        for (int i = 5; i > 0; i--) {
        	heap.removeMin();
        	assertEquals(i - 1, heap.size());
        }
        
        assertEquals(heap.isEmpty(), true);
        
        try {
            heap.removeMin();           
            fail("EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //Do nothing. This is desired.
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveFromSingle() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(1);
    	assertEquals(1, heap.size());
    	heap.removeMin();
    	assertEquals(0, heap.size());
    	
    }
    
    @Test(timeout=SECOND)
    public void alternatingInsertAndRemove() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	Integer[] expected = new Integer[100];
    	
    	for (int i = 0; i < MAX; i++) {
    		heap.insert(i * 2);
    		heap.insert(i * 2 + 1);
    		expected[i] = i + 100;
    		assertEquals(i, heap.removeMin());
    		
    	}
    	this.assertHeapsMatch(expected, heap);
    }
    
    @Test(timeout=SECOND)
    public void testSinglePeek() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(1);
    	assertEquals(1, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testBasicPeek() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	assertEquals(1, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testRepeatedPeek() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	for (int i = 0; i < MAX; i++) {
    		assertEquals(1, heap.peekMin());
    	}
    	this.assertHeapsMatch(new Integer[] {1, 2, 3, 4, 5}, heap);
    	
    }
    
    @Test(timeout=SECOND)
    public void testPeekAfterInsert() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for (int i = 0; i < MAX; i++) {
    		heap.insert(i);
    		assertEquals(0, heap.peekMin());
    	}
    	
    	heap = this.makeInstance();
    	for (int i = MAX; i > 0; i--) {
    		heap.insert(i);
    		assertEquals(i, heap.peekMin());
    	}
    }
    
    @Test(timeout=SECOND)
    public void testPeekAfterRemove() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	Integer[] expected = new Integer[100];
    	
    	for (int i = 0; i < MAX; i++) {
    		heap.insert(i * 2);
    		heap.insert(i * 2 + 1);
    		expected[i] = i + 100;
    		assertEquals(i, heap.peekMin());
    		heap.removeMin();
    	}
    	this.assertHeapsMatch(expected, heap);
    }
    
    @Test(timeout=SECOND)
    public void testPeekEmptyThrowsException() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	try {
            heap.peekMin();            
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //Do nothing. This is desired.
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekEmptyAfterRemovingThrowsException() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	for (int i = 1; i <= 5; i++) {
    		heap.removeMin();
    	}
    	try {
            heap.peekMin();            
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //Do nothing. This is desired.
        }
    }
    
    @Test(timeout=SECOND)
    public void checkStringsWork() {
    	IPriorityQueue<String> heap = this.makeInstance();
    	for (int i = 0; i < 5; i++) {
    		heap.insert(String.valueOf((char) (i + 'a')));
    		assertEquals("a", heap.peekMin());
    		assertEquals(i + 1, heap.size());
    	}
    	for (int i = 0; i < 5; i++) {
    		assertEquals(String.valueOf((char) (i + 'a')), heap.peekMin());
    		assertEquals(String.valueOf((char) (i + 'a')), heap.removeMin());
    		assertEquals(4 - i, heap.size());
    	}
    }
    
    @Test(timeout=SECOND)
    public void checkDoublesWork() {
    	IPriorityQueue<Double> heap = this.makeInstance();
    	for (int i = 0; i < 5; i++) {
    		heap.insert(1. * i);
    		assertEquals(0.0, heap.peekMin());
    		assertEquals(i + 1, heap.size());
    	}
    	for (int i = 0; i < 5; i++) {
    		assertEquals(1. * i, heap.peekMin());
    		assertEquals(1. * i, heap.removeMin());
    		assertEquals(4 - i, heap.size());
    	}
    }
}
