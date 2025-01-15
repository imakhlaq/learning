using ContactManager.ServiceContracts.DTO;

namespace ContactManager.ServiceContracts;

public interface ICountryService
{
    //service receives DTOS
    Task<CountryResponse> AddCountry(CountryAddRequest countryAddRequest);
    Task<List<CountryResponse>> GetAllCountry();

    /**
     * It returns a country from db.
     * <param name="id">Id (guid) to be searched</param>
     * <returns></returns>
     */
    Task<CountryResponse?> GetCountryById(Guid id);
}