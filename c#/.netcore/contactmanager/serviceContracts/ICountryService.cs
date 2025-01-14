using Entities;
using serviceContracts.DTO;

namespace serviceContracts;

public interface ICountryService
{
   Country AddCountry(CountryAddRequest countryAddRequest);
}
