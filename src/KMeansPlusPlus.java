//import org.apache.commons.math3.ml.clustering.Cluster;
//import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.*;

/**
 * Created by ArtemBulkhak on 29.04.18.
 */

class KMeansPlusPlus {

    private final Random random;

    public KMeansPlusPlus(final Random random) {
        this.random = random;
    }

    public ArrayList<Cluster> cluster(ArrayList<GLCM> points, final int k) {

        // create the initial clusters
        ArrayList<Cluster> clusters = chooseInitialCenters(points, k, random);
        assignPointsToClusters(clusters, points);

        // iterate through updating the centers until we're done
        final int max = Integer.MAX_VALUE;
        for (int count = 0; count < max; count++) {
            boolean clusteringChanged = false;
            ArrayList<Cluster> newClusters = new ArrayList<>();

            for (Cluster cluster : clusters) {
                GLCM newCenter = cluster.centroidOf();

                if (newCenter.getValueCharactestic() != (cluster.getCenter().getValueCharactestic())) {
                    clusteringChanged = true;
                }
                newClusters.add(new Cluster(newCenter));
            }
            if (!clusteringChanged) {
                Collections.sort(clusters);
                return clusters;
            }
            assignPointsToClusters(newClusters, points);
            clusters = newClusters;
        }
        Collections.sort(clusters);
        return clusters;
    }

    private static void assignPointsToClusters(ArrayList<Cluster> clusters, ArrayList<GLCM> points) {

        for (GLCM p : points) {
            Cluster cluster = getNearestCluster(clusters, p);
            cluster.addPoint(p);
        }
    }

    private static ArrayList<Cluster> chooseInitialCenters(ArrayList<GLCM> points, final int k, final Random random) {

        ArrayList<Cluster> resultSet = new ArrayList<>();

        // Choose one center uniformly at random from among the data points.
        GLCM firstPoint = points.remove(random.nextInt(points.size()));
        resultSet.add(new Cluster(firstPoint));

        double[] dx2 = new double[points.size()];
        while (resultSet.size() < k) {
            // For each data point x, compute D(x), the distance between x and
            // the nearest center that has already been chosen.
            int sum = 0;

            for (int i = 0; i < points.size(); i++) {
                GLCM p = points.get(i);
                Cluster nearest = getNearestCluster(resultSet, p);
                double d = Math.abs(nearest.getCenter().getValueCharactestic() - p.getValueCharactestic());
                sum += d * d;
                dx2[i] = sum;
            }

            // Add one new data point as a center. Each point x is chosen with
            // probability proportional to D(x)2
            double r = random.nextDouble() * sum;
            for (int i = 0 ; i < dx2.length; i++) {
                if (dx2[i] >= r) {
                    GLCM p = points.remove(i);
                    resultSet.add(new Cluster(p));
                    break;
                }
            }
        }

        return resultSet;
    }


    private static Cluster getNearestCluster(ArrayList<Cluster> clusters, GLCM point) {

        double minDistance = Double.MAX_VALUE;
        Cluster minCluster = null;

        for (Cluster c : clusters) {
            double distance = Math.abs(c.getCenter().getValueCharactestic() - point.getValueCharactestic());

            if (distance < minDistance) {
                minDistance = distance;
                minCluster = c;
            }
        }
        return minCluster;
    }

}


