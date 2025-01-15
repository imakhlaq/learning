using System.ComponentModel.DataAnnotations;

namespace ContactManager.ServiceContracts.DTO;

public class PersonAddRequest
{
    [Required(ErrorMessage = "Person name can't be blank.")]
    public string Name { get; set; }

    [Required(ErrorMessage = "Email can't be blank.")]
    [EmailAddress(ErrorMessage = "Provided email is not valid email address.")]
    public string Email { get; set; }

    public DateTime? DateOfBirth { get; set; }
    public string? Gender { get; set; }
    public Guid? CountryId { get; set; }
    public string? Address { get; set; }
    public bool? ReceiveNewsLetters { get; set; }
}