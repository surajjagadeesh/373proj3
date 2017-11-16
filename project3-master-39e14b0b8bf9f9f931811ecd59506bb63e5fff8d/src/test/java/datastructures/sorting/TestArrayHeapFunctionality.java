package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Arrays;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    int MIN= 3000;
    int MAX = 400000;
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    public IPriorityQueue<Integer> makeBasicHeap() {
       IPriorityQueue<Integer> heap = this.makeInstance();
       heap.insert(1);
       heap.insert(2);
       heap.insert(3);
       heap.insert(4);
       heap.insert(5);
       return heap;
       
   }
   
    protected <T> void assertHeapsMatch(T[] expected, IPriorityQueue<Integer> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());
        
        for(int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i] , actual.removeMin());
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
    public void checkBasicHeap() {
    	IPriorityQueue<Integer> heap = this.makeBasicHeap();
    	Integer[] expected = new Integer[] {1, 2, 3, 4, 5};
    	this.assertHeapsMatch(expected, heap);
    }
    
    
    /*
    @Test(timeout=SECOND)
    public void testInternalMatching() {
        //empty
        Integer[] expected = new Integer[MAX];
        IPriorityQueue<Integer> actual = this.makeInstance();
        
        this.assertHeapsMatch(expected, actual);
        
        for (int i = 0; i < MAX; i++) {
            expected[i] = i*i;
            actual.insert(i*i);
            this.assertHeapsMatch(Arrays.copyOfRange(expected, 0, i+1), actual);
        }
        
        this.assertHeapsMatch(expected, actual);
        
    }
    
    @Test(timeout=SECOND)
    public void testMakeBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(heap.peekMin(), 3);
        assertEquals(1, heap.size());
        this.assertHeapsMatch(new Integer[] {3}, heap);
    }
    
    @Test(timeout=SECOND)
    public void testMakeLargeSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] expected = new Integer[MAX];
        for (int i = 0; i < MAX; i++) {
            heap.insert(i*i);
            expected[i] = i*i;
        }
        
        assertEquals(MAX, heap.size());
        this.assertHeapsMatch(expected, heap);
    }
    
    //check peek empty
    @Test(timeout=SECOND)
    public void testPeekEmptyThrowsException() {
        IPriorityQueue<Integer> heap = makeBasicHeap();
        assertEquals(heap.peekMin(), 1);
        heap.removeMin();
        this.assertHeapsMatch(new Integer[] {2,3,4}, heap);
        assertEquals(heap.peekMin(), 2);
        heap.removeMin();
        this.assertHeapsMatch(new Integer[] {3,4}, heap);
        assertEquals(heap.peekMin(), 3);
        heap.removeMin();
        this.assertHeapsMatch(new Integer[] {4}, heap);
        assertEquals(heap.peekMin(), 4);
        heap.removeMin();
        assertEquals(heap.isEmpty(), true);
        
        try {
            heap.peekMin();            
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //Do nothing. This is desired.
        }
    }
    
    //check remove empty
    @Test(timeout=SECOND)
    public void testRemoveEmptyThrowsException() {
        IPriorityQueue<Integer> heap = makeBasicHeap();
        assertEquals(heap.removeMin(), 1);
        assertEquals(heap.removeMin(), 2);
        assertEquals(heap.removeMin(), 3);
        assertEquals(heap.removeMin(), 4);
        
        assertEquals(heap.isEmpty(), true);
        
        try {
            heap.removeMin();           
            fail("EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //Do nothing. This is desired.
        }
    }
    
    //check insert null
    @Test(timeout=SECOND)
    public void testInsertNullThrowsException() {
        IPriorityQueue<Integer> heap = makeBasicHeap();
        //Check basic filled heap
        try {
            heap.insert(null);
            fail("IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //Check null heap
            try {
                IPriorityQueue<Integer> heap2 = null;
                heap.insert(null);
            } catch (IllegalArgumentException ex2) {
                //Do nothing. This is desired.
            }
        }
    }
    
    @Test(timeout=SECOND)
    public void testRepeatedInsertAndPeek() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = MAX; i < 0; i--) {
            heap.insert(i*i);
            assertEquals(heap.peekMin(), i*i);
        }
    }
    
    @Test(time=SECOND)
    public void testRepeatedRemoveAndPeak() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] expected = new Integer[MIN];
        for (int i = 0; i < MAX; i++) {
            heap.insert(i*i);
            expected[i] = i*i;
        }
        
        this.assertHeapsMatch(expected, heap);
        
        for (int i = 0; i < MAX; i++) {
            assertEquals(heap.peekMin(), i*i);
            assertEquals(heap.removeMin(),  i*i);
        }
    }
    
    @Test(time=SECOND)
    public void testRepeatedRemoveIntegrity() {
        IPriorityQueue<Integer> actual = this.makeInstance();
        Integer[] expected = new Integer[MIN];
        
        for (int i = 0; i < MIN; i++) {
            actual.insert(i*i);
            expected[i] = i*i;
        }
        for (int i = 0; i < MIN; i++) {
            assertEquals(actual.removeMin(), i*i);
            this.assertHeapsMatch(Arrays.copyOfRange(expected, i, expected.length - 1),heap);
        }
    }
    
    @Test(time =SECOND)
    public void testEmptyAlternatingInsertRemove() {
        IPriorityQueue<Integer> actual = this.makeInstance();
        
        //remove alternating with few elements
        for(int i = 0; i < MAX/2; i++) {
            actual.insert(i*i);
            assertEquals(actual.removeMin(), i*i);
        }   
    }
    
    @Test(time =SECOND)
    public void testFilledAlternatingInsertRemove() {
        IPriorityQueue<Integer> actual = this.makeInstance();
        for (int i = 0; i < 500; i++) {
            
        }
        //remove alternating with few elements
        for(int i = 0; i < MAX/2; i++) {
            actual.insert(-1*i*i);
            assertEquals(actual.removeMin(), -1*i*i);
        }   
    }
    
    private void fail(String string) {
        // TODO Auto-generated method stub
        
    }
    @Test(time=SECOND)
    public void testSimpleInsertAndRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        assertEquals(heap.isEmpty(),true);
        
        heap.insert(1);
        this.assertHeapsMatch(new Integer[] {1}, heap);
        assertEquals(heap.peekMin(), 1);
        
        assertEquals(heap.removeMin(), 1);
        assertEquals(heap.isEmpty(), true);
    }
    
    @Test(time=SECOND)
    public  void testRemoveWithNegatives() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -1*(MAX/2); i < (MAX/2); i++) {
            heap.insert(i);
        }
        
        int negativeCount = MAX/2;
        int postitiveCount = MAX - negativeCount;
        
        //Checks all negative values 
        for (int i = -1*negativeCount; i < 0; i++) {
            assertEquals(heap.removeMin(),i);
        }
        //Make sure we're at 0
        assertEquals(heap.removeMin(), 0); 
        //Checks all postitive values
        for (int i = 1; i < postitiveCount; i++) {
            assertEquals(heap.removeMin(),i);
        }
        
    }
    
    // Runs in 2O(n) could fail cause load size
    @Test(time=SECOND)
    public void testInsertManyThenRemoveMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        assertEquals(0, heap.size());
        assertEquals(heap.isEmpty(),true);
        
        for (int i = MAX; i > 0; i--) {
            heap.insert(i*i);
            assertEquals(heap.peekMin(), i*i);
            assertEquals(MAX - i + 1, heap.size());
        }
        
        for (int i = 1; i <= MAX; i++) {
            assertEquals(heap.removeMin(), i*i);
        }
        assertEquals(heap.isEmpty(), true);
    }
   */
}
