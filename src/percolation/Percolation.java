package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private enum states {
        OPEN, FULL, BLOCKED
    }
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private states[] siteState;
    private int numberOfOpenSize = 0;
    private int totalSize;
    private final int TOP_VIRTUAL_SITE;
    private final int BOTTOM_VIRTUAL_SITE;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be a positive number");
        }
        initializeAllStatesToBlock(n);
        totalSize = n;
        TOP_VIRTUAL_SITE = totalSize * totalSize;
        BOTTOM_VIRTUAL_SITE = (totalSize * totalSize) + 1;
        weightedQuickUnionUF = new WeightedQuickUnionUF(n*n + 2);
        connectVirtualTrees();
    }

    private void connectVirtualTrees() {
        for (int i = 1; i <= totalSize; i++) {
            int currentRowSiteIndex = calculateIndex(1, i);
            weightedQuickUnionUF.union(currentRowSiteIndex, TOP_VIRTUAL_SITE);
        }
        for (int j = 1 ; j <= totalSize; j++) {
            int currentRowSiteIndex = calculateIndex(totalSize, j);
            weightedQuickUnionUF.union(currentRowSiteIndex, BOTTOM_VIRTUAL_SITE);
        }
    }

    public void open(int row, int col) {
        boolean isValidRow = row >= 1 && row <= totalSize;
        boolean isValidColumn = col >= 1 && col <= totalSize;
        if (!isValidRow || !isValidColumn) {
            throw new IllegalArgumentException("row and column must be n valid range");
        }
        int siteIndex = calculateIndex(row, col);
        states currentState = siteState[siteIndex];
        if (currentState.equals(states.BLOCKED)) {
            numberOfOpenSize++;
            siteState[siteIndex] = states.OPEN;
            int firstCandidateRow = row;
            int firstCandidateCol = col + 1;
            processCandidate(firstCandidateRow, firstCandidateCol, siteIndex);
            int secondCandidateRow = row;
            int secondCandidateCol = col - 1;
            processCandidate(secondCandidateRow, secondCandidateCol, siteIndex);
            int thirdCandidateRow = row + 1;
            int thirdCandidateCol = col;
            processCandidate(thirdCandidateRow, thirdCandidateCol, siteIndex);
            int fourthCandidateRow = row - 1;
            int fourthCandidateCol = col;
            processCandidate(fourthCandidateRow, fourthCandidateCol, siteIndex);
        }
    }

    public boolean isOpen(int row, int col) {
        boolean isValidRow = row >= 1 && row <= totalSize;
        boolean isValidColumn = col >= 1 && col <= totalSize;
        if (!isValidRow || !isValidColumn) {
            throw new IllegalArgumentException("row and column must be n valid range");
        }
        int siteIndex = calculateIndex(row, col);
        states currentState = siteState[siteIndex] ;
        if (currentState.equals(states.OPEN)) {
            return true;
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        boolean isValidRow = row >= 1 && row <= totalSize;
        boolean isValidColumn = col >= 1 && col <= totalSize;
        if (!isValidRow || !isValidColumn) {
            throw new IllegalArgumentException("row and column must be n valid range");
        }
        int siteIndex = calculateIndex(row, col);
        states currentState = siteState[siteIndex];
        boolean isFullCaseOne = currentState.equals(states.OPEN) &&
                weightedQuickUnionUF.connected(siteIndex, TOP_VIRTUAL_SITE);
        boolean isFullCaseTwo = currentState.equals(states.OPEN) &&
                (weightedQuickUnionUF.connected(siteIndex, TOP_VIRTUAL_SITE) ||
                        weightedQuickUnionUF.connected(siteIndex, BOTTOM_VIRTUAL_SITE)
                ) && percolates();
        if (isFullCaseOne || isFullCaseTwo) {
            return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSize;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(TOP_VIRTUAL_SITE, BOTTOM_VIRTUAL_SITE)
                && numberOfOpenSize > 0;
    }

    private int calculateIndex(int row, int col) {
        return ((row * totalSize) - (totalSize - col)) - 1;
    }

    private void initializeAllStatesToBlock(int n) {
        siteState = new states[n*n];
        for (int i = 0; i < n*n; i++) {
            siteState[i] = states.BLOCKED;
        }
    }

    private boolean isValidCoordinates(int row, int col) {
        boolean isRowValid = row >= 1 && row <= totalSize;
        boolean isColValid = col >= 1 && col <= totalSize;
        return isRowValid && isColValid;
    }

    private void processCandidate(int row, int col, int siteIndex) {
        if (isValidCoordinates(row, col)) {
            int candidateIndex = calculateIndex(row, col);
            states firstCandidateState = siteState[candidateIndex];
            if (firstCandidateState.equals(states.OPEN) || firstCandidateState.equals(states.FULL)) {
                weightedQuickUnionUF.union(candidateIndex, siteIndex);
            }
        }
    }
}
