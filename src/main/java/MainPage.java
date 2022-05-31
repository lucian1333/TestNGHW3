import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class MainPage {

    @Test

    public void task1 () {





        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.navigate().to("http://automationpractice.com/index.php");

        WebElement emptyCart = driver.findElement(By.xpath("//div[@class='shopping_cart']//span[@class='ajax_cart_no_product']"));
        Assert.assertEquals(emptyCart.getText().trim().substring(1, (emptyCart.getText().length() - 1)), "empty");

    // Get all the product prices
    // Find cheapest product and get the name of product

        List<WebElement> priceElements = driver.findElements(By.xpath("//ul[@id='homefeatured']//div[@class='right-block']//span[@class='price product-price']"));
        List<WebElement> nameElements = driver.findElements(By.xpath("//ul[@id='homefeatured']//div[@class='right-block']//a[@class='product-name']"));
    // iterate through the lists and in order to find the cheapest price we need a min value... so we create a variable minValue
    // as we DO NOT KNOW the max value in the List of prices will assign the max double value from the system to have comparison reference
    // at the first iteration in the list, the minValue will take the  value of the first price in the List
    // as we iterate will compare price elements one by one to be able to find the cheapest one

        double minPrice = Double.MAX_VALUE;
    // in order to create a connection between priceElements and nameElements we will create an index contor which will be used later to retrive the name of the minPrice element
        int index = 0;

        for (int i=0; i<priceElements.size(); i++)
        {
            String priceText = priceElements.get(i).getText().trim().substring(1); // retrieve each price from webelemnt as text
        // !! the easiest way to convert a String price to a Double price << -- >> double value = new Double(priceText)
            double value = new Double(priceText);    // convert each price to double value
            if (minPrice > value)
            { minPrice = value;
              index = i; } // above line and this one (50 & 51) translate = if the minPrice takes the value of element i then store the i value in int index to be able to get name

        }
        System.out.println(nameElements.get(index).getText().trim()+ " $"+minPrice); // -->> name of cheapest product + it's price

    // Get the original price of product
    // Get the discount rate of product
    // Validate after discount rate new product price is correct



    // to be able to connect the name we've got from min price with the three price values on the product element
    // we need to use an xPath which will locate the priceLine WebElement using a search by its name !!! it will be a bit complicated BUT to be able to validate elements with a returned value
    // on a previous search we NEED to use that value inside the locator to CREATE the LINK !!!!!!

    // EG : //div[@class='right-block']//a[contains(text() , 'Printed Chiffon Dress')] ... BUT ... BC this selector is NOT UNIQUE ... we need to locate it by its tree structure
    //  //ul[@id='homefeatured']//div[@class='right-block']//a[contains(text() , 'Printed Chiffon Dress')]/parent::h5/following-sibling::div[@class='content_price']/span
    // from title "Printed Chiffon Dress" we locate its parent (h5) and ... bc product title is sibling in tree structure with the 3 price field line we move from parent to following sibling

        List<WebElement> individualProductallPriceFields = driver.findElements(By.xpath("//ul[@id='homefeatured']//div[@class='right-block']" +
                                                                         "//a[contains(text() , '"+nameElements.get(index).getText().trim()+"')]/parent::h5/following-sibling::div[@class='content_price']/span"));

    // as we WANT our locator to be DYNAMIC and LINKED to the previous search will replace the actual product name  (Printed Chiffon Dress) with its variable :  nameElements.get(index).getText().trim()

    // NOW ... First element in this list of webelements is the dicountedPrice
    //          Second element is the originalPrice
    //     and  Third element is the discount percentage

        double originalPrice = new Double(individualProductallPriceFields.get(1).getText().trim().substring(1));
        double discountPercentageAmount = new Double(individualProductallPriceFields.get(2).getText().trim().substring(1 , individualProductallPriceFields.get(2).getText().trim().length()-1));

        double toBeDiscounted = originalPrice*discountPercentageAmount/100;
        double expectedDiscountedPrice = originalPrice-toBeDiscounted;

    // Validation -->>

        Assert.assertEquals(minPrice , expectedDiscountedPrice, "Percentage calculation is not right!");

        System.out.println("Expected price is "+expectedDiscountedPrice+" and actual displayed price is "+minPrice);
        System.out.println("Sale percentage is "+discountPercentageAmount);
        System.out.println("Original price of the product is "+originalPrice);





    }
}
