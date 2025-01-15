using ContactManager.ServiceContracts;
using Microsoft.AspNetCore.Mvc;
using serviceContracts;

namespace ContactManager.Api.Controllers;

[Controller]
[Route("/api/v1/country")]
public class CountryController : Controller
{
    private readonly ICountryService _countryService;

    public CountryController(ICountryService countryService) //injected by IOC
    {
        _countryService = countryService; //dependency injection
    }
}