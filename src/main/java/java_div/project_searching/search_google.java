package java_div.project_searching;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class search_google {

    public static void main(String[] args) {
        String searchQuery = "imobiliaria centro esteio rio grande do sul"; // SEARCH TERM

        String encodedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);   
        int linkCount = 0;
        int countPages = 1;
        System.out.println("Links found:");

        // Loop to go through multiple pages
        while (countPages < 10) {

            // Construct Google Search URL for the given page
            String googleSearchURL = "https://www.google.com/search?q=" + encodedQuery + "&start=" + (countPages * 10);

            try {
                // Jsoup connection to fetch the page
                Document doc = Jsoup.connect(googleSearchURL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(10000)
                        .get();

                // Select div elements that contain data-id attribute
                Elements divsWithLinks = doc.select("div[jscontroller][data-id]");

                for (Element div : divsWithLinks) {
                    // Get the value of the data-id attribute, which contains the link
                    String dataId = div.attr("data-id");

                    // Check if the data-id contains the expected format "atritem-"
                    if (dataId.contains("atritem-")) {
                        // Clean the link by extracting the URL portion from the data-id
                        String[] parts = dataId.split("atritem-");
                        if (parts.length > 1) {
                            String link = parts[1];  // Take the part after "atritem-"

                            // Print and limit to 20 links
                            if (linkCount < 100) {
                                System.out.println(link);
                                linkCount++;
                            } else {
                                break; // Stop once we reach 20 links
                            }
                        }
                    }
                }
                countPages++; // Increment to go to the next page
            } catch (IOException e) {
                System.err.println("Error connecting: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
