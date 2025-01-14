using serviceContracts.DTO;

namespace serviceContracts;

public interface IPersonService
{
    PersonResponse AddPerson(PersonAddRequest personAddRequest);
    PersonResponse? GetPersonById(Guid id);
    List<PersonResponse> GetAllPerson();
}