using ContactManager.Entities;

namespace ContactManager.ServiceContracts.DTO;

public class PersonResponse
{
    public Guid Id { get; set; }

    public string Name { get; set; }

    public string Email { get; set; }

    public DateTime? DateOfBirth { get; set; }
    public string? Gender { get; set; }
    public Guid? CountryId { get; set; }
    public string? Address { get; set; }
    public bool? ReceiveNewsLetters { get; set; }

    public override bool Equals(object? obj)
    {
        if (obj == null) return false;

        var personResponse2 = obj as PersonResponse;
        return personResponse2?.Id == Id;
    }
}

//adding an extension method to the Person Entity
//so we can convert a Entity to response
public static class PersonExtension
{
    public static PersonResponse ToPersonResponse(this Person person)
    {
        return new PersonResponse
        {
            Id = person.Id,
            Name = person.Name,
            Email = person.Email,
            DateOfBirth = person.DateOfBirth,
            Gender = person.Gender,
            CountryId = person.CountryId,
            Address = person.Address,
            ReceiveNewsLetters = person.ReceiveNewsLetters
        };
    }
}