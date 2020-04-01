/* *****************************************************************************
 *  Name: Marta Wisniewska
 *  Date: 29/03/2020
 *  Description: Stats for Percolation model
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONST_FOR_STATS = 1.96;
    private final double[] tresholdResults;
    private final double mean;
    private final double stdDev;
    private final double confidenceLo;
    private final double confidenceHi;
    private final int n;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int size, int trials) {
        n = size;
        tresholdResults = new double[trials];

        for (int i = 0; i < trials; i++) {
            double treshold = simulatePerculation();
            tresholdResults[i] = treshold;
        }
        mean = mean();
        stdDev = stddev();
        confidenceHi = confidenceHi();
        confidenceLo = confidenceLo();
    }

    private double simulatePerculation() {
        Percolation percolation = new Percolation(n);
        int openedCount = 0;

        while (!percolation.percolates()) {
            int col = StdRandom.uniform(1, n);
            int row = StdRandom.uniform(1, n);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
                openedCount++;
            }
        }

        return (openedCount)/Math.pow(n, 2);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(tresholdResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(tresholdResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONST_FOR_STATS * stdDev / Math.sqrt(n));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONST_FOR_STATS * stdDev / Math.sqrt(n));
    }

    // test client
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("mean = %f \n", stats.mean);
        System.out.printf("stddev = %f \n", stats.stdDev);
        StdOut.printf("%d%% confidence interval = [ %f, %f ] \n", 95, stats.confidenceLo, stats.confidenceHi);
    }
}
