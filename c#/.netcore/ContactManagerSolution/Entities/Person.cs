using System.ComponentModel.DataAnnotations;

namespace Entities;

public class Person
{
    [Required] public Guid Id { get; set; }

    [Required] public string Name { get; set; }

    [Required] public string Email { get; set; }

    public DateTime? DateOfBirth { get; set; }
    public string? Gender { get; set; }
    public Guid? CountryId { get; set; }
    public string? Address { get; set; }
    public bool? ReceiveNewsLetters { get; set; }
}