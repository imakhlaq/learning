using Entities;
using Entities.DbContext;
using Microsoft.EntityFrameworkCore;
using serviceContracts;
using serviceContracts.DTO;
using Services.Enums;
using services.Helpers;

namespace services;

public class PersonService : IPersonService
{
    private readonly AppDbContext _db;

    public PersonService(AppDbContext context)
    {
        _db = context;
    }

    public async Task<List<PersonResponse>> GetSortedPersons(string sortedBy, SortedOrder sortedOrder = SortedOrder.ASC)
    {
        if (sortedBy == null) throw new ArgumentNullException();


        var personsInSorted = await _db.Persons
            .OrderByDescending(p => p.Name)
            .ToListAsync();

        var mapToRes = personsInSorted
            .Select(e => e.ToPersonResponse())
            .ToList();

        return mapToRes;
    }


    public async Task<PersonResponse> AddPerson(PersonAddRequest personAddRequest)
    {
        if (personAddRequest == null) throw new ArgumentNullException();

        //check if already exits
        var personAlreadyPresent = await _db.Persons
            .Include("Country")
            .FirstOrDefaultAsync(p => p.Email == personAddRequest.Email);

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
        await _db.Persons.AddAsync(person);

        await _db.SaveChangesAsync();

        return person.ToPersonResponse();
    }

    public async Task<List<PersonResponse>> GetAllPerson()
    {
        //getting all person from db and converting it to Person Response
        var allPersons = await _db.Persons
            .ToListAsync();

        return allPersons
            .Select(p => p.ToPersonResponse())
            .ToList();
    }

    public async Task<PersonResponse?> GetPersonById(Guid id)
    {
        if (id == Guid.Empty) return null;

        var person = await _db.Persons
            .Include("Country")
            .FirstOrDefaultAsync(p => p.Id == id); //default will return null
        if (person == null) return null;

        return person.ToPersonResponse();
    }

    public async Task<List<PersonResponse>> GetFilterPersons(string searchBy, string? searchString)
    {
        if (string.IsNullOrEmpty(searchBy)) return await GetAllPerson();

        switch (searchBy)
        {
            case nameof(Person.Name):
            {
                //is search string is not provided
                if (searchString == null)
                {
                    var data = await _db.Persons.ToListAsync();
                    return data.Select(p => p.ToPersonResponse()).ToList();
                }


                //checking db for data with the searchString
                return _db.Persons
                    .Where(p => p.Name.Contains(searchString))
                    .Select(s => s.ToPersonResponse()).ToList();
            }

            case nameof(Person.Email):
            {
                //is search string is not provided
                if (searchString == null)
                    return _db.Persons.ToList()
                        .Select(p => p.ToPersonResponse()).ToList();

                //checking db for data with the searchString
                return _db.Persons
                    .Where(p => p.Email.Contains(searchString))
                    .Select(s => s.ToPersonResponse()).ToList();
            }
            case nameof(Person.Address):
            {
                //is search string is not provided
                if (searchString == null)
                    return _db.Persons
                        .ToList()
                        .Select(p => p.ToPersonResponse()).ToList();

                //checking db for data with the searchString
                var data = await _db.Persons
                    .Where(p => p.Address.Contains(searchString))
                    .ToListAsync();

                return data.Select(e => e.ToPersonResponse()).ToList();
            }
            default:
            {
                //return all person
                return await GetAllPerson();
            }
        }
    }

    public async Task<PersonResponse> UpdatePerson()
    {
        throw new NotImplementedException();
    }

    public async Task<Guid?> DeletePerson(Guid id)
    {
        throw new NotImplementedException();
    }
}