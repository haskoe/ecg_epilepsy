package com.p.app;

import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        Random rand = new Random();
        double[] rr = new double[1000];
        for (int i = 0; i < rr.length; i++) {
            rr[i] = 0.8 + 0.1 * rand.nextDouble();
        }
        List<CsiResult> csi = Csi.CalcCsi(rr, 100);
    }
}
