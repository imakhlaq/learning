using ContactManager.ServiceContracts;
using Microsoft.AspNetCore.Mvc;

namespace ContactManager.Api.Controllers;

[Controller]
public class PersonController : Controller
{
    private readonly IPersonService _personService;

    public PersonController(IPersonService personService) //injected by IOCs
    {
        _personService = personService;
    }

    [HttpGet("/get-all-person")]
    public async Task<IActionResult> GetAllPerson()
    {
        return Json(await _personService.GetAllPerson());
    }

    [HttpPost("/add-person")]
    public async Task<IActionResult> AddPerson()
    {
        return null;
    }
}

