import java.util.ArrayList;
import java.util.List;

/**
 * Created by ArtemBulkhak on 01.05.18.
 */
public class Cluster implements Comparable{

    private GLCM center;
    private List<GLCM> points;


    public Cluster(GLCM center) {
        this.center = center;
        points = new ArrayList<GLCM>();
        points.add(this.center);
    }

    public void addPoint(final GLCM point) {
        points.add(point);
    }

    public List<GLCM> getPoints() {
        return points;
    }

    public GLCM centroidOf(){

        int sum = 0;
        for (GLCM point: points){
            sum += point.getValueCharactestic();
        }

        double autoCorrelation = sum / points.size();
        double min = Integer.MAX_VALUE;
        int num = -1;

        for (int i = 0; i < points.size(); i++) {
            if (Math.abs(autoCorrelation - points.get(i).getValueCharactestic()) < min) {
                min = Math.abs(autoCorrelation - points.get(i).getValueCharactestic());
                num = i;
            }
        }
        return points.get(num);
    }

    public GLCM getCenter() {
        return center;
    }

    public void setCenter(GLCM center) {
        this.center = center;
    }


    @Override
    public int compareTo(Object o) {
        return (int) (this.center.getValueCharactestic() - ((Cluster)o).center.getValueCharactestic());
    }
}
