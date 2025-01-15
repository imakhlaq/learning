using ContactManager.Entities;

namespace ContactManager.ServiceContracts.DTO;

public class CountryAddRequest
{
    public string CountryName { get; init; }

    //this will create and return CountryEntity from country DTO
    public Country ToCountryEntity()
    {
        return new Country { Id = Guid.NewGuid(), Name = CountryName };
    }
}