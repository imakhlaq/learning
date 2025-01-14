using serviceContracts;
using serviceContracts.DTO;
using services;

namespace test;

public class CountryServiceTest
{
    private ICountryService _countryService;

    public CountryServiceTest()
    {
        this._countryService = new CountriesService();
    }
    
    //when CountryAddRequest is null, it should throw ArgumentNullException
    [Fact]
    public void TestCountryAddRequestIsNull()
    {
        //arrange
        CountryAddRequest? countryAddRequest = null;


        //assert
        Assert.Throws<ArgumentNullException>(() =>
        {
            //act
            return _countryService.AddCountry(countryAddRequest);
        });
    }
    
    
    //when the CountryName is null, it should throw ArgumentException

    [Fact]
    public void TestCountryNameIsNull()
    {
        var country = new CountryAddRequest();

        Assert.Throws<ArgumentException>(() =>
        {
            _countryService.AddCountry(country);
        });
    }
    
    //when the CountryName already exits in the db, it should throw ArgumentException
    [Fact]
    public void TestCountryAlreadyExist()
    {
        var country1 = new CountryAddRequest() { CountryName = "USA" };
        var country2 = new CountryAddRequest() { CountryName = "USA" };

        Assert.Throws<ArgumentException>(() =>
        {
            _countryService.AddCountry(country1);
            _countryService.AddCountry(country2);
        });


    }
    
    
    //when you supply proper arguments it should return CountryResponse
    [Fact]
    public void TestSuccessResult()
    {
        var country = new CountryAddRequest() { CountryName = "India" };

        var res = _countryService.AddCountry(country);
        
        //checking country id is generated
        Assert.True(res.id!=Guid.Empty);
    }
}