/**
 * Created by ArtemBulkhak on 20.04.18.
 */
public class GLCM {

    private double valueCharactestic;
    private double[][] glcm;
    private int grayLevel;
    private int x;
    private int y;

    private double contrast;
    private double homogeneity;
    private double entropy;
    private double energy;
    private double inertia;
    private double shadeClaster;
    private double prominence;
    private double correlation;

    private double meanX;
    private double meanY;
    private double stdevX;
    private double stdevY;


    public GLCM(int heigth, double[][] averag_matrix, int grayLevel, int x, int y, int c){
        this.x = x;
        this.y = heigth - y;
        this.grayLevel = grayLevel;
        glcm = averag_matrix;
//        doBasicStats();
        valueCharactestic = calcСharacteristic(c);
    }

    public double getGLCM(int i, int j){
        return glcm[i][j];
    }

    public double getValueCharactestic(){
        return valueCharactestic;
    }


    public double getHomogeneity() {
        int homogeneity = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                homogeneity += ((1 / (1 + (Math.pow(i - j, 2)))) * glcm[i][j]);

        return homogeneity;
    }

    public double getContrast() {
        int contrast = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                contrast += glcm[i][j] * Math.abs(i - j);

        return contrast;
    }

    public double getEntropy() {
        int entropy = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++) {
                if (glcm[i][j] != 0)
                    entropy += (glcm[i][j] * Math.log10(glcm[i][j])) * -1;
            }

        return entropy;
    }

    public double getEnergy() {
        int energy = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                energy += Math.pow(glcm[i][j], 2);

        return energy;
    }

    public double getInertia() {
        inertia = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                inertia += glcm[i][j] * Math.abs(i - j);

        return inertia;
    }

    public double getShadeClaster(){
        shadeClaster = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                shadeClaster += (Math.pow((i + j - meanX - meanY), 3) * glcm[i][j]);

        return shadeClaster;
    }

    public double getProminence(){
        prominence = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                prominence += (Math.pow((i + j - meanX - meanY), 4) * glcm[i][j]);

        return prominence;
    }

    public double getCorrelation(){
        correlation = 0;
        if (stdevX != 0 && stdevY != 0) {

            for (int i = 0; i < grayLevel + 1; i++)
                for (int j = 0; j < grayLevel + 1; j++)
                    correlation += ((((i - meanX) * (j - meanY)) / (stdevX * stdevY)) * glcm[i][j]);

        }
          return correlation;
    }

    public double getAutoCorrelation(){
        int autoCorrelation = 0;
        for (int i = 0; i < grayLevel + 1; i++)
            for (int j = 0; j < grayLevel + 1; j++)
                autoCorrelation += ((i* j) * glcm[i][j]);
        return autoCorrelation;
    }

    public double calcСharacteristic(int c) {

        switch (c) {
            case 1:
                return getHomogeneity();
            case 2:
                return getContrast();
            case 3:
                return getEntropy();
            case 4:
                return getEnergy();
            case 5:
                return getAutoCorrelation();
        }
        return 0;
    }
//        autoCorrelation = 0;
//        for (int i = 0; i < grayLevel + 1; i++)
//            for (int j = 0; j < grayLevel + 1; j++)
//                autoCorrelation += ((i* j) * glcm[i][j]);
//

//        homogeneity = 0;
//        for (int i = 0; i < grayLevel + 1; i++)
//            for (int j = 0; j < grayLevel + 1; j++)
//                autoCorrelation += ((1 / (1 + (Math.pow(i - j, 2)))) * glcm[i][j]);


//        contrast = 0;
//        for (int i = 0; i < grayLevel + 1; i++)
//            for (int j = 0; j < grayLevel + 1; j++)
//                autoCorrelation += glcm[i][j] * Math.abs(i - j);


//        entropy = 0; хорошо справляется имено с сегментацией поверхностей
//        for (int i = 0; i < grayLevel + 1; i++)
//            for (int j = 0; j < grayLevel + 1; j++) {
//                if (glcm[i][j] != 0)
//                    autoCorrelation += (glcm[i][j] * Math.log10(glcm[i][j])) * -1;
//            }
//

//        energy = 0; надо разобраться!!! на 140 справился неплохо
//        for (int i = 0; i < grayLevel + 1; i++)
//            for (int j = 0; j < grayLevel + 1; j++)
//                autoCorrelation += Math.pow(glcm[i][j], 2);
//////
////
//                return autoCorrelation;
//
//
//
//    }


    public void doBasicStats(){

        double [] px = new double [grayLevel + 1];
        double [] py = new double [grayLevel + 1];

        meanX = 0;
        meanY = 0;
        stdevX = 0;
        stdevY = 0;

        // Px(i) and Py(j) are the marginal-probability matrix; sum rows (px) or columns (py)
        // First, initialize the arrays to 0
        for (int i = 0;  i < grayLevel + 1; i++) {
            px[i] = 0.0;
            py[i] = 0.0;
        }

        // sum the glcm rows to Px(i)
        for (int i = 0;  i < grayLevel + 1; i++) {
            for (int j = 0; j < grayLevel + 1; j++) {
                px[i] += glcm[i][j];
            }
        }

        // sum the glcm rows to Py(j)
        for (int j = 0;  j < grayLevel + 1; j++) {
            for (int i = 0; i < grayLevel + 1; i++) {
                py[j] += glcm[i][j];
            }
        }

        // calculate meanx and meany
        for (int i = 0;  i < grayLevel + 1; i++) {
            meanX += (i*px[i]);
            meanY += (i*py[i]);
        }

        // calculate stdevx and stdevy
        for (int i = 0;  i < grayLevel + 1; i++) {
            stdevX += ((Math.pow((i-meanX),2))*px[i]);
            stdevY += ((Math.pow((i-meanY),2))*py[i]);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
