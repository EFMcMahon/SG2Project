import java.io.*;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, this program will ask the user for the name of the CSV file.");
        System.out.println("The program will then read the CSV file, line by line, and extract the following:");
        System.out.println("Please press ENTER to continue");

        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("Press ENTER to exit.");
                scanner.nextLine();
                scanner.close();
                return;
            }

            System.out.println("Error, Please press ENTER to continue.");
        }
    }
}

public static void chec(Scanner scanner) throws IOException {
    while (true) {
        System.out.println("Please enter the name of the CSV file:");
        String file = scanner.nextLine().trim();

        if (file.toLowerCase().endsWith(".csv")) {
            File csvFile = new File(file);

            if (csvFile.exists() && csvFile.isFile()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(csvFile));

                    try {
                        processCSV(reader);
                    } catch (Throwable e) {
                        try {
                            reader.close();
                        } catch (Throwable closeException) {
                            e.addSuppressed(closeException);
                        }
                        throw e;
                    }

                    reader.close();
                    return;
                } catch (FileNotFoundException e) {
                    System.out.println("File not found. Please try again.");
                } catch (IOException e) {
                    System.out.println("Error reading the file.");
                }
            } else {
                System.out.println("File not found in the specified directory. Please make sure the file is in the project root.");
            }
        } else {
            System.out.println("Invalid file extension. Please enter a valid CSV file (e.g., 'data.CSV').");
        }
    }
}