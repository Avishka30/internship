package com.sentura.internship.model;

public class Country {
    private String name;
    private String capital;
    private String region;
    private long population;
    private String flag;

    // Add standard Getters and Setters here (or use @Data if you added Lombok)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public long getPopulation() { return population; }
    public void setPopulation(long population) { this.population = population; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
}