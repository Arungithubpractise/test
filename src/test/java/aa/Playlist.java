package aa;

import static io.restassured.RestAssured.given;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class Playlist {

	@Test

	public static void main(String[] args) throws InterruptedException {

		// generating Authorization code with Authorization url

		//ChromeOptions ops = new ChromeOptions();
		//ops.addArguments("--remote-allow-origins=*");

		//WebDriver driver = new ChromeDriver(ops);
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		ChromeOptions ops = new ChromeOptions();
		ops.addArguments("--remote-allow-origins=*");
		ops.addArguments("--headless", "--disable-gpu",String.format("--window-size=%d,%d", (int)size.getWidth(),(int)size.getHeight()));
        ops.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
		
        WebDriver driver = new ChromeDriver(ops);

driver.get("https://accounts.spotify.com/authorize?client_id=905f333391b643669dfdf6b0f98bc5ee&redirect_uri=https://open.spotify.com/&state=karnataka&scope=playlist-modify-public playlist-read-private playlist-modify-private&response_type=code");

		driver.manage().window().maximize();
		driver.findElement(By.id("login-username")).sendKeys("arunkumarn8105@gmail.com");
		driver.findElement(By.id("login-password")).sendKeys("Arun@12345");
		driver.findElement(By.xpath("//span[text()='Log In']")).click();

		Thread.sleep(3000);
		
		String url = driver.getCurrentUrl();

		String partialcode = url.split("code=")[0];

		String code = partialcode.split("&state")[0];
		System.out.println(code);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// generating token with Token url

		RestAssured.baseURI = "https://open.spotify.com/";

		String response = given().urlEncodingEnabled(false)
				.queryParams("client_id", "905f333391b643669dfdf6b0f98bc5ee")
				.queryParams("client_secret", "29d45b172e58470c99d221da20abe5d9")
				.queryParams("grant_type", "client_credentials")

				// .when().log().all()
				.post("https://accounts.spotify.com/api/token").asString();

		JsonPath jsonPath = new JsonPath(response);
		String accessToken = jsonPath.getString("access_token");
		System.out.println(accessToken);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//create playlist

		String r2 = given().auth().oauth2(accessToken)
				.header("Authorization", "Basic OTA1ZjMzMzM5MWI2NDM2NjlkZmRmNmIwZjk4YmM1ZWU=:MjlkNDViMTcyZTU4NDcwYzk5ZDIyMWRhMjBhYmU1ZDk=")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.body("{\r\n"
						+ "    \"name\": \"Arun Playlist\",\r\n"
						+ "    \"description\": \"Arun fovourite songs\",\r\n"
						+ "    \"public\": false\r\n"
						+ "}")
				.when().post("https://api.spotify.com/v1/users/317gyhsluh6xwyu23hi6clu3yzcu/playlists").asString();
		System.out.println(r2);
		
		driver.quit();
	
	
		// get playlist
		
	/*String r2 = given().auth().oauth2(accessToken)
			//.header("Authorization", "Bearer +accessToken")			
			.when().get("https://api.spotify.com/v1/playlists/1xRfaq2Dy9c892xbHkt6A1").asString();
	System.out.println("Get playlist" +r2);*/
	
	
	// Add items to playlist
	
	/*String resp =given().auth().oauth2(accessToken)
	//	.header("Authorization", "Bearer +accessToken")
	.header("Content-Type", "application/json")
	.body("{\r\n"
			+ "    \"uris\": [\r\n"
			+ "        \"string\"\r\n"
			+ "    ],\r\n"
			+ "    \"position\": 0\r\n"
			+ "}")
	.when().post("https://api.spotify.com/v1/playlists/1xRfaq2Dy9c892xbHkt6A1/tracks?uris=spotify:track:4UMIv5jd9gK98a39BQRD9X,spotify:track:5aMLADD1Ho6Ogq8s8mIzB9").asString();
	
	System.out.println("add items to playlist" +resp);*/

	
	driver.quit();
}

}
