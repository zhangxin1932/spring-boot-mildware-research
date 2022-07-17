package com.zy.spring.mildware.misc.collection;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;

public class CollectionSamples {

    public static void main(String[] args) {
        f1();
    }

    /**
     * List如何一边遍历，一边删除
     */
    private static void f1() {
        ArrayList<String> list = Lists.newArrayList("1", "3", "99", "85");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (Integer.parseInt(next) > 20) {
                iterator.remove();
            }
        }
        System.out.println(list);
    }
}
