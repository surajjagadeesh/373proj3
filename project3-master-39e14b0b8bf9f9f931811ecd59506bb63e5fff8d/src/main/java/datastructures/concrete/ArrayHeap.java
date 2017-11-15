package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

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
    private int size;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(NUM_CHILDREN+1);
        size = 0;
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

    public T removeMin() {
        if (size == 0) {
        	throw new EmptyContainerException();
        }
        T value = heap[0];
        if (size == 1) {
        	heap[0] = null;
        	size--;
        	return value;
        	
        }
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        percolateDown(0);
        return value;
    }
    
    private void percolateDown(int i) {
    	T smallest = heap[i];
    	int smallestIndex = i;
    	int child = (i * 4) + 1;
    	int lastChild = (i * 4) + 5;
    	while (child < size - 1 && child < lastChild) {
    		if (heap[child].compareTo(smallest) < 0) {
    			smallest = heap[child];
    			smallestIndex = child;
    		}
    		child++;
    	}
    	if (smallestIndex != i) {
    		percolateDown(smallestIndex);
    	}
    }
    
    private void percolateUp(int i) {
    	int parent = (i - 1) / 4;
    	int child = i;

    	if (i == 0 || heap[parent].compareTo(heap[child]) <= 0) {
    		//done
    	} else {
    		T childValue = heap[child];
    		heap[child] = heap[parent];
    		heap[parent] = childValue;
    		percolateUp(parent);
    	}
    }
    

    @Override
    public T peekMin() {
        return heap[0];
    }

    @Override
    public void insert(T item) {
        heap[size] = item;
        percolateUp(size);
        size++;
    }

    @Override
    public int size() {
        return size;
    }
}
