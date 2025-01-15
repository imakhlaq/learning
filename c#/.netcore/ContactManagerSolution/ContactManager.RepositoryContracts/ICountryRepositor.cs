using ContactManager.Entities;

namespace ContactManager.RepositoryContracts;

public interface ICountryRepository
{
    //Repository receives the entity object not DTO
    Task<Country> AddCountry(Country country);
    Task<List<Country>> GetAllPerson();
    Task<Country>? GetPersonById();
}