using Microsoft.AspNetCore.Mvc;
using serviceContracts;

namespace contactmanager.Controllers;

[Controller]
public class PersonController : Controller
{
    private readonly IPersonService _personService;

    public PersonController(IPersonService personService) //injected by IOCs
    {
        _personService = personService;
    }

    [HttpGet("/get-all-person")]
    public IActionResult GetAllPerson()
    {
        return null;
    }

    [HttpPost("/add-person")]
    public IActionResult AddPerson()
    {
        return null;
    }
}