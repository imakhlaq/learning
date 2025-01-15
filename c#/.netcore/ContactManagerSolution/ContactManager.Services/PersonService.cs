using Entities;
using serviceContracts;
using serviceContracts.DTO;
using Services.Enums;
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
        if (person == null) return null;

        return person.ToPersonResponse();
    }

    public List<PersonResponse> GetFilterPersons(string searchBy, string? searchString)
    {
        if (string.IsNullOrEmpty(searchBy)) return GetAllPerson();

        switch (searchBy)
        {
            case nameof(Person.Name):
            {
                //is search string is not provided
                if (searchString == null) return _persons.Select(p => p.ToPersonResponse()).ToList();

                //checking db for data with the searchString
                return _persons.Where(p => p.Name.Contains(searchString))
                    .Select(s => s.ToPersonResponse()).ToList();
            }

            case nameof(Person.Email):
            {
                //is search string is not provided
                if (searchString == null) return _persons.Select(p => p.ToPersonResponse()).ToList();

                //checking db for data with the searchString
                return _persons.Where(p => p.Email.Contains(searchString))
                    .Select(s => s.ToPersonResponse()).ToList();
            }
            case nameof(Person.Address):
            {
                //is search string is not provided
                if (searchString == null) return _persons.Select(p => p.ToPersonResponse()).ToList();

                //checking db for data with the searchString
                return _persons.Where(p => p.Address.Contains(searchString))
                    .Select(s => s.ToPersonResponse()).ToList();
            }
            default:
            {
                //return all person
                return GetAllPerson();
            }
        }
    }

    public List<PersonResponse> GetSortedPersons(string sortedBy, SortedOrder sortedOrder = SortedOrder.ASC)
    {
        if (sortedBy == null) throw new ArgumentNullException();


        var personsInSorted = _persons.OrderByDescending(p => p.Name)
            .Select(p => p.ToPersonResponse())
            .ToList();

        return personsInSorted;
    }

    public PersonResponse UpdatePerson()
    {
        throw new NotImplementedException();
    }

    public Guid? DeletePerson(Guid id)
    {
        throw new NotImplementedException();
    }
}