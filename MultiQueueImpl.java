package p12.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    final Map<Q,Queue<T>> map = new HashMap<>(); 
    @Override
    public Set<Q> availableQueues() {
        // returns a set view of the keys contained in this map 
        return map.keySet(); 
    }

    @Override
    public void openNewQueue(Q queue) {
        // return true if this map contains a mapping for the specified key 
        if(map.containsKey(queue)) { 
            throw new IllegalArgumentException("Queue is just open.\n");
        }

        // create a new queue 
        map.put(queue, new LinkedList<>()); 
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        Queue<T> q = map.get(queue);

        if(q == null) {
            throw new IllegalArgumentException("Queue is not open.\n");
        }
        
        //return true if this queue contains no elements 
        return q.isEmpty(); 
    }

    @Override
    public void enqueue(T elem, Q queue) {
        Queue<T> q = map.get(queue);

        if(q == null) { 
            throw new IllegalArgumentException("Queue is not open.\n");
        }

        // inserts the new element in the queue 
        q.add(elem); 
    }

    @Override
    public T dequeue(Q queue) {
        Queue<T> q = map.get(queue);

        if(q == null) {
            throw new IllegalArgumentException("Queue is not open.\n");
        }

        // Retrieves and removes the head of this queue or returns null if this queue is empty
        return q.poll(); 
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> dequeueElement = new HashMap<>();

        for(Q queue : map.keySet()) {
            Queue<T> q = map.get(queue);
            if(q != null) { 
                
                T elem = q.poll();
                //initialize a map whit the set of dequeued elements 
                dequeueElement.put(queue, elem);
            }
        }

        return dequeueElement;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        // creates the set of elements 
        Set<T> allElements = new HashSet<>(); 

        for(Queue<T> queue : map.values()) {
            // initialize elements whit the set of all enqueued elements 
            allElements.addAll(queue); 
        }

        return allElements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        Queue<T> q = map.get(queue);
        if(q == null) {
             throw new IllegalArgumentException("Queue is not open.\n");
        }

        // creates the list  
        List<T> list = new ArrayList<>(); 
        
        while(!q.isEmpty()) {

            // initialize the list with all the dequeued elements and clears the queue
             list.add(q.poll());
        }
    
        return list;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        List<T> elements = dequeueAllFromQueue(queue);

        if(!map.containsKey(queue)) {
            throw new IllegalArgumentException("Queue is not open.\n");
        }

        if(map.keySet().size() <= 1) {
            throw new IllegalStateException("There aren't other queue.\n");
        }
        
        Queue<T> newQueue = null;
        for(Q q : map.keySet()) {

            // find the first queue (different from the closed one)
            if(!q.equals(queue) && newQueue == null) {
                newQueue = map.get(q);
            }
        }

        // add the list of removed elements  
        newQueue.addAll(elements); 
                
        // removes the queue from map
        map.remove(queue);
    }

}
