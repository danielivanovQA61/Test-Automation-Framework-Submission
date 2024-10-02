package saucedemotests.web;

import com.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import saucedemotests.core.SauceDemoBaseWebTest;
import saucedemotests.enums.TestData;

public class ProductsTests extends SauceDemoBaseWebTest {
    public final String BACKPACK_TITLE = "Sauce Labs Backpack";
    public final String SHIRT_TITLE = "Sauce Labs Bolt T-Shirt";
    public final String BIKE_LIGHT_TITLE = "Sauce Labs Bike Light";
    public final String FLEECE_JACKET_TITLE = "Sauce Labs Fleece Jacket";
    public final String FIRST_NAME = "Daniel";
    public final String LAST_NAME = "Ivanov";
    public final String ZIP = "1000";

    @BeforeEach
    public void beforeTest(){
        // Authenticate with Standard user

        // Navigate to Login Page
        loginPage.navigate();
        // Submit login form
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();
        // Assert expected page navigated
        inventoryPage.assertNavigated();
    }

    @Test
    public void productAddedToShoppingCart_when_addToCart(){
        // Add products to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE,BIKE_LIGHT_TITLE,SHIRT_TITLE,FLEECE_JACKET_TITLE);
        // Go to shopping cart
        shoppingCartPage.navigate();
        // Assert Items and Totals
        Assertions.assertEquals(BACKPACK_TITLE, shoppingCartPage.getShoppingCartItems().get(0).getText(), "Item title is not as expected.");
        Assertions.assertEquals(BIKE_LIGHT_TITLE, shoppingCartPage.getShoppingCartItems().get(1).getText(), "Item title is not as expected.");
        Assertions.assertEquals(SHIRT_TITLE, shoppingCartPage.getShoppingCartItems().get(2).getText(), "Item title is not as expected.");
        Assertions.assertEquals(FLEECE_JACKET_TITLE, shoppingCartPage.getShoppingCartItems().get(3).getText(), "Item title is not as expected.");
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){
        // Add products to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE,BIKE_LIGHT_TITLE,SHIRT_TITLE,FLEECE_JACKET_TITLE);
        // Go to shopping cart
        shoppingCartPage.navigate();
        // Go to checkout
        shoppingCartPage.clickCheckout();
        // Fill form
        checkoutYourInformationPage.fillShippingDetails(FIRST_NAME,LAST_NAME,ZIP);
        // Continue
        checkoutYourInformationPage.clickContinue();
        // Assert Cart Items number
        Assertions.assertEquals(4, shoppingCartPage.getShoppingCartItems().size(), "Items count is not as expected.");
        // Calculate expected total cost
        double expectedTotalCost = 29.99 + 9.99 + 15.99 + 58.47;
        String expectedTotal = String.format("Total: $%.2f", expectedTotalCost);
        var total = driver().findElement(By.className("summary_total_label")).getText();
        // Assert Cart Items Titles and total cost
        Assertions.assertEquals(BACKPACK_TITLE, shoppingCartPage.getShoppingCartItems().get(0).getText(), "Item title is not as expected.");
        Assertions.assertEquals(BIKE_LIGHT_TITLE, shoppingCartPage.getShoppingCartItems().get(1).getText(), "Item title is not as expected.");
        Assertions.assertEquals(SHIRT_TITLE, shoppingCartPage.getShoppingCartItems().get(2).getText(), "Item title is not as expected.");
        Assertions.assertEquals(FLEECE_JACKET_TITLE, shoppingCartPage.getShoppingCartItems().get(3).getText(), "Item title is not as expected.");
        Assertions.assertEquals(expectedTotal, total, "Items total price is not as expected.");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){
        // Add Backpack and T-shirt to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE,SHIRT_TITLE);
        // Click on shopping Cart
        shoppingCartPage.navigate();
        // Go to Billing Info
        shoppingCartPage.clickCheckout();
        // Fill form
        checkoutYourInformationPage.fillShippingDetails(FIRST_NAME,LAST_NAME,ZIP);
        // Continue
        checkoutYourInformationPage.clickContinue();
        // Complete Order
        checkoutOverviewPage.clickFinish();
        // Assert Items removed from Shopping Cart
        inventoryPage.clickShoppingCartLink();
        Assertions.assertEquals(0,inventoryPage.getShoppingCartItemsNumber(), "Items count is not as expected.");
        // Assert Shopping cart is empty
        shoppingCartPage.navigate();
        Assertions.assertTrue(shoppingCartPage.getShoppingCartItems().isEmpty(), "Shopping cart is not empty.");
    }
}