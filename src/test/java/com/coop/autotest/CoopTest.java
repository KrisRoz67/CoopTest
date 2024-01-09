package com.coop.autotest;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.logging.Logger;

public class CoopTest {
    public static WebDriver driver;
    private static final Logger log = Logger.getLogger(CoopTest.class.getName());

    @BeforeAll
    public static void driverSetUp() {
        driver = new ChromeDriver();
        driver.get("https://www.cooppank.ee/kodulaen");
        WebElement cookieMessage = driver.findElement(By.cssSelector("#sliding-popup"));
        if (cookieMessage.isDisplayed()) {
            WebElement submitButton = driver.findElement(By.cssSelector("#popup-buttons > a.btn.btn-primary.agree-button.eu-cookie-compliance-default-button"));
            submitButton.click();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        }
    }


    @Test
    @DisplayName("Test for checking the display of maximum loan amount ")
    public void checkMaxSum() {
        WebElement incomeInput = driver.findElement(By.id("edit-monthly-income"));
        incomeInput.clear();
        incomeInput.sendKeys("650");
        incomeInput.sendKeys(Keys.ENTER);
        WebElement sum = driver.findElement(By.cssSelector("#loan-tab--2 > div > div.c-form-group.calculation-result-wrapper > div.c-form-field.c-calculator--col-right > p > span.value"));
        Assertions.assertTrue(sum.isDisplayed(), "Sum is not displayed despite the fact that it should be");
        Assertions.assertEquals("42 478", sum.getText(), "Actual value doesn't match the expected value.");
        log.info("Test passed");
    }


    @Test
    @DisplayName("Test for checking the message in case of low income ")
    public void tooLowIncomeTest() {
        WebElement incomeInput = driver.findElement(By.id("edit-monthly-income"));
        incomeInput.clear();
        incomeInput.sendKeys("50");
        incomeInput.sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        WebElement sum = driver.findElement(By.cssSelector("#loan-tab--2 > div > div.c-form-group.calculation-result-wrapper > div.c-form-field.c-calculator--col-right > p > span.value"));
        Assertions.assertFalse(sum.isDisplayed(), "The amount is shown despite the fact that it should not be. Test failed");
        WebElement errorMessage = driver.findElement(By.cssSelector("#loan-tab--2 > div > div.c-form-group.month-income-low.text-center > div > p"));
        Assertions.assertTrue(errorMessage.isDisplayed());
        Assertions.assertEquals("Taotleja minimaalne netopalk peab olema 550 eurot", errorMessage.getText(), " Actual text doesn't match the expected text.");
        log.info("Test passed");
    }

    @Test
    @DisplayName("Test for checking the input of negative numbers")
    public void checkNegativeInput() {
        WebElement incomeInput = driver.findElement(By.id("edit-monthly-income"));
        incomeInput.clear();
        incomeInput.sendKeys("-7");
        incomeInput.sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        String incomeInputText = incomeInput.getText();
        Assertions.assertTrue(incomeInputText.isEmpty(), "Negative number was allowed despite the fact that it should not be");
        log.info("Test passed");
    }

    @Test
    @DisplayName("Test for checking the message in case of to high income ")
    public void tooHighIncomeTest() {
        WebElement incomeInput = driver.findElement(By.id("edit-monthly-income"));
        incomeInput.clear();
        incomeInput.sendKeys("50000");
        incomeInput.sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        WebElement sum = driver.findElement(By.cssSelector("#loan-tab--2 > div > div.c-form-group.calculation-result-wrapper > div.c-form-field.c-calculator--col-right > p > span.value"));
        Assertions.assertFalse(sum.isDisplayed(),"The amount is shown despite the fact that it should not be. Test failed.");
        WebElement errorMessage = driver.findElement(By.cssSelector("#loan-tab--2 > div > div.c-form-group.max-loan-high.text-center > div > p"));
        Assertions.assertTrue(errorMessage.isDisplayed());
        Assertions.assertEquals("Kui soovite taotleda suuremat laenu kui 500 000, võtke ühendust eralaenuhalduriga."
                , errorMessage.getText(), " Actual text doesn't match the expected text.");
        log.info("Test passed");
        }

    @AfterAll
    public static void quit() {
        driver.quit();
    }
}
