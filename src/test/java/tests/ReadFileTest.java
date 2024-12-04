package tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GenericPages;
import utils.Drivers;
import utils.DynamicPageFactory;
import utils.ReadFileHelper;

import java.io.IOException;
import java.util.List;

public class ReadFileTest {

    @Test
    public static void main(String[] args) throws IOException {
        try{
            String filePath = "src/main/resources/car_input.txt";
            String regexPattern = "[A-Z]{2}[0-9]{2}\\s?[A-Z]{3}";

        // Get numberds
        List<String> numbers = ReadFileHelper.extractNumbers(filePath, regexPattern);

            System.out.println("Extracted Registraion numbers: ");
            for (String number : numbers) {
                System.out.println(number);
        }
    } catch (IOException e) {
            System.err.println("Error - check file:" + e.getMessage());
        }
    }
}
