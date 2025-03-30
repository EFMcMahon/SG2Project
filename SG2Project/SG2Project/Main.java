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

    public static void promptUserEnterKey(Scanner s) {
        System.out.println("Press Enter to continue...");
        s.nextLine();
    }
    
    // This function is for making sure that the file being inputed matches the correct format
    public static void validateInput(String fileUser, Scanner consoleScanner) throws IOException {
        fileUser = fileUser.toUpperCase();
        if (fileUser.length() < 4) {
            System.out.println("The file must be at least 5 characters including \".CSV\" as a suffix.");
            fileUser = consoleScanner.nextLine();
            validateInput(fileUser, consoleScanner);
        } else if (!fileUser.substring(fileUser.length() - 4).contains(".CSV")) {
            System.out.println("Please input a CSV file. It must have \".CSV\" as a suffix.");
            fileUser = consoleScanner.nextLine();
            validateInput(fileUser, consoleScanner);
        } else if (((fileUser.length() - 4) == 0)) {
            System.out.println("Please input a CSV file. It must have more characters than just \".CSV\"");
            fileUser = consoleScanner.nextLine();
            validateInput(fileUser, consoleScanner);
        } else { // This else is ran if the csv passes all validation checks
            File file = new File(fileUser);
            processCSV(file);
        }
    }

    public static boolean processCSV(File file) throws IOException
    {
        // Used for keeping track of what line the program is reading
        int lineNumber = 0;
        int numSpecies = 0;
        int numDates = 0;
        double max = 0;
        Scanner scanner = new Scanner(System.in);
        List<String> dates = new ArrayList<>();

        if (!file.exists() || !file.isFile()) {
            System.out.println("Error: File not found or incorrect directory.");
            return false;
        }

        // BufferedWriters for Dated.Data and PresentAbsent
        BufferedWriter dateWriter = new BufferedWriter(new FileWriter("DatedData.txt"));
        BufferedWriter presentAbsentWriter = new BufferedWriter(new FileWriter("PresentAbsent.txt"));

        //Reads file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            String line;
            line = reader.readLine();
            lineNumber++;
            char commaCheck = line.charAt(0);
            if (commaCheck != ',') {
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

                        promptUserEnterKey(scanner);
                        return false;
                    }

                    for (int x = 1; x < dateList.length; x++) {
                        // checks if number provided contains alphabet character
                        if (dateList[x].matches(".*[a-zA-Z].*")) {
                            System.out.println("Content provided contains alphabet character at line: " + lineNumber);
                            System.out.println("Invalid Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            promptUserEnterKey(scanner);
                            return false;
                        }
                        // checks if number provided is real number
                        if (!dateList[x].matches("^[-+]?(?:\\d+(\\.\\d*)?|\\d+/\\d+)$")) {
                            System.out.println("Content provided not a valid number at line: " + lineNumber);
                            System.out.println("Invalid Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            promptUserEnterKey(scanner);
                            return false;
                        }
                        // checks if number provided is a fraction
                        if (dateList[x].contains("/")) {
                            System.out.println("Content provided is a fraction at line: " + lineNumber);
                            System.out.println("Invalid Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            promptUserEnterKey(scanner);
                            return false;
                        }
                        // checks if number provided is negative
                        if (Double.parseDouble(dateList[x]) < 0) {
                            System.out.println("Content provided is negative at line: " + lineNumber);
                            System.out.println("Content: " + dateList[x]);
                            System.out.println("Line Content: " + line);

                            promptUserEnterKey(scanner);
                            return false;
                        }
                    }
                    // if numbers are valid then add to dates found count
                    numDates++;

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
            /*
            SG2 should output a message to the screen announcing how many different species (names) were found
            in the file, and how many different dates were found. The user should be prompted to push ENTER to
            continue the program
             */
            System.out.println("Number of different species(names) found: " + numSpecies);
            System.out.println("Number of dates found: " + numDates);
            promptUserEnterKey(scanner);
        }
        dateWriter.close();
        presentAbsentWriter.close();
        return true;
    }
}

