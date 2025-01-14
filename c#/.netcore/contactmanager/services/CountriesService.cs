using Entities;
using serviceContracts;
using serviceContracts.DTO;

namespace services;

public class CountriesService : ICountryService
{
    private readonly List<Country> _countries;

    public CountriesService()
    {
        this._countries = new List<Country>();
    }


    public Country AddCountry(CountryAddRequest countryAddRequest)
    {

        if (countryAddRequest==null) throw new ArgumentNullException();
        if (countryAddRequest.CountryName==null)throw new ArgumentException();
        
        //convert CountryEntity from CountryAddRequest
        var countryFromReq = countryAddRequest.ToCountryEntity();

        //checking if country already exists
        var isCountryExist = _countries.Any(country => country.name == countryFromReq.name);

        if (isCountryExist) throw new ArgumentException();

        countryFromReq.id = Guid.NewGuid();

        //Add country to db
        _countries.Add(countryFromReq);
        
        return countryFromReq.ToCountryResponse();
    }
}