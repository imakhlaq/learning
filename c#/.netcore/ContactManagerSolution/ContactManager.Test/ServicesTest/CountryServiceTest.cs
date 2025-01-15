using ContactManager.ServiceContracts;
using ContactManager.ServiceContracts.DTO;
using ContactManager.Services;
using Xunit.Abstractions;

namespace test;

public class CountryServiceTest
{
    private readonly ICountryService _countryService;
    private readonly ITestOutputHelper _testOutputHelper; //to write output in the test window


    public CountryServiceTest(ITestOutputHelper testOutputHelper)
    {
        _countryService = new CountriesService(null);
        _testOutputHelper = testOutputHelper;


        //to write
        _testOutputHelper.WriteLine("STARTED TEST"); //it takes string as input
    }

    //when CountryAddRequest is null, it should throw ArgumentNullException
    [Fact]
    public async Task TestAddCountry_CountryAddRequestIsNull()
    {
        //arrange
        CountryAddRequest? countryAddRequest = null;


        //assert
        await Assert.ThrowsAsync<ArgumentNullException>(() =>
        {
            //act
            return _countryService.AddCountry(countryAddRequest);
        });
    }


    //when the CountryName is null, it should throw ArgumentException

    [Fact]
    public async Task TestAddCountry_CountryNameIsNull()
    {
        var country = new CountryAddRequest();

        await Assert.ThrowsAsync<ArgumentException>(() => _countryService.AddCountry(country));
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
    public async Task TestAddCountry_SuccessResult()
    {
        var country = new CountryAddRequest { CountryName = "India" };

        var res = await _countryService.AddCountry(country);

        //checking country id is generated
        Assert.True(res.Id != Guid.Empty);
    }


    [Fact]
    public async Task TestGetAllCountry_ByAddingCountry()
    {
        List<CountryAddRequest> countryAddRequests = new()
        {
            new CountryAddRequest { CountryName = "japan" },
            new CountryAddRequest { CountryName = "usa" }
        };

        List<CountryResponse> countryResponses = new();

        //adding countries
        foreach (var request in countryAddRequests) countryResponses.Add(await _countryService.AddCountry(request));

        //getting all countries
        var resList = await _countryService.GetAllCountry();

        foreach (var response in resList)
            //contains calls Equal(Object obj) to check equality, override equal mehtod
            Assert.Contains(response, countryResponses);
    }

    [Fact]
    public async Task TestGetAllCountry()
    {
        var countryResponses = await _countryService.GetAllCountry();

        Assert.NotNull(countryResponses);
    }

    #region GetCountryById

    [Fact]
    public async Task TestGetCountryById_IdIsNull()
    {
        await Assert.ThrowsAsync<ArgumentNullException>(() => _countryService.GetCountryById(Guid.Empty));
    }

    [Fact]
    public async Task TestGetCountryById_IdIsNotNull()
    {
        var country = new CountryAddRequest { CountryName = "India" };
        var addCountryRes = await _countryService.AddCountry(country);

        var getByIdRes = await _countryService.GetCountryById(addCountryRes.Id);

        //to check the equality it calls the Equals() method
        Assert.Equal(addCountryRes, getByIdRes);
    }

    #endregion
}