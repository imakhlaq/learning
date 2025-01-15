using ContactManager.Entities;
using ContactManager.Entities.AppDbContext;
using ContactManager.RepositoryContracts;

namespace ContactManager.Repository;

public class CountryRepository : ICountryRepository
{
    private readonly AppDbContext _db;

    public CountryRepository(AppDbContext dbContext)
    {
        _db = dbContext;
    }

    public async Task<Country> AddCountry(Country country)
    {
        _db.Countries.Add(country);
        await _db.SaveChangesAsync(); //commiting the changes
        return country;
    }

    public async Task<List<Country>> GetAllPerson()
    {
        throw new NotImplementedException();
    }

    public async Task<Country>? GetPersonById()
    {
        throw new NotImplementedException();
    }
}