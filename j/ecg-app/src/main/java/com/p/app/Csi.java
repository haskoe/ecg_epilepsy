package com.p.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class Csi {
    public static List<CsiResult> CalcCsi(double[] rr, int winSize) { // throws Exception {
        int numResults = rr.length - winSize;
        if (numResults <= 0)
            return null;

        int medianWindow = 5;
        int stop = rr.length - medianWindow;
        double[] cumSum = CumSum(rr);
        double[] medRR = Cfg.findMedian(rr, medianWindow);

        
        List<Double> neighbourDiff = new ArrayList<Double>();
        List<Double> neighbourAdd = new ArrayList<Double>();
        List<Double> neighbourModDiff = new ArrayList<Double>();
        List<Double> neighbourModAdd = new ArrayList<Double>();
        for (int i = 1; i < rr.length; i++) {
            neighbourDiff.add(rr[i - 1] - rr[i]);
            neighbourAdd.add(rr[i - 1] + rr[i]);
            neighbourModDiff.add(medRR[i - 1] - medRR[i]);
            neighbourModAdd.add(medRR[i - 1] + medRR[i]);
        }

        return null;
    }

    public static double[] CumSum(double[] arr)
    {
		double[] result = new double[arr.length];
        double sum = 0;
        for (int i=0; i<arr.length; i++) {
            sum += arr[i];
            result[i] = sum;
        }
        return result;
    }
}
