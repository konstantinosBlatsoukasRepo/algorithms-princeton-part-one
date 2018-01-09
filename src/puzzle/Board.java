package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by kon on 29/10/2017.
 */
public class Board {

    private final int[][] blocks;
    private int columnOfZeroValue;
    private int rowOfZeroValue;
    private boolean twinGenrated;
    private int[][] freshBlocks;
    private Board twinBoard;

    public Board(int[][] blocks) {
        this.blocks = copy(blocks);
    }

    private int[][] copy(int[][] blocks) {
        int[][] copy = new int[blocks.length][blocks.length];
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length; col++)
                copy[row][col] = blocks[row][col];
        return copy;
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        int initialHammingScore = 0;
        int expectedValueAdder = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                int valueInPlace = j + expectedValueAdder;
                int currentBlockValue = blocks[i][j];
                if (isBlockOutOfPlace(currentBlockValue, valueInPlace)) {
                    initialHammingScore++;
                }
            }
            expectedValueAdder += dimension();
        }
        return initialHammingScore;
    }

    private boolean isBlockOutOfPlace(int blockValue, int valueInPlace) {
        return blockValue != 0 && blockValue != valueInPlace;
    }

    public int manhattan() {
        int expectedValueAdder = 1;
        int manhattanMoves = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                int valueInPlace = j + expectedValueAdder;
                int currentBlockValue = blocks[i][j];
                if (currentBlockValue != valueInPlace && currentBlockValue != 0) {
                    int moves = movesToReachTheDesiredBlock(currentBlockValue, i, j, expectedValueAdder);
                    manhattanMoves += moves;
                }
            }
            expectedValueAdder += dimension();
        }
        return manhattanMoves;
    }

    private int movesToReachTheDesiredBlock(int desiredValue, int currentRow,
                                            int currentCollumn, int expectedValueAdder) {
        int totalMoves = 0;
        if (isDesiredValueInRange(expectedValueAdder, desiredValue)) {
            if (expectedValueAdder + currentCollumn == desiredValue) {
                return totalMoves;
            } else if (expectedValueAdder + currentCollumn > desiredValue) {
                while (expectedValueAdder + currentCollumn != desiredValue) {
                    currentCollumn--;
                    totalMoves++;
                }
                return totalMoves;
            }
            while (expectedValueAdder + currentCollumn != desiredValue) {
                currentCollumn++;
                totalMoves++;
            }
        } else if (isAfterTheCurrentRange(expectedValueAdder, desiredValue)) {
            while (!isDesiredValueInRange(expectedValueAdder, desiredValue)) {
                expectedValueAdder += dimension();
                currentRow++;
                totalMoves++;
            }
            totalMoves += calculateMovesInTheRow(expectedValueAdder, currentCollumn, desiredValue);
        } else {
            while (!isDesiredValueInRange(expectedValueAdder, desiredValue)) {
                expectedValueAdder -= dimension();
                currentRow--;
                totalMoves++;
            }
            totalMoves += calculateMovesInTheRow(expectedValueAdder, currentCollumn, desiredValue);
        }
        return totalMoves;
    }

    private int calculateMovesInTheRow(int expectedValueAdder, int currentCollumn, int desiredValue) {
        int rowMoves = 0;
        if (expectedValueAdder + currentCollumn == desiredValue) {
            return rowMoves;
        } else if (expectedValueAdder + currentCollumn > desiredValue) {
            while (expectedValueAdder + currentCollumn != desiredValue) {
                currentCollumn--;
                rowMoves++;
            }
            return rowMoves;
        }
        while (expectedValueAdder + currentCollumn != desiredValue) {
            currentCollumn++;
            rowMoves++;
        }
        return rowMoves;
    }

    private boolean isDesiredValueInRange(int expectedValueAdder, int desiredValue) {
        int from = expectedValueAdder;
        int to = expectedValueAdder + dimension() - 1;
        return from <= desiredValue && desiredValue <= to;
    }

    private boolean isAfterTheCurrentRange(int expectedValueAdder, int desiredValue) {
        int to = expectedValueAdder + dimension() - 1;
        return desiredValue > to;
    }

    public boolean isGoal() {
        int [][] goal = new int[blocks.length][blocks.length];
        int currentValue = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                goal[i][j] = currentValue;
                currentValue++;
            }
        }
        goal[blocks.length - 1][blocks.length - 1] = 0;
        Board boardGoal = new Board(goal);
        return this.toString().equals(boardGoal.toString());
    }

    public Board twin() {
        if(!twinGenrated) {
            twinGenrated = true;
            int firstRandomRowIndex = StdRandom.uniform(blocks.length);
            int firstRandomCollumnIndex = StdRandom.uniform(blocks.length);
            int secondRandomRowIndex = StdRandom.uniform(blocks.length);
            int secondRandomCollumnIndex = StdRandom.uniform(blocks.length);
            while (!isTwoPointsDifferentToZero(firstRandomRowIndex, firstRandomCollumnIndex, secondRandomRowIndex, secondRandomCollumnIndex) ||
                    isTheSamePoint(firstRandomRowIndex, firstRandomCollumnIndex, secondRandomRowIndex, secondRandomCollumnIndex)) {
                firstRandomRowIndex = StdRandom.uniform(blocks.length);
                firstRandomCollumnIndex = StdRandom.uniform(blocks.length);
                secondRandomRowIndex = StdRandom.uniform(blocks.length);
                secondRandomCollumnIndex = StdRandom.uniform(blocks.length);
            }
            int[][] freshBlocks = new int[blocks.length][blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks.length; j++) {
                    freshBlocks[i][j] = blocks[i][j];
                }
            }
            int temp = freshBlocks[firstRandomRowIndex][firstRandomCollumnIndex];
            freshBlocks[firstRandomRowIndex][firstRandomCollumnIndex] = freshBlocks[secondRandomRowIndex][secondRandomCollumnIndex];
            freshBlocks[secondRandomRowIndex][secondRandomCollumnIndex] = temp;
            twinBoard = new Board(freshBlocks);
        }
        return twinBoard;
    }

    private boolean isTwoPointsDifferentToZero(int firstRandomRowIndex , int firstRandomCollumnIndex ,
                                               int secondRandomRowIndex , int secondRandomCollumnIndex) {
        boolean isFirstPointDifferentToZero = blocks[firstRandomRowIndex][firstRandomCollumnIndex] != 0;
        boolean isSecondPointDifferentToZero = blocks[secondRandomRowIndex][secondRandomCollumnIndex] != 0;
        return isFirstPointDifferentToZero && isSecondPointDifferentToZero;
    }

    private boolean isTheSamePoint(int firstRandomRowIndex, int firstRandomCollumnIndex,
                                   int secondRandomRowIndex, int secondRandomCollumnIndex) {
        return firstRandomRowIndex == secondRandomRowIndex && firstRandomCollumnIndex == secondRandomCollumnIndex;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board boardToBeCompared = (Board) y;
        return this.toString().equals(boardToBeCompared.toString());
    }

    public Iterable<Board> neighbors() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    columnOfZeroValue = j;
                    rowOfZeroValue = i;
                }
            }
        }
        Stack<Board> neighbors = new Stack<>();
        int firstCandidateRow = rowOfZeroValue;
        int firstCandidateCol = columnOfZeroValue + 1;
        if (isValidTableIndices(firstCandidateRow, firstCandidateCol)) {
            Board neighbor = copyBlocksWithExchange(rowOfZeroValue, columnOfZeroValue,
                    firstCandidateRow, firstCandidateCol);
            neighbors.push(neighbor);
        }
        int secondCandidateRow = rowOfZeroValue;
        int secondCandidateCol = columnOfZeroValue - 1;
        if (isValidTableIndices(secondCandidateRow, secondCandidateCol)) {
            Board neighbor = copyBlocksWithExchange(rowOfZeroValue, columnOfZeroValue,
                    secondCandidateRow, secondCandidateCol);
            neighbors.push(neighbor);
        }
        int thirdCandidateRow = rowOfZeroValue + 1;
        int thirdCandidateCol = columnOfZeroValue;
        if (isValidTableIndices(thirdCandidateRow, thirdCandidateCol)) {
            Board neighbor = copyBlocksWithExchange(rowOfZeroValue, columnOfZeroValue,
                    thirdCandidateRow, thirdCandidateCol);
            neighbors.push(neighbor);
        }
        int fourthCandidateRow = rowOfZeroValue - 1;
        int fourthCandidateCol = columnOfZeroValue;
        if (isValidTableIndices(fourthCandidateRow, fourthCandidateCol)) {
            Board neighbor = copyBlocksWithExchange(rowOfZeroValue, columnOfZeroValue,
                    fourthCandidateRow, fourthCandidateCol);
            neighbors.push(neighbor);
        }
        return neighbors;
    }

    private boolean isValidTableIndices(int row, int col) {
        boolean isRowValid = row >= 0 && row <= blocks.length - 1;
        boolean isColValid = col >= 0 && col <= blocks.length - 1;
        return isRowValid && isColValid;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(blocks.length + "\n");
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private Board copyBlocksWithExchange(int row, int column, int exRow, int exColumn) {
        int[][] copyBlock = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                copyBlock[i][j] = blocks[i][j];
            }
        }
        int temp = copyBlock[exRow][exColumn];
        copyBlock[exRow][exColumn] = copyBlock[row][column];
        copyBlock[row][column] = temp;
        Board copyBoard = new Board(copyBlock);
        return copyBoard;
    }

    public static void main(String[] args) {
        In in = new In("puzzle3x3-16.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        initial.isGoal();
        System.out.println("Dimension: " + initial.dimension());
        System.out.println("Hamming score: " + initial.hamming());
        System.out.println("Manhattan score: " + initial.manhattan());
        System.out.println(initial.toString());
        Stack<Board> neighbors = (Stack<Board>) initial.neighbors();
        for (Board neighbor:neighbors) {
            System.out.println(neighbor.toString());
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(initial.twin().toString());
        }
    }
}
