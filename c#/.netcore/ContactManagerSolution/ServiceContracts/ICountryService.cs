using serviceContracts.DTO;

namespace serviceContracts;

public interface ICountryService
{
    CountryResponse AddCountry(CountryAddRequest countryAddRequest);
    List<CountryResponse> GetAllCountry();

    /// <summary>
    ///     It returns a country from db.
    /// </summary>
    /// <param name="id">Id (guid) to be searched</param>
    /// <returns></returns>
    CountryResponse? GetCountryById(Guid id);
}