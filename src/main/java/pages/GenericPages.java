package pages;

import org.openqa.selenium.*;

import java.util.HashMap;
import java.util.Map;


public class GenericPages {
    private WebDriver driver;
    private String baseUrl;
    private By searchBoxLocator;
    private By searchButtonLocator;

    public GenericPages(WebDriver driver, String baseUrl, By searchBoxLocator, By searchButtonLocator) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.searchBoxLocator = searchBoxLocator;
        this.searchButtonLocator = searchButtonLocator;
    }

    // Navigate to site
    public void open() {
        driver.get(this.baseUrl);
    }
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void acceptCookies(By cookiesLocator) {
        try {
            scrollToElement(cookiesLocator);
//            WebDriverWait wait = new WebDriverWait(driver, duration.ofSeconds(5));
            WebElement cookieButton = driver.findElement(cookiesLocator);
            if (cookieButton.isDisplayed()) {
                cookieButton.click();
                System.out.println("cookie button clicked & accepted.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("no cookie button found.");
        }
    }

    // Search action

    public void seachLicenseNumber(String number, int mileage, By mileageLocator) {
        // Enter licence plate number
        WebElement searchBox = driver.findElement(searchBoxLocator);
        searchBox.clear();
        searchBox.sendKeys(number);

        /**
         * Enter miliage

         */
        WebElement mileageBox = driver.findElement(mileageLocator);
        mileageBox.clear();
        mileageBox.sendKeys(String.valueOf(mileage));

        // click search button

        driver.findElement(searchButtonLocator).click();
        System.out.println("search button clicked & accepted.");
    }
    // what are we returning? list of v details
    public Map<String, String> extractSearchResults(By variantLocator, By makeLocator, By modelLocator, By yearLocator ) {
        Map<String, String> res_data = new HashMap<>();
        try {
            String variantReg = driver.findElement(variantLocator).getText();
            System.out.println("Variant Reg Text: " + variantReg);
            res_data.put("variantReg", variantReg != null ? variantReg : "NOTHING BACK");

            String model = driver.findElement(modelLocator).getText();
            System.out.println("Model Text: " + model);
            res_data.put("model", model != null ? model : "NOTHING BACK");

            String year = driver.findElement(yearLocator).getText();
            System.out.println("Year Text: " + year);
            res_data.put("year", year != null ? year : "NOTHING BACK");

            String make = driver.findElement(makeLocator).getText();
            System.out.println("Make Text: " + make);
            res_data.put("make", make != null ? make : "NOTHING BACK");


//            res_data.put("VARIANT_REG", driver.findElement(variantLocator).getText());
//            res_data.put("MAKE_REG", driver.findElement(makeLocator).getText());
//            res_data.put("MODEL_REG", driver.findElement(modelLocator).getText());
//            res_data.put("YEAR", driver.findElement(yearLocator).getText());
        } catch (Exception e) {
            System.err.println("Error while trying to extract search results: " + e.getMessage());
            e.printStackTrace();
        }
        return res_data;
    }


    public boolean isELementDisplayed(By locator) {
        return driver.findElement(locator).isDisplayed();
    }

    public void close() {
        if(driver != null){
            driver.quit();
        }
    }
}

