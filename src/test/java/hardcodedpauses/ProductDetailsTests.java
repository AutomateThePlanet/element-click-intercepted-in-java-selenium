/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hardcodedpauses;

import io.github.bonigarcia.wdm.WebDriverManager;
import models.ProductDetails;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ProductDetailsTests {
    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;

    @BeforeEach
    public void testInit() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void testCleanup() {
        driver.quit();
    }

    @Test
    public void elementInterceptedTest() {

        // Act
        driver.navigate().to("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=29");

        var addToCartButton = driver.findElement(By.xpath("//button[@title='Add to Cart']"));
        addToCartButton.click();

        var viewCartButton = driver.findElement(By.xpath("//a[@class='btn btn-primary btn-block']"));
        viewCartButton.click();
//      var shippingCountrySelect = driver.findElement(By.id("input-country"));
//      shippingCountrySelect.click();

        var estimateCountryExpandSection = driver.findElement(By.id("//h5[text()='Estimate Shipping & Taxes ']"));
        estimateCountryExpandSection.click();

        var element = driver.findElement(By.id("Login"));
        actions.moveToElement(element).click().build().perform();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        actions.moveByOffset(123, 653).click().build().perform();
    }

    @Test
    public void clickDisabledButton() {
        driver.navigate().to("https://ecommerce-playground.lambdatest.io/index.php?route=product/product&product_id=32");

        var addToCartButton = driver.findElement(By.xpath("//button[@title='Add to Cart']"));
        //addToCartButton.click();

        Assertions.assertFalse(addToCartButton.isEnabled());
    }

    // add demo bellatrix add coupon - overlapping elements

    @Test
    public void correctInformationDisplayedInQuickView_whenOpenProductFromSearchResults() throws InterruptedException {
        var expectedProduct1 = new ProductDetails();
        expectedProduct1.setName("iPod Touch");
        expectedProduct1.setId(32);
        expectedProduct1.setPrice("$194.00");
        expectedProduct1.setModel("Product 5");
        expectedProduct1.setBrand("Apple");
        expectedProduct1.setWeight("5.00kg");

        driver.navigate().to("https://ecommerce-playground.lambdatest.io/");
        var searchInput = driver.findElement(By.xpath("//input[@name='search']"));
        searchInput.sendKeys(expectedProduct1.getName());

        var searchButton = driver.findElement(By.xpath("//button[text()='Search']"));
        searchButton.click();

        var productLinkXpath = String.format("//a[contains(@id, '%d')]", expectedProduct1.getId());
        var productsLink = driver.findElement(By.xpath(productLinkXpath));
        productsLink.click();

        var addToCartButton = driver.findElement(By.xpath("//button[@title='Add to Cart']"));
        addToCartButton.click();
    }

    private void waitForAjax() {
        var js = (JavascriptExecutor)driver;
        wait.until(wd -> js.executeScript("return jQuery.active").toString() == "0");
    }
}