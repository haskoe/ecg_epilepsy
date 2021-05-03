package epilepsy;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        double[] rr = new double[99];
        List<CsiResult> csi = Csi.CalcCsi(rr, 100);
    }
}
