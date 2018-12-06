import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by ArtemBulkhak on 21.03.18.
 */


public class Main {

    private static int GRAY_LEVEL = 3;
    private static String IMAGE = "/Users/admin/IdeaProjects/GrayLevelMatrix/66.png";
    private static int NUM_OF_CLUSTERS = 2;
    private static int CHARACTERISTIC = 3;
    private static boolean COLOR = false;
    private static boolean PIXEL_OR_RECTANGLE = true;


    public static void main(String[] arg) throws IOException {

        CalcArrMatrix calcArrMatrix = new CalcArrMatrix(GRAY_LEVEL, new File(IMAGE), CHARACTERISTIC);

        Random random = new Random();

        KMeansPlusPlus kMeansPlusPlus = new KMeansPlusPlus(random);

        Drawer drawer = new Drawer(PIXEL_OR_RECTANGLE, COLOR,
                                        calcArrMatrix,
                                            kMeansPlusPlus.cluster(calcArrMatrix.getArrGLCM(), NUM_OF_CLUSTERS));




    }
}
