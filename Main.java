package com.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int size = 100000000;
        List<Integer> list = new ArrayList<>(100000000);
        for(int i=0;i<size;i++){
            list.add(i+1);
        }
        MultiThreading threading = new MultiThreading(10);
        int idx = threading.linearSearch(list,99850000,10);
        System.out.println("Found at index "+idx);
    }
}
