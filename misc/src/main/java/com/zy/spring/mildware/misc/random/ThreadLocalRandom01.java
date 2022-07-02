package com.zy.spring.mildware.misc.random;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandom01 {
    public static void main(String[] args) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int i = random.nextInt(1, 2);
        System.out.println(i);
    }
}
