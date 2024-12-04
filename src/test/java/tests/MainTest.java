package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GenericPages;
import utils.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.DynamicPageFactory.parseLocator;

public class MainTest {
    private Map<String, String> inputFilePaths;
    private Map<String, String> locators;
    private  WebDriver driver;

    @BeforeClass
    public void setup() throws IOException {
        inputFilePaths = Map.of(
                "src/main/resources/car_input.txt", "src/main/resources/car_output.txt");

        locators = DynamicPageFactory.loadConfig().get("webuyanycar");
        driver = Drivers.createChromeDriver();
    }

    @AfterClass
    public void teardown() {
        if (driver != null){
            driver.quit();
        }
    }

    @Test
    public void mainTest() throws IOException {
        // Patterns
        String regexPattern = "[A-Z]{2}[0-9]{2}\\s?[A-Z]{3}";

        for (Map.Entry<String, String> entry : inputFilePaths.entrySet()) {
            String inputPath = entry.getKey();
            String outputPath = entry.getValue();

            System.out.println("Processing files paths: " + inputPath + " - " + outputPath);

            // Extract numbers from car input
            List<String> lNumbers = ReadFileHelper.extractNumbers(inputPath, regexPattern);
            Map<String, Map<String, String>> expectedData = FileParser.parseDataFile(outputPath);

            for (String number : lNumbers) {
                boolean tResult = runLicenceTest(number, expectedData);
                if (tResult) {
                    System.out.println("Test Passed: " + number);
                } else {
                    System.out.println("Test Failed: " + number);
                }
            }
        }
    }

    private boolean runLicenceTest(String number, Map<String, Map<String, String>> expectedData) {
        try {
            String normalizedLicenceNumber = number.trim().replaceAll("\\s+", "").toUpperCase();

            Map<String, String> expectedRecord = expectedData.get(normalizedLicenceNumber);
            if (expectedRecord == null) {
                System.err.println("Record not found: " + normalizedLicenceNumber);
                return false;
            }

            GenericPages page = DynamicPageFactory.createPage(driver, "webuyanycar");
            page.open();
            page.acceptCookies(By.id("onetrust-accept-btn-handler"));
            int mileage = MileageHelper.genRandomMileage(1000, 150000);
            System.out.println("Search for Licensce: " + number + " Mileage: " + mileage);
            page.seachLicenseNumber(number, mileage, parseLocator(locators.get("mileageLocator")));

            Map<String, String> actualData = page.extractSearchResults(
                    parseLocator(locators.get("variantLocator")),
                    parseLocator(locators.get("modelLocator")),
                    parseLocator(locators.get("yearLocator")),
                    parseLocator(locators.get("makeLocator"))
            );


            return compareData(normalizedLicenceNumber, expectedRecord, actualData);
        } catch (Exception e) {
            System.err.println("Eror procressing licence plate number " + number + ":" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean compareData(String number, Map<String, String> expectedRecord, Map<String, String> actualData) {
        boolean allMatch = true;
        for (String key : expectedRecord.keySet()) {
            String expectedValue = expectedRecord.get(key);
            String actualValue = actualData.get(key.toLowerCase());

            if (!expectedValue.equalsIgnoreCase(actualValue)){
                System.err.println("Mismatch for " + number + "_" + key + ": does not match Expected:  " + expectedValue + " " + actualValue);
                allMatch = false;
            } else {
                System.out.println("Matching for  " + number + "_ " + key + "matching ->" + expectedValue + " " + actualValue);
            }
        }
        return allMatch;

        }
    }














//
//        //get all locators
////        Map<String, By> locators = DynamicPageFactory.getLocators("webuyanycar");
////        System.out.println("LOCATORS: " + locators); // not sure why null right now
//
//        // do manual for data extraction as time permits - can put locators in separate file for resusability
//        By variantLocator = By.xpath("(//*[normalize-space(text())='Image for illustrative purposes only'])[4]/following::div[1]");
//        By yearLocator = By.xpath("//*[@id=\"wbac-app-container\"]/div/div/vehicle-questions/div/section[1]/div/div[1]/div/div[3]/div/vehicle-details/div[3]/div[2]/div[2]/div[2]");
//        By modelLocator = By.xpath("//*[@id=\"wbac-app-container\"]/div/div/vehicle-questions/div/section[1]/div/div[1]/div/div[3]/div/vehicle-details/div[3]/div[2]/div[3]/div[2]");
//        By makeLocator = By.xpath("//*[@id=\"wbac-app-container\"]/div/div/vehicle-questions/div/section[1]/div/div[1]/div/div[3]/div/vehicle-details/div[3]/div[2]/div[1]/div[2]");
//
//
//        for (String licenceNumber : lNumbers) {
//            WebDriver driver = null;
//
//            try {
//                driver = Drivers.createChromeDriver();
//
//                // Get Mileage Locator
//                Map<String, String> siteConfig = DynamicPageFactory.loadConfig().get("webuyanycar"); // this cant me hard coded
//                By mileageLocator = parseLocator(siteConfig.get("mileageLocator"));
//
//                GenericPages page = DynamicPageFactory.createPage(driver, "webuyanycar");
//                page.open();
//                page.acceptCookies(By.id("onetrust-accept-btn-handler"));
//                int mileage = MileageHelper.genRandomMileage(1000, 150000);
//                System.out.println("Search for Licensce: " + licenceNumber + " Mileage: " + mileage);
//                page.seachLicenseNumber(licenceNumber, mileage, mileageLocator);
//
//                Map<String, String> actualData = page.extractSearchResults(
//                        variantLocator,
//                        modelLocator,
//                        yearLocator,
//                        makeLocator
//                );
//
//
//                System.out.println("Actual Data Keys: " + actualData.keySet());
//                System.out.println("Actual Data: " + actualData);
//
//                String normalizedLicenceNumber = licenceNumber.trim().replaceAll("\\s+", "").toUpperCase();
//                Map<String, String> expectedRecord = expectedData.get(normalizedLicenceNumber);
//                System.out.println("Expected: " + normalizedLicenceNumber + ":" + expectedRecord);
//
//                if (expectedRecord == null) {
//                    System.err.println("Record not found: " + normalizedLicenceNumber);
//                    continue;
//                }
//
//                System.out.println("Expected Record Keys: " + expectedRecord.keySet());
//                System.out.println("Expected Record: " + expectedRecord);
//
//                System.out.println("Actual Data Keys: " + actualData.keySet());
//                System.out.println("Actual Data: " + actualData);
//
//
//
//                Map<String, String> normalizedExpectedRecord = new HashMap<>();
//
//                //comparison
//                for (String key : expectedRecord.keySet()) {
//                    normalizedExpectedRecord.put(key.toLowerCase(), expectedRecord.get(key).trim());
//                }
//                for (String key : normalizedExpectedRecord.keySet()) {
//                    String expectedValue = expectedRecord.get(key);
//                    String actualValue = actualData.getOrDefault(key, "None").trim();
//                    if (!expectedValue.equalsIgnoreCase(actualValue)) {
//                        System.err.println("Mismatch for " + licenceNumber + "_" + key + ": does not match Expected:  " + expectedValue + " " + actualValue);
//                    } else {
//                        System.out.println("Matching for  " + licenceNumber + "_ " + key + "matching ->" + expectedValue + " " + actualValue);
//                    }
//                }
//
//            } catch (Exception e) {
//                System.err.println("Eror procressing licence plate number " + licenceNumber + ":" + e.getMessage());
//                e.printStackTrace();
//            } finally {
//                if (driver != null) {
//                    driver.quit();
//                }
//            }
//        }
//
//    }
//}
//
//
