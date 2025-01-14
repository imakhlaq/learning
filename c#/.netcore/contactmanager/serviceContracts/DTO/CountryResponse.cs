using Entities;

namespace serviceContracts.DTO;

/// <summary>
/// DTO class that is used as return after creating country
/// </summary>
public class CountryResponse
{
   public Guid id { get; set; }
   public string CountryName;

   public CountryResponse CreateResponse(Country country)
   {
      return new() { id = country.id, CountryName = country.name };
   }
}

//adding extension method to Country Entity
public static class CountryExtensions
{
   public static Country ToCountryResponse(this Country country)
   {

      return new() { id = country.id, name = country.name };
   }
}