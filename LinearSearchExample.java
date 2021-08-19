package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LinearSearchExample {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        int noOfDiv = 10;
        int size = 10000;
        int lastIndex = size/noOfDiv;
        Integer[] arr = new Integer[size];

        int target=8000;
        for(int i=0;i<size;i++){
            arr[i]=i+1;
        }

        List<Callable<Boolean>> list = new ArrayList<>();
        for(int i=0;i<noOfDiv;i++){
            int currI = i;
            list.add(()->{
                for(int j=0;j<lastIndex;j++){
                    int idx= (currI *lastIndex)+j;
                    if(arr[idx]==target){
                        System.out.println(idx);
                        return true;
                    }

                }
                return false;
            });
        }

        List<Future<Boolean>> futures = executor.invokeAll(list);
        List<Boolean> foundList = new ArrayList<>();
        futures.forEach(ftr -> {
            try {
                foundList.add(ftr.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        boolean found=false;
        for(boolean bool:foundList){
            found=found||bool;
        }
        System.out.println(found);
        executor.shutdown();
    }
}
