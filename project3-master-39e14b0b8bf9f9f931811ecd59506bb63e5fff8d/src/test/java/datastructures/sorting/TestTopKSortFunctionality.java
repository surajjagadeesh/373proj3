package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.Searcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    int MAX = 500000;
    int MIN = 3000;
    int K = 100;
    
    @Test(timeout=10*SECOND)
    public <T> void assertProperlySorted(T[] expected, IPriorityQueue<Integer> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());
        
        for(int i = 0; i < expected.length; i++) {
            try {
                //!!!
                //!!!
                //  Need to update ILIST
                //!!!
                //!!!
                
                //assertEquals("Implemented sort does not match collections.sort(..)", Collections.sort(expected), actual);
                
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
    
    private int generatRandPositiveNegitiveNum(int max , int min) {
        Random rand = new Random();
        int n = -min + (int) (Math.random() * ((max - (-min)) + 1));
        return n;
    }
    
    //index < n/2 negative
    //index > n/2 postive
    private IList<Integer> makeSortedList(int n) {
        IList<Integer> list = new DoubleLinkedList<>();
        int orgin = (int)Math.floor(Math.random()) * MAX/2;
        orgin = -1*orgin;
        while (n > 0) {
            list.add(orgin);
            orgin++;
            n--;
        }
        assertEquals(list.size(), n);
        return list;
    }
    
    private IList<Integer> makeRandomList(int n) {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < n; i++) {
            int num = (int) Math.floor(Math.random()*MAX);
            
            list.add(generatRandomPositiveNegitiveValue(-1*(MAX/2), (MAX/2) ));
        }
        return list;
    }
    
    private IList<Integer> makeReversedSortedList(int n) {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = n; i > 0; i--) {
            list.add(i);
        }
        return list;
    }
    
    @Test(Timeout=SECOND)
   public void testSortSmall() {
        IList<Integer> list = makeSortedList(10);
        //assertEquals(Searcher.topKSort(5, list), Collections.sort(list));
        //assertEquals(Searcher.topKSort(15, list), Collections.sort(list));

    }    
    
    
    @Test(Timeout=SECOND)
   public void testSortLarge() {
        IList<Integer> list = makeSortedList(MAX);
        
        //List for comparison
        IList<Integer> expected = new DoubleLinkedList<Integer>();
        for (int i = 0; i < K; i++) {
            expected.add(list.get(i));
        }
        //expected = Collections.sort(expected);
        
        //Get Many K-Sorted
        for (int i = 0; i < MAX/2; i++) {
            IList<Integer> top = Searcher.topKSort(K, list);
            //assertProperlySorted(top, expected);
        }
    }
    
  @Test(Timeout=SECOND)
  public void testKLargerThanSize() {
      IList<Integer> list = makeSortedList(MAX/3);
      IList<Integer> top = Searcher.topKSort(MAX, list);
      //assertProperlySorted(top, Collections.sort(list);
      
      list = makeReversedSortedList(MAX/3);
      top = Searcher.topKSort(MAX, list);
      //assertProperlySorted(top, Collections.sort(list);
      
      list = makeRandomList(MAX/3);
      top = Searcher.topKSort(MAX, list);
      //assertProperlySorted(top, Collections.sort(list);
  }
  
  @Test(Timeout=SECOND)
  public void testKSmallerThanSize() {
      IList<Integer> list = makeSortedList(MAX);
      IList<Integer> top = Searcher.topKSort(MAX/2, list);
      //assertProperlySorted(top, Collections.sort(Arrays.copyOfRange(list, 0, MAX/2));
  }
  
  @Test(Timeout = SECOND)
  public void testRandomK() {
      for (int i = 0; i < MIN; i++) {
          IList<Integer> list = makeSortedList(MIN);
          int rand = (int)Math.random() * MIN;
          IList<Integer> top = Searcher.topKSort(rand, list);
          //assertProperlySorted(top, Collections.sort(Arrays.copyOfRange(list, 0, rand));
      }
  }
    
  @Test(Timeout = SECOND) {
  public void testFrontIsSmallest() {
      IList<Integer> list = Searcher.topKSort(K,makeRandomList(MAX));
      int n = list.get(0);
      boolean isTrue = true;
      for (int i = 1; i < list.size(); i++) {
          if (list.get(i) < n) {
              isTrue = false;
              break;
          }
      }
      assertEquals(isTrue, true);
  }
  
  @Test(Timeout = SECOND) {
  public void testLastIsLargest() {
      IList<Integer> list = Searcher.topKSort(K,makeRandomList(MAX));
      int n = list.get(list.size() - 1);
      boolean isTrue = true;
      for (int i = 1; i < list.size(); i++) {
          if (list.get(i) > n) {
              isTrue = false;
              break;
          }
      }
      assertEquals(isTrue, true);
  }
  
}
