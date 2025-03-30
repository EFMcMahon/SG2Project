/*
Language: Java
IDE: IntellJ, Visual Studio Code, netbeans
- - - - - - - - - - - - - - - - - - - -
Group: Randy Vo, Eric McMahon,  Joe Mugo 
Date: 04/2/2025
Class: CS 4500-001
- - - - - - - - - - - - - - - - - - - -
[Program Description:]
Assignment: Small Group Project 2
Description: This Java program reads a CSV file provided by the user and extracts the following information:
1. ** Extract Species (Names) **:
    - Reads the first line of the CSV file to get the names and number of names (abundance count).
    - Writes the column names to a file named 'Species.txt'.

2. ** Extracts Dates**:
    - Reads the dates from the first column of each subsequent line in the CSV file.
    - Writes the dates to a file named 'DatedData.txt'.

3. **Processes Numeric Data**:
    - Reads the numeric data from the remaining columns of each line.
    - Converts positive numbers to '1' and zero to '0'.
    - Writes the converted data to a file named 'PresentAbsent.txt'.
- - - - - - - - - - - - - - - - - - - -
[Sources:]
   - BufferedWriter: https://www.programiz.com/java-programming/bufferedwriter, https://www.geeksforgeeks.org/io-bufferedwriter-class-methods-java/
   - BufferedReader: https://www.programiz.com/java-programming/bufferedreader, https://www.geeksforgeeks.org/bufferedreader-class-in-java/
   - FileReader: https://www.geeksforgeeks.org/file-reader-java-with-examples/
   - FileWriter: https://www.geeksforgeeks.org/filewriter-class-in-java-with-examples/
   - IOException: https://www.geeksforgeeks.org/handle-an-ioexception-in-java/
   - FileNotFoundException: https://docs.oracle.com/javase/8/docs/api/java/io/FileNotFoundException.html
   - toLowerCase().endsWith(".csv") : https://stackoverflow.com/questions/26794275/how-do-i-ignore-case-when-using-startswith-and-endswith-in-java
   - split(“,“) function: https://www.geeksforgeeks.org/split-string-java-examples/
   - pathname: https://stackoverflow.com/questions/1693020/how-to-specify-filepath-in-java
- - - - - - - - - - - - - - - - - - - -
[How to Run:]
1. Import all the java library below.
2. Add ".CSV" file to the same root directory as the java program.
3. Run the Java program using the following command:
    'java SG2'
or pressing the run button/green triangle above.
4. Follow the on-screen instructions to enter the name of the CSV file.
5. The program will process the CSV file and generate the output files:
    - 'Species.txt'
    - 'DatedData.txt'
    - 'PresentAbsent.txt'
- - - - - - - - - - - - - - - - - - - -
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, this program will ask the user for the name of the CSV file.\nThe program will then read the CSV file, line by line, and extract the following:\nPlease press ENTER to continue");
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("Please input a file");
        String fileUser = consoleScanner.nextLine();
        validateInput(fileUser, consoleScanner);
    }
    
    // This function is for making sure that the file being inputed matches the correct format
    public static void validateInput(String fileUser, Scanner consoleScanner) throws IOException {
        while (true) {
            fileUser = fileUser.trim();

            if (fileUser.length() < 4 || !fileUser.toLowerCase().endsWith(".csv")) {
                System.out.println("Please input a valid CSV file name ending with .CSV");
            } else {
                File file = new File(fileUser);
                if (!file.exists() || !file.isFile()) {
                    System.out.println("File not found. Please input another file:");
                } else {
                    
                    processCSV(file);
                    return; 
                }
            }

            // Ask for input again
            fileUser = consoleScanner.nextLine();
        }
    }
    

    public static boolean processCSV(File file) throws IOException {
    
        // Used for keeping track of what line the program is reading
        int lineNumber = 1;
        int numSpecies = 0;
        double max = 0;
        Scanner scanner = new Scanner(System.in);


        System.out.println(file.exists());
        System.out.println(file.isFile());
        if (!file.exists() || !file.isFile()) {
            System.out.println("Error: File not found or incorrect directory.");
            return false;
        }

        // BufferedWriters for DatedData, PresentAbsent and species
        BufferedWriter speciesWriter = new BufferedWriter(new FileWriter("Species.txt"));
        BufferedWriter dateWriter = new BufferedWriter(new FileWriter("DatedData.txt"));
        BufferedWriter presentAbsentWriter = new BufferedWriter(new FileWriter("PresentAbsent.txt"));

        //Reads file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        
            String line = reader.readLine();
            char commaCheck = line.charAt(0);
            if (commaCheck != ',' || line == null) {
                System.out.println("The first character in the file must be ','.");
                return false;
            }

            //Only first line is read for species
        
                FileWriter species = new FileWriter("Species.txt");
                String[] speciesList = line.substring(1).split(",");
                numSpecies = speciesList.length;
                System.out.println(Arrays.toString(speciesList));
                BufferedWriter speciesWriter = new BufferedWriter(new FileWriter("Species.txt"));
                for (String specie : speciesList) 
                {
                    speciesWriter.write(specie);
                    speciesWriter.newLine();
                }
                speciesWriter.close();
                species.close();
        
            //Read the rest of the lines until the line is empty or null 
            while ((line = reader.readLine()) != null) 
            {
                lineNumber++;
                double highestAbd = 0;
                if (line.trim().isEmpty()) 
                {
                    break;
                }
                
                //splits line by commas
                String[] dateList = line.split(",");
                if (dateList.length > 0) 
                {
                    //writes only date in DatedData
                    if (dateList[0].matches("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$")) {
                        dateWriter.write(dateList[0]);
                        dateWriter.newLine();
                    } else {
                        System.out.println("The dates must be in MM/DD/YYYY format at line " + lineNumber);
                        return false;
                    }

                    // validates that numbers provided are equal to species column
                    // numbers provided should be equal to species provided minus 1 (date column)
                    if ((dateList.length - 1) != numSpecies) {
                        System.out.println("Row count is invalid at line: " + lineNumber);
                        System.out.println("Line Content: " + line);

                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        scanner.close();

                        return false;
                    }

                    for (int x = 1; x < dateList.length; x++)
                    {
                        // checks if number provided contains alphabet character
                        if (dateList[x].matches(".*[a-zA-Z].*")) {
                            System.out.println("Content provided contains alphabet character at line: " + lineNumber);
                            System.out.println("Invalid Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                            scanner.close();

                            return false;
                        }
                        // checks if number provided is real number
                        if (!dateList[x].matches("^[-+]?(?:\\d+(\\.\\d*)?|\\d+/\\d+)$")) {
                            System.out.println("Content provided not a valid number at line: " + lineNumber);
                            System.out.println("Invalid Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                            scanner.close();

                            return false;
                        }
                        // checks if number provided is a fraction
                        if (dateList[x].contains("/")) {
                            System.out.println("Content provided is a fraction at line: " + lineNumber);
                            System.out.println("Invalid Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            System.out.println("Press Enter to continue...");
                            scanner.nextLine();
                            scanner.close();

                            return false;
                        }
                        // checks if number provided is negative
                        if (Double.parseDouble(dateList[x]) < 0) {
                            System.out.println("Content provided is negative at line: " + lineNumber);
                            System.out.println("Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            System.out.print("Press Enter to continue...");
                            scanner.nextLine();
                            scanner.close();
                            return false;
                        }
                    }


                    
                    //List that stores converted values to 1's and 0's

                    //TODO PresentAbsent portion needs to be reworked into the way that is being asked for in the project. It needs to be set as an array type data structure rather than written to the file immediately
                    List<String> presentAbsentList = new ArrayList<>();
                    // Adds values to the presentAbsentList based on corresponding data from the CSV
                    for (int i = 1; i < dateList.length; i++) 
                    {
                        String value = dateList[i].trim();
                        if (!value.isEmpty() && Double.parseDouble(value) > 0) 
                        {
                            double num = Double.parseDouble(value);
                            if (num > max)
                            {
                                max = num;
                            }
                            presentAbsentList.add(num != 0 ? "1" : "0");
                        } else if (Double.parseDouble(value) < 0) {
                            System.out.println("The file can not contain negative numbers.");
                            return false;
                        }
                    }
                    //joins values with commas and write to presentabsent
                    presentAbsentWriter.write(String.join(", ", presentAbsentList));
                    presentAbsentWriter.newLine();
                }
            }
        }
        return true;
    }
}

