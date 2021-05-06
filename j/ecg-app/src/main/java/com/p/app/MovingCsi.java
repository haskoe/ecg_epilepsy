package com.p.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
A real-time moving CSI calculator:
Flow:
  add RR values until enough values have been added: minium window size + median window size
  The next addRR will return a CSI value
  The next addRR will remove the first RR value used in the previous window add the new an calculate
  etc, etc.
  TBD: What to do if unacceptable high or low RR values are added? Skip and re-use from previous window or??
*/
public class MovingCsi {
    private int _winSize;
    private int _medianHalfSize;
    private int _medianWinSize;
    private static final double _fact = Math.sqrt(2) / 2;

    private SimpleRegression _simpleRegression;

    private List<Double> _rrAcc = new ArrayList<Double>();
    private List<Double> _rrMedAcc = new ArrayList<Double>();
    private List<Double> neighbourDiff = new ArrayList<Double>();
    private List<Double> neighbourAdd = new ArrayList<Double>();
    private List<Double> neighbourModDiff = new ArrayList<Double>();
    private List<Double> neighbourModAdd = new ArrayList<Double>();

    public MovingCsi(final int csiWinSize, final int medianHalfSize) {
        _winSize = csiWinSize;
        _medianHalfSize = medianHalfSize;

        _medianWinSize = (2 * medianHalfSize + 1);

        _simpleRegression = new SimpleRegression();
    }

    public CsiResult addRR(double rr) {
        CsiResult result = null;

        _rrAcc.add(rr);
        if (_rrAcc.size() >= _medianWinSize) {
            // the median can be calculated
            List<Double> medianWindow = _rrAcc.subList(_rrAcc.size() - _medianWinSize, _rrAcc.size());
            double median = calcMedian(medianWindow, _medianHalfSize);
            _rrMedAcc.add(median);

            double x = _rrAcc.get(_rrAcc.size() - _medianHalfSize - 1);
            if (_simpleRegression.xl.size() > 0) {
                _simpleRegression.addData(x + _simpleRegression.xl.get(_simpleRegression.xl.size() - 1), median);
            } else {
                _simpleRegression.addData(x, median);
            }

            if (_rrMedAcc.size() > 1) {
                int iMed = _rrMedAcc.size() - 1;
                // the neighbour diff and add can be calculated
                neighbourModDiff.add(_rrMedAcc.get(iMed - 1) - _rrMedAcc.get(iMed));
                neighbourModAdd.add(_rrMedAcc.get(iMed - 1) + _rrMedAcc.get(iMed));

                int i = iMed + _medianHalfSize;
                neighbourDiff.add(_rrAcc.get(i - 1) - _rrAcc.get(i));
                neighbourAdd.add(_rrAcc.get(i - 1) + _rrAcc.get(i));

                if (neighbourModDiff.size() > _winSize) {
                    // remove the first values in moving list
                    neighbourAdd.remove(0);
                    neighbourDiff.remove(0);
                    neighbourModAdd.remove(0);
                    neighbourModDiff.remove(0);
                    double slope1 = _simpleRegression.getSlope();
                    _simpleRegression.popFirst();
                    _rrMedAcc.remove(0);
                    _rrAcc.remove(0);
                }

                if (neighbourModDiff.size() >= _winSize) {
                    result = new CsiResult();
                    // the CSI stddev's can be calculated
                    double sd1Mod = calcSD(neighbourModDiff);
                    double sd2Mod = calcSD(neighbourModAdd);
                    double modCsi = _fact * 4 * sd2Mod * sd2Mod / sd1Mod;
                    result.ModCsi = modCsi;

                    double sd1 = calcSD(neighbourDiff);
                    double sd2 = calcSD(neighbourAdd);

                    double slope = _simpleRegression.getSlope();
                    double csi = _fact * sd2 / sd1; // * slope;
                    result.Csi = csi;
                }
            }
        }
        return result;
    }

    public static double calcSD(List<Double> numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for (Double num : numArray) {
            sum += num;
        }

        double mean = sum / length;

        for (double num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public static double calcMedian(List<Double> lst, int medianHalfSize) {
        Double[] arr = new Double[lst.size()];
        lst.toArray(arr);
        Arrays.sort(arr);
        return arr[medianHalfSize];
    }

    public static List<Double> readCsv(final String fileName) {
        List<Double> temp = new ArrayList<Double>();

        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String str;
            while ((str = in.readLine()) != null) {
                temp.add(Double.parseDouble(str));
            }
        } catch (IOException e) {
            System.out.println("File Read Error");
        }
        return temp;
    }
}
