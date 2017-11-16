package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest { 

    IPriorityQueue<Integer> heap = this.makeInstance();
    //AssertEquals with collections.sort
    
    int MAX = 500000;
    int MIN = 3000;
    int K = 250000;
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
    //Test sort large heap (small/large)
    //Test sort small heap (small/large)
    
    //Test sort random
    //Test sort many random
    // -- 10 x 100 elements with values -500000 to 499999
    //Test sort repeated values
    //Test sort sorted
    //Test sort reverse
    //Test sort empty
    //Test sort null
    //Test sort partially sorted
    // -- [0,1] , [1,0] [0,1,2] permutations

    
}
