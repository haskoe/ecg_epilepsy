package com.p.app;

import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        //Random rand = new Random();
        // CsiResult result = null;
        // for (int i = 0; i < 1000; i++) {
        //     result = movingCsi.addRR(1000 * (0.8 + 0.1 * rand.nextDouble()));
        // }

        MovingCsi movingCsi = new MovingCsi(100, 3);
        CsiResult result = null;

        String fileName = "\\Users\\henri\\proj\\p\\Epilepsy_project\\new\\p16.dat";
        try(BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String str;
            while ((str = in.readLine()) != null) {
                double rr = Double.parseDouble(str));
                result = movingCsi.addRR(rr);
            }
        }
        catch (IOException e) {
            System.out.println("File Read Error");
        }        
    }
}
