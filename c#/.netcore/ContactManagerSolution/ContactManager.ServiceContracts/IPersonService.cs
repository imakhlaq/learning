using ContactManager.ServiceContracts.DTO;
using ContactManager.ServiceContracts.Enums;

namespace ContactManager.ServiceContracts;

public interface IPersonService
{
    Task<PersonResponse> AddPerson(PersonAddRequest personAddRequest);
    Task<PersonResponse?> GetPersonById(Guid id);
    Task<List<PersonResponse>> GetAllPerson();

    /**
     * Testing summary
     * <param name="searchBy">On which row we need to search</param>
     * <param name="searchString">Search string</param>
     * <returns></returns>
     */
    Task<List<PersonResponse>> GetFilterPersons(string searchBy, string? searchString);

    Task<List<PersonResponse>> GetSortedPersons(string sortedBy, SortedOrder sortedOrder);

    Task<PersonResponse> UpdatePerson();

    /**
     * <param name="id">Id of person you want to delete.</param>
     */
    Task<Guid?> DeletePerson(Guid id);
}