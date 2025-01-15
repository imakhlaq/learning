using ContactManager.Entities.AppDbContext;
using ContactManager.ServiceContracts;
using ContactManager.ServiceContracts.DTO;
using Microsoft.EntityFrameworkCore;

namespace ContactManager.Services;

public class CountriesService : ICountryService
{
    private readonly AppDbContext _db; //using this perform operations on db

    public CountriesService(AppDbContext context)
    {
        //context will be injected by IOC because we added it as service in context
        _db = context;
    }


    public async Task<CountryResponse> AddCountry(CountryAddRequest countryAddRequest)
    {
        if (countryAddRequest == null) throw new ArgumentNullException();
        if (countryAddRequest.CountryName == null) throw new ArgumentException();

        //convert CountryEntity from CountryAddRequest
        var countryFromReq = countryAddRequest.ToCountryEntity();

        //checking if country already exists
        var isCountryExist = await _db.Countries.AnyAsync(country => country.Name == countryFromReq.Name);

        if (isCountryExist) throw new ArgumentException();

        countryFromReq.Id = Guid.NewGuid();

        //Add country to db
        await _db.Countries.AddAsync(countryFromReq);

        await _db.SaveChangesAsync(); // to commit the changes;

        return countryFromReq.ToCountryResponse();
    }

    public async Task<List<CountryResponse>> GetAllCountry()
    {
        //selecting whole country record from dB
        var allCountries = await _db.Countries
            .ToListAsync();

        //converting all counties entities to country response
        var allCountryToResponse = allCountries
            .Select(c => c.ToCountryResponse()).ToList();


        return allCountryToResponse;
    }

    public async Task<CountryResponse?> GetCountryById(Guid id)
    {
        if (id == Guid.Empty) throw new ArgumentNullException();

        var country = await _db.Countries
            .FirstOrDefaultAsync(c => c.Id == id);

        if (country == null) return null;

        return country.ToCountryResponse();
    }
}