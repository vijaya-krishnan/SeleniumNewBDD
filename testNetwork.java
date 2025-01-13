import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Optional;

public class MockResponseExample {
    public static void main(String[] args) {
        // Set up WebDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // Create a DevTools session
        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();

        // Enable network interception
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Intercept network requests and mock responses
        devTools.addListener(Network.requestIntercepted(), request -> {
            devTools.send(Network.continueInterceptedRequest(
                    request.getInterceptionId(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of("Mock response body"),
                    Optional.of(200),  // HTTP status code
                    Optional.of("OK"), // Status text
                    Optional.empty()
            ));
        });

        // Set the interception pattern
        devTools.send(Network.setRequestInterception(
                Optional.of(List.of(new Network.RequestPattern(
                        Optional.of("*"), // Intercept all requests
                        Optional.empty(),
                        Optional.empty()
                )))
        ));

        // Navigate to a website
        driver.get("https://example.com");

        // Close the browser
        driver.quit();
    }
}
