//Suraj Jagadeesh and Allen Putich

package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

import java.util.Arrays;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int heapSize;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(NUM_CHILDREN+1);
        heapSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue are considered "equal"
     * according to their compareTo method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    public T removeMin() {
        if (heapSize == 0) {
        	throw new EmptyContainerException();
        }
        T value = heap[0];
        heapSize--;
        if (heapSize == 0) {
        	heap[0] = null;
        	return value;
        }
        heap[0] = heap[heapSize];
        heap[heapSize] = null;
        percolateDown(0);
        ensureDownheapSize();
        return value;
    }
    
    /**
     * Checks if array is full, if so doubles size of array and copies everything over
     */
    private void ensureCapacity() {
    	if (heapSize == heap.length) {
    		heap = Arrays.copyOf(heap, heapSize * 2);
    	}
    }
    
    /**
     * Checks if array is 1/4 full, if so halves size of array and copies everything over
     */
    private void ensureDownheapSize() {
    	if (heapSize <= heap.length / 4 && heap.length >= 10) { //so that array.length is >=5
    		heap = Arrays.copyOf(heap, heap.length / 2);
    	}
    }
    
    /**
     * Percolates down (if possible) from the value at the specified index of the array, i
     */
    private void percolateDown(int i) {
    	int child = (i * 4) + 1;
    	if (heapSize - 1 >= child) {
    		T smallest = heap[i];
        	int smallestIndex = i;
        	int lastChild = (i * 4) + 5;
        	while (child < heapSize && child < lastChild) {
        		if (heap[child].compareTo(smallest) < 0) {
        			smallest = heap[child];
        			smallestIndex = child;
        		}
        		child++;
        	}   	
        	if (smallestIndex != i) {
        		heap[smallestIndex] = heap[i];
            	heap[i] = smallest;
        		percolateDown(smallestIndex);
        	}
    	}
    }
    
    /**
     * Percolates up (if possible) from the value at the specified index of the array, i
     */
    private void percolateUp(int i) {
    	int parent = (i - 1) / 4;
    	int child = i;

    	if (i != 0 && heap[parent].compareTo(heap[child]) > 0) {
    		T childValue = heap[child];
    		heap[child] = heap[parent];
    		heap[parent] = childValue;
    		percolateUp(parent);
    	}
    }
    

    /**
     * Returns, but does not remove, the smallest element in the queue.
     *
     * This method must break ties in the same way the removeMin
     * method breaks ties.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    public T peekMin() {
    	if (heapSize == 0) {
        	throw new EmptyContainerException();
        }
        return heap[0];
    }

    /**
     * Inserts the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    public void insert(T item) {
    	if (item == null) {
    		throw new IllegalArgumentException();
    	}
        heap[heapSize] = item;
        percolateUp(heapSize);
        heapSize++;
        ensureCapacity();
    }

    /**
     * Returns the number of elements contained within this queue.
     */
    public int size() {
        return heapSize;
    }
}
