using Entities;
using serviceContracts;
using serviceContracts.DTO;
using services.Helpers;

namespace services;

public class PersonService : IPersonService
{
    private readonly List<Person> _persons;

    public PersonService()
    {
        _persons = new List<Person>();
    }


    public PersonResponse AddPerson(PersonAddRequest personAddRequest)
    {
        if (personAddRequest == null) throw new ArgumentNullException();

        //check if already exits
        var personAlreadyPresent = _persons.Find(p => p.Email == personAddRequest.Email);

        if (personAlreadyPresent != null) return personAlreadyPresent.ToPersonResponse();

        //validate the request param
        ValidatorHelper.ValidateRequest(personAddRequest);


        //add person to dB
        var person = new Person
        {
            Id = Guid.NewGuid(),
            Name = personAddRequest.Name,
            Email = personAddRequest.Email,
            DateOfBirth = personAddRequest.DateOfBirth,
            Gender = personAddRequest.Gender,
            Address = personAddRequest.Address,
            CountryId = personAddRequest.CountryId,
            ReceiveNewsLetters = personAddRequest.ReceiveNewsLetters
        };
        _persons.Add(person);

        return person.ToPersonResponse();
    }

    public List<PersonResponse> GetAllPerson()
    {
        //getting all person from db and converting it to Person Response
        var allPersons = _persons.Select(person => person.ToPersonResponse())
            .ToList();

        return allPersons;
    }

    public PersonResponse? GetPersonById(Guid id)
    {
        if (id == Guid.Empty) return null;

        var person = _persons.FirstOrDefault(p => p.Id == id); //default will return null
        return person.ToPersonResponse();
    }
}