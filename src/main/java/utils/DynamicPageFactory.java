package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.GenericPages;

import java.io.File;
import java.util.*;
import java.io.IOException;

public class DynamicPageFactory {
    private static  final String CONFIG_FILE = "src/main/resources/config.json";

    public static GenericPages createPage(WebDriver driver, String siteName) throws IOException {
        // Get the Json config
        Map<String, Map<String, String>> conf = loadConfig();

        // Try & retrieve config details based on the site name
        Map<String, String> siteConfig  = conf.get(siteName);
        if (siteConfig == null) {
            throw new IllegalArgumentException("site config not found for this site: " + siteName);
        }

        // Extract config details
        String baseurl = siteConfig.get("url");
        By searchBoxLocator = parseLocator(siteConfig.get("searchBoxLocator"));
        By searchButtonLocator = parseLocator(siteConfig.get("searchButtonLocator"));
        By cookiesLocator = parseLocator(siteConfig.get("cookiesLocator"));
        By mileageLocator = parseLocator(siteConfig.get("mileageLocator"));

        // return a generic page instance
        GenericPages page = new GenericPages(driver, baseurl, searchBoxLocator, searchButtonLocator);
        page.acceptCookies(cookiesLocator);
        return page;
    }

    // 1. load & parse to map, return map config
    public static Map<String, Map<String, String>> loadConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(CONFIG_FILE), new TypeReference<Map<String, Map<String, String>>>() {
        });
    }

    /**
     * Get all locators
     */

    public static Map<String, By>getLocators(String siteName) throws IOException {
        Map<String, Map<String, String>> conf = loadConfig();
        Map<String, String> siteConfig  = conf.get(siteName);

        if (siteConfig == null) {
            throw new IllegalArgumentException("site config not found for this site: " + siteName);
        }
        Map<String, By> locators = new HashMap<>();
        for (Map.Entry<String, String> entry : siteConfig.entrySet()) {
            if (entry.getKey().endsWith("locator")) {
                locators.put(entry.getKey(), parseLocator(entry.getValue()));
            }
        }
        System.out.println("LOCATORS: " + locators); // debug
        return locators;
    }



    // parse locator string and return By object
    public static By parseLocator(String locator) {
        System.out.println("Locator received: " + locator);
        if (locator == null){
            throw  new IllegalArgumentException("Locator string is null.");
        }
            if (locator.startsWith("id:")){
                return By.id(locator.substring(3));
            } else if (locator.startsWith("css:")) {
                return By.cssSelector(locator.substring(4));
            } else if (locator.startsWith("xpath:")) {
                return By.xpath(locator.substring(6));
            } else {
                throw new IllegalArgumentException("Unknown locator type: " + locator);

            }
        }
}

