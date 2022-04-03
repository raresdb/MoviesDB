package main;

import common.Constants;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class used for testing the database.
 * <p>
 * @author Butilca Rares
 */
public final class
Main {

    /**
     * Run the database simulation on a given input
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        File[] inputDir = directory.listFiles();

        if (inputDir != null) {
            Arrays.sort(inputDir);

            //Read the input file
            Scanner scanner = new Scanner(System.in);
            String fileName = scanner.next();
            for (File file : inputDir) {
                if (file.getName().equalsIgnoreCase(fileName)) {
                    action(file.getAbsolutePath(), Constants.OUT_FILE);
                    break;
                }
            }
        }
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        // Creating the entry point of the database program and running it
        Database database = new Database();
        database.run(input, fileWriter, arrayResult);

        fileWriter.closeJSON(arrayResult);
    }
}
