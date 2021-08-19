package com.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class MultiThreading<T>{

    private ExecutorService executor;
    private volatile boolean flag;
    private AtomicInteger count;
    private Set<T> visited;
    private volatile List<Callable<Integer>> callableList;

    public MultiThreading(int nThreads){
        executor = Executors.newFixedThreadPool(nThreads);
        count = new AtomicInteger(0);
        visited = new TreeSet<>();
        flag = false;
        callableList = new ArrayList<>();
    }

    private void makeCallableList(final List<T> list,final T target,final int jStart,final int jEnd) {
        callableList.add(()->{
            final int start = jStart;
            final int end = jEnd;
            for(int j = start; j< end; j++){
                count.getAndAdd(1);
//                visited.add(list.get(j));
                if(list.get(j).equals(target)){
                    System.out.println("Visited target idx "+j);
                    flag=true;
                    return j;
                }
                else if(flag) {
                    return -1;
                }
            }
            return -1;
        });
    }

    public Integer linearSearch(final List<T> list,final T target,final int noOfDiv) {
        int size = list.size();
        int lastIndex = size/noOfDiv;

        for(int i=0;i<noOfDiv;i++){
            int jStart = i*lastIndex;
            int jEnd = (i+1)*lastIndex;
            makeCallableList(list, target, jStart, jEnd);
        }

        makeCallableList(list, target, noOfDiv*lastIndex, size);

        List<Future<Integer>> futures = null;
        try {
            futures = executor.invokeAll(callableList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Integer> foundList = new ArrayList<>();

        futures.forEach(ftr -> {
            try {
                foundList.add(ftr.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        executor.shutdown();
        System.out.println("Visited this many indexes "+count);

        System.out.println("Unique indexes "+ visited.size());
        System.out.println("Visited set has "+target+" : "+visited.contains(target));
        System.out.println(foundList);
        if(!flag){
            return -1;
        }

        return foundList.parallelStream().filter(x->x!=-1).findFirst().get();
    }


}


