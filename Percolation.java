/* *****************************************************************************
 *  Name: Marta Wiśniewska
 *  Date: 25/02/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] blocked;
    private final int n;
    private final WeightedQuickUnionUF weightedQuickUnionUF;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int size) {
        if (size < 1) {
            throw new IllegalArgumentException();
        } else {
            n = size;
            weightedQuickUnionUF = new WeightedQuickUnionUF(n * n);
            blocked = new boolean[n * n];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    int i = (r * n) + c;
                    blocked[i] = true;
                }
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validArgument(row) || !validArgument(col)) {
            throw new IllegalArgumentException();
        } else {
            if (!isOpen(row, col)) {
                int id = getId(row, col);
                if (id > -1 && id < n * n) {
                    blocked[id] = false;
                    weightedQuickUnionUF.union(id, id);
                    unionNeighours(row, col, id);
                }
            }
        }
    }

    private boolean validArgument(int x) {
        return x >= 0 && x <= n;
    }

    private int getId(int row, int col) {
        return (row-1) * n + (col-1);
    }

    private void unionNeighours(int row, int col, int id) {
        int[][] casesToCheck;
        casesToCheck = new int[][]{{row-1, col}, {row+1, col}, {row, col-1}, {row, col+1}};
        for (int i = 0; i < casesToCheck.length; i++) {
            int neighbourId = getId(casesToCheck[i][0], casesToCheck[i][1]);
            if (neighbourId > -1 && neighbourId < n*n && isOpen(casesToCheck[i][0], casesToCheck[i][1])) {
                weightedQuickUnionUF.union(id, neighbourId);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validArgument(row) || !validArgument(col)) {
            throw new IllegalArgumentException();
        } else {
            return !blocked[getId(row, col)];
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validArgument(row) || !validArgument(col)) {
            throw new IllegalArgumentException();
        } else {
            int id = getId(row, col);
            for (int j = 0; j < n; j++) {
                if (weightedQuickUnionUF.connected(id, j) && isOpen(row, col)) {
                    return true;
                }
            }
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (isOpen(i, j)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int c = 1; c <= n; c++) {
            if (isFull(n, c)) {
                return true;
            }
        }
        return false;
    }
}