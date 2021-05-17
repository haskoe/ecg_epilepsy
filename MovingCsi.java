package com.example.epicsvread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
A real-time moving CSI calculator:
Flow:
  The addRR method is called consecutively
  When enough RR values have been added to calculate a median the first RR median is calculated and stored in _rrMedAcc 
  When addRR is called next we can calculate and store all neighbour adds and diffs because we now have two RR median values
  When enough neighbour adds and diffs are available CSI and modCSI can be calculated and returned from addRR 
  and the first values in the lists can be abandoned as they are no longer needed for the calculations
  TBD: What to do if unacceptable high or low RR values are added? Skip and re-use from previous window or??
*/
public class MovingCsi {
    private int _winSize;
    private int _medianHalfSize;
    private int _medianWinSize;
    private static final double _fact = Math.sqrt(2) / 2;

    private SimpleRegression _simpleRegression;

    // below are the lists of values needed for the various ingtermediate calculations
    // values are added to the end of the lists 
    // and removed at the start of the lists when a value is no longer needed  
    // 
    // list of incoming RR values.  
    private List<Double> _rrAcc = new ArrayList<Double>();
    // list of calculated RR median values.  
    private List<Double> _rrMedAcc = new ArrayList<Double>();
    // Lists of added and diffed RR and median RR neigbours 
    private List<Double> neighbourDiff = new ArrayList<Double>();
    private List<Double> neighbourAdd = new ArrayList<Double>();
    private List<Double> neighbourModDiff = new ArrayList<Double>();
    private List<Double> neighbourModAdd = new ArrayList<Double>();

    // constructor
    // csiWinSize: Window size for the CSI calculation, i.e. 30, 50, 100
    // medianHalfSize: Number of elements to the left/right of the middle point in the median window
    public MovingCsi(final int csiWinSize, final int medianHalfSize) {
        _winSize = csiWinSize;
        _medianHalfSize = medianHalfSize;

        _medianWinSize = (2 * medianHalfSize + 1);

        // instantiate the SimpleRegression object for the slope calculation
        _simpleRegression = new SimpleRegression();
    }

    public CsiResult addRR(double rr) {
        CsiResult result = null;

        // add incoming rr to list of rr values
        _rrAcc.add(rr);

        if (_rrAcc.size() >= _medianWinSize) { // we now have enough rr values to calculate a median

            // create list of RR values for calculation of the median - the last median window size values on _rrAcc
            List<Double> medianWindow = _rrAcc.subList(_rrAcc.size() - _medianWinSize, _rrAcc.size());

            // calculat median and add value to list _rrMedAcc 
            double median = calcMedian(medianWindow, _medianHalfSize);
            _rrMedAcc.add(median); 

            // the x in the slope calculation is the RR value in the middle of the median window
            double x = _rrAcc.get(_rrAcc.size() - _medianHalfSize - 1);

            // the if below ensures that x i "cumsummed"
            if (_simpleRegression.xl.size() > 0) {
                _simpleRegression.addData(x + _simpleRegression.xl.get(_simpleRegression.xl.size() - 1), median);
            } else {
                _simpleRegression.addData(x, median);
            }

            if (_rrMedAcc.size() > 1) {
                // With two or more median values neighbour adds and diffs can be calculated  
                int iMed = _rrMedAcc.size() - 1;
                // the neighbour diff and add can be calculated

                // the neighbour diff/add for modCSI - uses median values
                neighbourModDiff.add(_rrMedAcc.get(iMed - 1) - _rrMedAcc.get(iMed));
                neighbourModAdd.add(_rrMedAcc.get(iMed - 1) + _rrMedAcc.get(iMed));

                // the neighbour diff/add for CSI - uses raw RR values
                int i = iMed + _medianHalfSize; // use neighbour pairs in the middle of the median window
                neighbourDiff.add(_rrAcc.get(i - 1) - _rrAcc.get(i));
                neighbourAdd.add(_rrAcc.get(i - 1) + _rrAcc.get(i));

                if (neighbourModDiff.size() > _winSize) { // first value in the list is no longer needed and is removed
                    // remove the first values in moving list
                    neighbourAdd.remove(0);
                    neighbourDiff.remove(0);
                    neighbourModAdd.remove(0);
                    neighbourModDiff.remove(0);
                    _rrMedAcc.remove(0);
                    _rrAcc.remove(0);
                }

                if (neighbourModDiff.size() >= _winSize) { // Enough values for calculation of CSI and modCSI
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
