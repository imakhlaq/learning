using serviceContracts.DTO;
using Services.Enums;

namespace serviceContracts;

public interface IPersonService
{
    PersonResponse AddPerson(PersonAddRequest personAddRequest);
    PersonResponse? GetPersonById(Guid id);
    List<PersonResponse> GetAllPerson();

    /**
     * Testing summary
     * <param name="searchBy">On which row we need to search</param>
     * <param name="searchString">Search string</param>
     * <returns></returns>
     */
    List<PersonResponse> GetFilterPersons(string searchBy, string? searchString);

    List<PersonResponse> GetSortedPersons(string sortedBy, SortedOrder sortedOrder);

    PersonResponse UpdatePerson();

    /**
     * <param name="id">Id of person you want to delete.</param>
     */
    Guid? DeletePerson(Guid id);
}