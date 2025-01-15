using ContactManager.Entities;

namespace ContactManager.RepositoryContracts;

public interface IPersonRepository
{
    Task<List<Person>> GetAllPerson();
    Task<Person>? GetPersonById();
}