using Entities;

namespace serviceContracts.DTO;

/// <summary>
///     DTO class that is used as return after creating country
/// </summary>
public class CountryResponse
{
    public string CountryName;
    public Guid Id { get; set; }

    public CountryResponse CreateResponse(Country country)
    {
        return new CountryResponse { Id = country.Id, CountryName = country.Name };
    }

    public override bool Equals(object? obj)
    {
        var countryResponse2 = (CountryResponse)obj;
        return Id == countryResponse2.Id;
    }
}

//adding extension method to Country Entity
//so we can convert a Entity to response
public static class CountryExtensions
{
    public static CountryResponse ToCountryResponse(this Country country)
    {
        return new CountryResponse { Id = country.Id, CountryName = country.Name };
    }
}