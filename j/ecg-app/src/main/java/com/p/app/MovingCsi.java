package com.p.app;

import java.util.ArrayList;
import java.util.List;

/*
A constantly moving/updating CSI:
Flow:
  add RR values until enough values have been added: minium window size + median window size
  The next addRR will return a CSI value
  The next addRR will remove the first RR value used in the previous window add the new an calculate
  etc, etc.
  TBD: What to do if unacceptable high or low RR values are added? Skip and re-use from previous window or??
*/
public class MovingCsi {
    private int _winSize;

    private List<Double> _rrAcc = new ArrayList<Double>();

    public MovingCsi(final int csiWinSize,final int mediaWinSize) {
        _winSize = winSize;
    }

    public CsiResult addRR(double rr) {
        /*
            1) TBD: skip and return null if RR is unacceptably high or low
            2) 
        */

    }

    public static List<CsiResult> CalcCsi(double[] rr, int winSize) { // throws Exception {
        int numResults = rr.length - winSize;
        if (numResults <= 0)
            return null;

        int medianWindow = 5;
        int stop = rr.length - medianWindow;
        double[] cumSum = CumSum(rr);
        double[] medRR = Cfg.findMedian(rr, medianWindow);

        SimpleRegression simpleRegression = new SimpleRegression();

        List<Double> neighbourDiff = new ArrayList<Double>();
        List<Double> neighbourAdd = new ArrayList<Double>();
        List<Double> neighbourModDiff = new ArrayList<Double>();
        List<Double> neighbourModAdd = new ArrayList<Double>();
        for (int i = 1; i < rr.length; i++) {
            simpleRegression.addData(cumSum[i-1],medRR[i-1]);
            if (simpleRegression.getN()>100)
                simpleRegression.popFirst();

            double slope = simpleRegression.getSlope();

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
