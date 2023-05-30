package com.devthalys.inventorycontrolsystem.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;

public class ProductSeleniumTest {

    @Test
    public void productRegister(){
        System.setProperty("webdriver.edge.driver", "src\\drive\\msedgedriver.exe");
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        WebDriver navegator = new EdgeDriver();

        navegator.get("http://ceo:123@localhost:8081/server/");

        navegator.findElement(By.xpath("/html/body/div/form/input[1]")).click();
        navegator.findElement(By.xpath("/html/body/div/form/input[1]")).sendKeys("apple");
        navegator.findElement(By.xpath("/html/body/div/form/input[2]")).click();
        navegator.findElement(By.xpath("/html/body/div/form/input[2]")).sendKeys("99993");
        navegator.findElement(By.xpath("/html/body/div/form/input[3]")).click();
        navegator.findElement(By.xpath("/html/body/div/form/input[3]")).sendKeys("100");
        navegator.findElement(By.xpath("/html/body/div/form/input[4]")).click();
        navegator.findElement(By.xpath("/html/body/div/form/input[4]")).sendKeys("1000");

        WebElement selectElement = navegator.findElement(By.xpath("//*[@id=\"productCategory\"]"));
        Select select = new Select(selectElement);
        select.selectByValue("CELULARES");

        navegator.findElement(By.xpath("/html/body/div/form/button")).click();

        navegator.quit();
    }
}
