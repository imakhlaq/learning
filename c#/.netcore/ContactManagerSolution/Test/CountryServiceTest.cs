using serviceContracts;
using serviceContracts.DTO;
using services;
using Xunit.Abstractions;

namespace test;

public class CountryServiceTest
{
    private readonly ICountryService _countryService;
    private readonly ITestOutputHelper _testOutputHelper; //to write output in the test windows

    public CountryServiceTest(ITestOutputHelper testOutputHelper)
    {
        _countryService = new CountriesService();
        _testOutputHelper = testOutputHelper;


        //to write
        _testOutputHelper.WriteLine("STARTED TEST"); //it takes string as input
    }

    //when CountryAddRequest is null, it should throw ArgumentNullException
    [Fact]
    public void TestAddCountry_CountryAddRequestIsNull()
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
    public void TestAddCountry_CountryNameIsNull()
    {
        var country = new CountryAddRequest();

        Assert.Throws<ArgumentException>(() => { _countryService.AddCountry(country); });
    }

    //when the CountryName already exits in the db, it should throw ArgumentException
    [Fact]
    public void TestAddCountry_CountryAlreadyExist()
    {
        var country1 = new CountryAddRequest { CountryName = "USA" };
        var country2 = new CountryAddRequest { CountryName = "USA" };

        Assert.Throws<ArgumentException>(() =>
        {
            _countryService.AddCountry(country1);
            _countryService.AddCountry(country2);
        });
    }


    //when you supply proper arguments it should return CountryResponse
    [Fact]
    public void TestAddCountry_SuccessResult()
    {
        var country = new CountryAddRequest { CountryName = "India" };

        var res = _countryService.AddCountry(country);

        //checking country id is generated
        Assert.True(res.id != Guid.Empty);
    }


    [Fact]
    public void TestGetAllCountry_ByAddingCountry()
    {
        List<CountryAddRequest> countryAddRequests = new()
        {
            new CountryAddRequest { CountryName = "japan" },
            new CountryAddRequest { CountryName = "usa" }
        };

        List<CountryResponse> countryResponses = new();

        //adding countries
        foreach (var request in countryAddRequests) countryResponses.Add(_countryService.AddCountry(request));

        //getting all countries
        var resList = _countryService.GetAllCountry();

        foreach (var response in resList)
            //contains calls Equal(Object obj) to check equality, override equal mehtod
            Assert.Contains(response, countryResponses);
    }

    [Fact]
    public void TestGetAllCountry()
    {
        var countryResponses = _countryService.GetAllCountry();

        Assert.NotNull(countryResponses);
    }

    #region GetCountryById

    [Fact]
    public void TestGetCountryById_IdIsNull()
    {
        Assert.Throws<ArgumentNullException>(() => _countryService.GetCountryById(Guid.Empty));
    }

    [Fact]
    public void TestGetCountryById_IdIsNotNull()
    {
        var country = new CountryAddRequest { CountryName = "India" };
        var addCountryRes = _countryService.AddCountry(country);

        var getByIdRes = _countryService.GetCountryById(addCountryRes.id);

        //to check the equality it calls the Equals() method
        Assert.Equal(addCountryRes, getByIdRes);
    }

    #endregion
}