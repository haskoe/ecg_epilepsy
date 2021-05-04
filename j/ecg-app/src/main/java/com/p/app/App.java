package com.p.app;

import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        Random rand = new Random();
        MovingCsi movingCsi = new MovingCsi(10, 2);
        for (int i = 0; i < 100; i++) {
            movingCsi.addRR(1000 * (0.8 + 0.1 * rand.nextDouble()));
        }
    }
}
