using Entities;
using serviceContracts;
using serviceContracts.DTO;

namespace services;

public class CountriesService : ICountryService
{
    private readonly List<Country> _countries;

    public CountriesService()
    {
        _countries = new List<Country>();
    }


    public CountryResponse AddCountry(CountryAddRequest countryAddRequest)
    {
        if (countryAddRequest == null) throw new ArgumentNullException();
        if (countryAddRequest.CountryName == null) throw new ArgumentException();

        //convert CountryEntity from CountryAddRequest
        var countryFromReq = countryAddRequest.ToCountryEntity();

        //checking if country already exists
        var isCountryExist = _countries.Any(country => country.name == countryFromReq.name);

        if (isCountryExist) throw new ArgumentException();

        countryFromReq.Id = Guid.NewGuid();

        //Add country to db
        _countries.Add(countryFromReq);

        return countryFromReq.ToCountryResponse();
    }

    public List<CountryResponse> GetAllCountry()
    {
        //selecting whole country record from dB
        var allCountries = _countries.Select(country => country).ToList();

        //converting all counties entities to country response
        var allCountryToResponse = allCountries.Select(c => c.ToCountryResponse()).ToList();


        return allCountryToResponse;
    }

    public CountryResponse? GetCountryById(Guid id)
    {
        if (id == Guid.Empty) throw new ArgumentNullException();

        var country = _countries.First(c => c.Id == id);

        return country.ToCountryResponse();
    }
}