import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class PageRankMatrix {
    private int amount;
    private BigDecimal[][] LMatrix;
    private BigDecimal[][] LTMatrix;
    private BigDecimal[] aMatrix;
    private BigDecimal[] hMatrix;


    PageRankMatrix(ArrayList<Pair<Integer, Integer>> paths, int amount){
        this.amount = amount;
        initHMatrix();
        initLMatrix(paths);
        initAMatrix();
    }

    private void initHMatrix(){
        this.hMatrix = new BigDecimal[amount];
        for(int i = 0; i < amount; i++) {
            hMatrix[i] = BigDecimal.valueOf(1);
        }
    }

    private void initLMatrix(ArrayList<Pair<Integer, Integer>> paths){
        this.LMatrix = new BigDecimal[amount][amount];
        this.LTMatrix = new BigDecimal[amount][amount];
        for(int i = 0; i < amount; i++) {
            for(int j = 0; j < amount; j++) {
                if(paths.contains(new Pair<>(i, j))) {
                    LMatrix[i][j] = BigDecimal.valueOf(1);
                    LTMatrix[j][i] = BigDecimal.valueOf(1);
                } else {
                    LMatrix[i][j] = BigDecimal.valueOf(0);
                    LTMatrix[j][i] = BigDecimal.valueOf(0);
                }
            }
        }
    }

    private void initAMatrix(){
        this.aMatrix = new BigDecimal[amount];
        BigDecimal max = BigDecimal.valueOf(-1);
        for(int i = 0; i < amount; i++) {
            aMatrix[i] = BigDecimal.valueOf(0);
            for(int j = 0; j < amount; j++) {
                aMatrix[i] = aMatrix[i].add(LTMatrix[i][j]);
            }
            if(aMatrix[i].compareTo(max) > 0) {
                max = aMatrix[i];
            }
        }
        for(int i = 0; i < amount; i++) {
            aMatrix[i] = aMatrix[i].divide(max, 2, RoundingMode.HALF_UP);
        }
    }

    public void doAlgorithm(int howManyTimes) {
        for (int j = 0; j < howManyTimes; j++) {
            calcHMatrix();
            calcAMatrix();
        }
        for (int i = 0; i < amount; i++) {
            System.out.println(hMatrix[i] + " h - a " + aMatrix[i]);
        }
    }

    private void calcHMatrix(){
        BigDecimal max = BigDecimal.valueOf(-1);
        for(int i = 0; i < hMatrix.length; i++) {
            hMatrix[i] = BigDecimal.valueOf(0);
            for(int j = 0; j < amount; j++) {
                hMatrix[i] = hMatrix[i].add(LMatrix[i][j].multiply(aMatrix[j]));
            }
            if(hMatrix[i].compareTo(max) > 0) {
                max = hMatrix[i];
            }
        }
        for(int i = 0; i < amount; i++) {
            hMatrix[i] = hMatrix[i].divide(max,2, RoundingMode.HALF_UP);
        }
    }

    private void calcAMatrix(){
        BigDecimal max = BigDecimal.valueOf(-1);
        for(int i = 0; i < aMatrix.length; i++) {
            aMatrix[i] = BigDecimal.valueOf(0);
            for(int j = 0; j < amount; j++) {
                aMatrix[i] = aMatrix[i].add(LTMatrix[i][j].multiply(hMatrix[j]));
            }
            if(aMatrix[i].compareTo(max) > 0) {
                max = aMatrix[i];
            }
        }
        for(int i = 0; i < amount; i++) {
            aMatrix[i] = aMatrix[i].divide(max, 2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal[] getaMatrix() {
        return aMatrix;
    }

    public BigDecimal[] gethMatrix() {
        return hMatrix;
    }
}
