package com.p.app;

import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        //Random rand = new Random();
        // CsiResult result = null;
        // for (int i = 0; i < 1000; i++) {
        //     result = movingCsi.addRR(1000 * (0.8 + 0.1 * rand.nextDouble()));
        // }

        MovingCsi movingCsi = new MovingCsi(100, 3);
        CsiResult result = null;
        double[] rr = MovingCsi.readCsv("\\Users\\henri\\proj\\p\\Epilepsy_project\\new\\p16.dat");
        for (int i = 0; i < rr.length; i++) {
            result = movingCsi.addRR(rr[i]);
        }
    }
}
