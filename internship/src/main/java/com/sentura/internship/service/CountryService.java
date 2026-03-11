package com.sentura.internship.service;

import com.fasterxml.jackson.databind.JsonNode;
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

    @Cacheable("countries")
    public List<Country> getAllCountries() {
        // Fetch Countries [cite: 12] using the specific fields query to optimize payload
        String url = "https://restcountries.com/v3.1/all?fields=name,capital,region,population,flags";
        JsonNode[] response = restTemplate.getForObject(url, JsonNode[].class);
        List<Country> countries = new ArrayList<>();

        if (response != null) {
            for (JsonNode node : response) {
                Country c = new Country();
                c.setName(node.get("name").get("common").asText());
                c.setRegion(node.has("region") ? node.get("region").asText() : "");
                c.setPopulation(node.has("population") ? node.get("population").asLong() : 0);

                if (node.has("capital") && node.get("capital").isArray() && !node.get("capital").isEmpty()) {
                    c.setCapital(node.get("capital").get(0).asText());
                } else {
                    c.setCapital("N/A");
                }

                if (node.has("flags") && node.get("flags").has("svg")) {
                    c.setFlag(node.get("flags").get("svg").asText());
                } else {
                    c.setFlag("");
                }
                countries.add(c);
            }
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