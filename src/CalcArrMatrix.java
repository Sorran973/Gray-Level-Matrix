import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.max;

/**
 * Created by ArtemBulkhak on 21.03.18.
 */
public class CalcArrMatrix {

    private BufferedImage image;
    private final int grayLevel;
    private int[][] gtm;
    private int gtm_width;
    private int gtm_heigth;
    private int characteristic;
    private ArrayList<GLCM> arrGLCM;
    private double max = Double.MIN_VALUE;


    private ArrayList<GLCM> arrAutoCorrelation = new ArrayList<>();
    private ArrayList<GLCM> arrCorrelation = new ArrayList<>();
    private ArrayList<GLCM> arrEntropy = new ArrayList<>();
    private ArrayList<GLCM> arrProminence = new ArrayList<>();
    private ArrayList<GLCM> arrShadeClaster = new ArrayList<>();
    private ArrayList<GLCM> arrHomogeneity = new ArrayList<>();

    private ArrayList<GLCM> arrBorder = new ArrayList<>();



    public CalcArrMatrix(int grayLevel, File image, int charactestic) throws IOException {

        this.grayLevel = grayLevel;
        this.image = ImageIO.read(image);
        gtm_width = this.image.getWidth();
        gtm_heigth = this.image.getHeight();
        gtm = new int[gtm_width][gtm_heigth];
        this.characteristic = charactestic;
        createGTM();

        arrGLCM = new ArrayList<>();
        fillArrGLCM();
//        fillArrText();
    }

    public int getGrayLevel(){
        return this.grayLevel;
    }
    public int getGtm_width(){
        return this.gtm_width;
    }
    public int getGtm_heigth(){
        return this.gtm_heigth;
    }

    public ArrayList<GLCM> getArrGLCM(){
        return this.arrGLCM;
    }
    public ArrayList<GLCM> getArrHomogeneity(){
        return this.arrHomogeneity;
    }
    public ArrayList<GLCM> getArrAutoCorrelation(){
        return this.arrAutoCorrelation;
    }
    public double getMax(){
        return this.max;
    }


    public void createGTM() {
        for (int i = 0; i < gtm_width; i++) {
            for (int j = 0; j < gtm_heigth; j++) {
                Color rgb = new Color(image.getRGB(i, j));
                int newR = rgb.getRed();
                int newG = rgb.getGreen();
                int newB = rgb.getBlue();
                int gr = (newR + newG + newB) / 3;

                gtm[i][j] = gr * grayLevel / 255;
            }
        }
    }

    public void fillArrGLCM(){

        int m;
//        if (pixelOrRactagle){m = 1;} else m = grayLevel;
        for (int i = 0; i+grayLevel <= gtm_width; i++){
            for (int j = 0; j+grayLevel <= gtm_heigth; j++){

                //0°
                int[][] glcm0 = createGLCM(0, i, j);

                //45°
                int[][] glcm45 = createGLCM(45, i, j);

                //90°
                int[][] glcm90 = createGLCM(90, i, j);

                //135°
                int[][] glcm135 = createGLCM(135, i, j);
//                double[][] glcm0 = createGLCM(0, i, j);
//
//                //45°
//                double[][] glcm45 = createGLCM(45, i, j);
//
//                //90°
//                double[][] glcm90 = createGLCM(90, i, j);
//
//                //135°
//                double[][] glcm135 = createGLCM(135, i, j);

                double[][] averag_matrix = averagingMatrices(glcm0, glcm45, glcm90, glcm135);

                GLCM glcm = new GLCM(gtm_heigth, averag_matrix, grayLevel, i+grayLevel/2, j+grayLevel/2, characteristic);
                arrGLCM.add(glcm);
            }
        }

    }

    public int[][] createGLCM(int angle, int width, int height) {
//    public double[][] createGLCM(int angle, int width, int height) {
        int[][] temp = new int[grayLevel+1][grayLevel+1];

        int startRow = 0;
        int startColumn = 0;
        int endColumn = 0;

        boolean validAngle = true;
        switch (angle) {
            case 0:
                startRow = width;
                startColumn = height;
                endColumn = grayLevel+height-2;
                break;
            case 45:
                startRow = 1+width;
                startColumn = height;
                endColumn = grayLevel+height-2;
                break;
            case 90:
                startRow = 1+width;
                startColumn = height;
                endColumn = grayLevel+height-1;
                break;
            case 135:
                startRow = 1+width;
                startColumn = 1+height;
                endColumn = grayLevel+height-1;
                break;
            default:
                validAngle = false;
                break;
        }

        if (validAngle) {
            for (int i = startRow; i < grayLevel+width; i++) {
                for (int j = startColumn; j <= endColumn; j++) {
                    switch (angle) {
                        case 0:
                            temp[gtm[i][j]][gtm[i][j+1]]++;
                            break;
                        case 45:
                            temp[gtm[i][j]][gtm[i-1][j+1]]++;
                            break;
                        case 90:
                            temp[gtm[i][j]][gtm[i-1][j]]++;
                            break;
                        case 135:
                            temp[gtm[i][j]][gtm[i-1][j-1]]++;
                            break;
                    }
                }
            }
        }

        return add(temp, transposeMatrix(temp));

//        return normalizeMatrix(add(temp, transposeMatrix(temp)));
    }

    public int[][] transposeMatrix(int [][] m){
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < grayLevel+1; i++){
            for (int j = 0; j < grayLevel+1; j++){
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }

    public int[][] add(int [][] m2, int [][] m1){
        int[][] temp = new int[grayLevel+1][grayLevel+1];
        for (int i = 0; i < grayLevel+1; i++){
            for (int j = 0; j < grayLevel+1; j++){
                temp[j][i] = m1[i][j] + m2[i][j];
            }
        }
        return temp;
    }
    public double[][] add(double [][] m2, double [][] m1){
        double[][] temp = new double[grayLevel+1][grayLevel+1];
        for (int i = 0; i < grayLevel+1; i++){
            for (int j = 0; j < grayLevel+1; j++){
                temp[j][i] = m1[i][j] + m2[i][j];
            }
        }
        return temp;
    }

    public int getTotal(int [][] m){
        int temp = 0;
        for (int i = 0; i < grayLevel+1; i++){
            for (int j = 0; j < grayLevel+1; j++){
                temp += m[i][j];
            }
        }
        return temp;
    }

    public double[][] normalizeMatrix(int [][] m){
        double[][] temp = new double[grayLevel+1][grayLevel+1];
        int total = getTotal(m);
        for (int i = 0; i < grayLevel+1; i++){
            for (int j = 0; j < grayLevel+1; j++){
                temp[j][i] = (double) m[i][j] / total;
            }
        }
        return temp;
    }


    public double[][] averagingMatrices(int [][] m1, int [][] m2, int [][] m3, int [][] m4){
//    public double[][] averagingMatrices(double [][] m1, double [][] m2, double [][] m3, double [][] m4){
        double[][] temp = new double[grayLevel+1][grayLevel+1];

        int[][] m_add = add(add(add(m1, m2), m3), m4);
//        double[][] m_add = add(add(add(m1, m2), m3), m4);
        for (int i = 0; i < grayLevel+1; i++){
            for (int j = 0; j < grayLevel+1; j++){

                temp[j][i] = m_add[i][j] / 4;
            }
        }
        return temp;
    }

    public void getArrGLCMatrices(){

        int count = 1;
        for (GLCM glcm: arrGLCM){

            System.out.println(count + ")");
            for (int i = 0; i <= grayLevel; i++){
                for (int j = 0; j <= grayLevel; j++){
                    System.out.print(glcm.getGLCM(i, j) + " ");
                }
                System.out.println();
            }
            count++;
            System.out.println();
        }
    }

    public void fillArrText(){

        int count = 1;
        for (GLCM glcm: arrGLCM){

//            double correlation = glcm.getCorrelation();
//            double entropy = glcm.getEntropy();
//            double homogeneity = glcm.getHomogeneity();
//            double prominence = glcm.getProminence();
//            double shadeClaster = glcm.getShadeClaster();
            double autoCorrelation = glcm.getValueCharactestic();


//            if (!arrCorrelation.contains(correlation)) arrCorrelation.add(glcm);
//            if (!arrEntropy.contains(entropy)) arrEntropy.add(glcm);
//            if (homogeneity == 1) arrHomogeneity.add(glcm); else arrBorder.add(glcm);
//            if (!arrProminence.contains(prominence)) arrProminence.add(glcm);
//            if (!arrShadeClaster.contains(shadeClaster)) arrShadeClaster.add(glcm);
            if (!arrAutoCorrelation.contains(autoCorrelation)) {
                arrAutoCorrelation.add(glcm);
                if (autoCorrelation > max)
                    max = autoCorrelation;
            }



//            System.out.println(count + ")");
//
//            System.out.print("Contrast: " + glcm.getContrast());
//            System.out.print(" Correlation: " + glcm.getCorrelation());         //! сильно отличаются
//            System.out.print(" Energy: " + glcm.getEnergy());     // сильно отличаются
//            System.out.print(" Entropy: " + glcm.getEntropy());                 //! сильно отличаются
//            System.out.print(" Homogeneity: " + glcm.getHomogeneity());         //!
//            System.out.print(" Inertia: " + glcm.getInertia());
//            System.out.print(" Prominence: " + glcm.getProminence());           //! сильно отличаются
//            System.out.print(" ShadeClaster: " + glcm.getShadeClaster());       //! сильно отличаются
//            System.out.print(" AutoCorrelation: " + glcm.getAutoCorrelation()); //! сильно отличаются
//
//
//            count++;
//            System.out.println();
        }
    }
}

