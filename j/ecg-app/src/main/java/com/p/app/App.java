package com.p.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        // Random rand = new Random();
        // CsiResult result = null;
        // for (int i = 0; i < 1000; i++) {
        // result = movingCsi.addRR(1000 * (0.8 + 0.1 * rand.nextDouble()));
        // }
        String homeDir = System.getProperty("user.home");
        Path pth = Paths.get(homeDir, "dev", "haskoe", "ecg_epilepsy", "p16-rr.dat");
        String fileName = pth.toString();
        
        MovingCsi movingCsi = new MovingCsi(100, 3);
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String str;
            int cnt = 0;
            while ((str = in.readLine()) != null) {
                double rr = Double.parseDouble(str);
                CsiResult result = movingCsi.addRR(rr);
                if (null != result) {
                    System.out.println(result.Csi);
                    cnt += 1;
                    if (cnt > 1000)
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("File Read Error");
        }
    }
}
