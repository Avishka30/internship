package com.sentura.internship.controller;

import com.sentura.internship.model.Country;
import com.sentura.internship.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "*") // Allows React frontend to communicate without CORS errors
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getCountries() {
        return countryService.getAllCountries();
    }

    // Search Countries: Should return filtered results
    @GetMapping("/search")
    public List<Country> searchCountries(@RequestParam String keyword) {
        return countryService.getAllCountries().stream()
                .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}