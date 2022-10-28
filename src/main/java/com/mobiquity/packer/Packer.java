package com.mobiquity.packer;

import com.mobiquity.exception.APIException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Packer {

    /**
     * Splits/extracts the contents of the text file into 3 arrays - weights[]. indices[], costs[] and determines the max cost
     * @param filePath
     * @return
     * @throws APIException
     */
    public static String pack(String filePath) throws APIException {
        String fileData = readFile(filePath);
        String[] lines = fileData.split("\n");
        StringBuffer results = new StringBuffer();

        for (String line : lines) {
            String[] components = line.split(":");
            int maxWeight;
            if (components.length == 2) {
                maxWeight = Integer.valueOf(components[0].trim()) * 100;
                String inputs = components[1].trim();
                long countOfWhitespaces = inputs.chars().filter(ch -> ch == ' ').count();
                int numberOfInputs = (int) countOfWhitespaces + 1;
                int[] weights = new int[numberOfInputs];
                int[] costs = new int[numberOfInputs];
                int[] indices = new int[numberOfInputs];
                int index = 0;

                String[] items = inputs.split(" ");
                for (String item : items) {

                    item = item
                            .replace("(", "")
                            .replace(")", "")
                            .replace("€", "");
                    String[] contents = item.split(",");
                    if (contents.length == 3) {
                        indices[index] = Integer.valueOf(contents[0]);
                        weights[index] = (int) (Float.valueOf(contents[1]) * 100);
                        costs[index] = Integer.valueOf(contents[2]);
                        index += 1;
                    } else {
                        throw new APIException("Failed to parse input file - unable to split to 3 parts by comma(,)");
                    }

                }

                List<Integer> selectedIndices = CostMaximise.maximiseCost(weights, costs, indices, maxWeight);
                if (selectedIndices.size() < 1) {
                    results.append("-");
                } else {
                    StringBuffer result = new StringBuffer();
                    for (Integer i : selectedIndices) {
                        result.append(i + ",");
                    }
                    results.append(result.toString().substring(0, result.toString().length() - 1));
                }
                results.append("\n");

            } else {
                throw new APIException("Failed to parse input file - unable to split to 2 parts by colon(:)");
            }

        }
        return results.toString();

    }

    /**
     * Reading text file contents
     * @param path
     * @return
     */
    private static String readFile(String path) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream
                     = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            //Read the content with Stream
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileContent = contentBuilder.toString();
        return fileContent;
    }
}
