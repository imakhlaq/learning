using ContactManager.Entities;
using ContactManager.RepositoryContracts;

namespace ContactManager.Repository;

public class PersonRepository : IPersonRepository
{
    public async Task<List<Person>> GetAllPerson()
    {
        throw new NotImplementedException();
    }

    public async Task<Person>? GetPersonById()
    {
        throw new NotImplementedException();
    }
}