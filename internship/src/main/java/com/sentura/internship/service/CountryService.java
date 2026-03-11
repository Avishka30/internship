package com.sentura.internship.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentura.internship.model.Country;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable("countries")
    public List<Country> getAllCountries() {
        // Fetch Countries using the specific fields query to optimize payload
        String url = "https://restcountries.com/v3.1/all?fields=name,capital,region,population,flags";
        List<Country> countries = new ArrayList<>();

        try {
            // BULLETPROOF FIX: Fetch as a raw String first
            String response = restTemplate.getForObject(url, String.class);

            if (response != null) {
                // Manually parse the String into a JSON tree
                JsonNode rootNode = objectMapper.readTree(response);

                if (rootNode.isArray()) {
                    for (JsonNode node : rootNode) {
                        Country c = new Country();
                        c.setName(node.path("name").path("common").asText("Unknown"));
                        c.setRegion(node.path("region").asText(""));
                        c.setPopulation(node.path("population").asLong(0));

                        if (node.path("capital").isArray() && !node.path("capital").isEmpty()) {
                            c.setCapital(node.path("capital").get(0).asText());
                        } else {
                            c.setCapital("N/A");
                        }

                        if (node.path("flags").has("svg")) {
                            c.setFlag(node.path("flags").path("svg").asText(""));
                        } else {
                            c.setFlag("");
                        }
                        countries.add(c);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching or parsing country data: " + e.getMessage());
        }

        return countries;
    }

    // Refresh every 10 minutes (600,000 milliseconds)
    @CacheEvict(value = "countries", allEntries = true)
    @Scheduled(fixedRate = 600000)
    public void refreshCache() {
        System.out.println("Cache cleared. Will fetch fresh data on next request.");
    }
}