import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [countries, setCountries] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCountry, setSelectedCountry] = useState(null);

  // Fetch initial data from our Spring Boot backend
  useEffect(() => {
    fetch('http://localhost:8080/api/countries')
      .then(res => res.json())
      .then(data => {
        // Protect against backend errors returning objects instead of arrays
        if (Array.isArray(data)) {
          setCountries(data);
        } else {
          console.error("Backend did not return an array:", data);
          setCountries([]); // Fallback to an empty array to prevent crashes
        }
      })
      .catch(err => {
        console.error("Error fetching data: ", err);
        setCountries([]); // Also fallback here just in case
      });
  }, []);

  // Typing in search box filters results 
  const filteredCountries = countries.filter(country =>
    country.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container">
      <h1>Countries Directory</h1>
      
      {/* Search Input  */}
      <input
        type="text"
        placeholder="Search countries by name..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-input"
      />

      {/* Countries Table  */}
      <table className="countries-table">
        <thead>
          <tr>
            <th>Flag</th>
            <th>Name</th>
            <th>Capital</th>
            <th>Region</th>
            <th>Population</th>
          </tr>
        </thead>
        <tbody>
          {filteredCountries.map((country, index) => (
            // Click a row show popup/modal with details
            <tr key={index} onClick={() => setSelectedCountry(country)}>
              <td><img src={country.flag} alt={`${country.name} flag`} width="40" /></td>
              <td>{country.name}</td>
              <td>{country.capital}</td>
              <td>{country.region}</td>
              <td>{country.population.toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Country Details Modal  */}
      {selectedCountry && (
        <div className="modal-overlay" onClick={() => setSelectedCountry(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h2>{selectedCountry.name}</h2>
            <img src={selectedCountry.flag} alt="flag" width="150" />
            <p><strong>Capital:</strong> {selectedCountry.capital}</p>
            <p><strong>Region:</strong> {selectedCountry.region}</p>
            <p><strong>Population:</strong> {selectedCountry.population.toLocaleString()}</p>
            <button onClick={() => setSelectedCountry(null)} className="close-btn">Close</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;