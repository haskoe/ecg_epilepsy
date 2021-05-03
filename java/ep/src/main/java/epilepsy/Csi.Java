package epilepsy;

public class Csi {
    // public static CsiResult[] CalcCsi(double[] rr, int winSize) throws Exception {
    //     int numResults = winSize - rr.length;
    //     if (numResults<=0)
    //         return null;
    // }

    public static List<CsiResult> CalcCsi(double[] rr, int winSize) throws Exception {
        int numResults = rr.length - winSize;
        if (numResults <= 0)
            return null;

        List<Double> neighbourDiff = new ArrayList<Double>();
        List<Double> neighbourAdd = new ArrayList<Double>();
        for (int i = 1; i < rr.length; i++) {
            neighbourDiff.add(rr[i - 1] - rr[i]);
            neighbourAdd.add(rr[i - 1] + rr[i]);
        }
        
        return null;
    }
}
