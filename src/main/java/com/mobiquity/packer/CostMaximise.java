package com.mobiquity.packer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CostMaximise {

    /**
     * Finds the maximum cost by using dynamic programming approach
     * @param weight
     * @param cost
     * @param indices
     * @param maxWeight
     * @return
     */
    public static List<Integer> maximiseCost(int[] weight, int[] cost, int[] indices, int maxWeight) {

        int itemLength = weight.length;

        sort(weight, cost, indices, itemLength);
        int[][] dp = new int[itemLength][maxWeight + 1];

        for (int j = 1; j <= maxWeight; j++) {
            if (j > weight[0]) {
                dp[0][j] = cost[0];
            }
        }

        for (int i = 1; i < itemLength;i++) {
            for (int j = 1; j <= maxWeight; j++) {
                if (j >= weight[i]) {
                    dp[i][j] = Math.max(dp[i - 1][j], cost[i] + dp[i - 1][j - weight[i]]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return getSelectedIndices(dp, weight, cost, indices, maxWeight)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }


    /**
     * Sorting based on weights
     * @param A
     * @param B
     * @param C
     * @param size
     */

    private static void sort(int[] A, int[] B, int[]C, int size) {

        for(int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (A[i] >= A[j]) {
                    int tempA = A[i];
                    int tempB = B[i];
                    int tempC = C[i];
                    A[i] = A[j];
                    B[i] = B[j];
                    C[i] = C[j];
                    A[j] = tempA;
                    B[j] = tempB;
                    C[j] = tempC;
                }
            }
        }

    }

    /**
     * Indices of selected items are calculated here
     * @param dp
     * @param weights
     * @param costs
     * @param indices
     * @param capacity
     * @return
     */
    private static List<Integer> getSelectedIndices(int dp[][], int[] weights, int[] costs, int[] indices, int capacity){
        List<Integer> selectedIndices = new ArrayList<>();
        int maxCost = dp[weights.length-1][capacity];
        for(int i=weights.length-1; i > 0; i--) {
            if(maxCost != dp[i-1][capacity]) {
                selectedIndices.add(indices[i]);
                capacity -= weights[i];
                maxCost -= costs[i];
            }
        }

        if(maxCost != 0) {
            selectedIndices.add(indices[0]);
        }

        return selectedIndices;
    }

}
