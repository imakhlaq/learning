using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ContactManager.Entities;

public class Person
{
    [Key] public Guid Id { get; set; }

    [Column("person_name", TypeName = "varchar")]
    public string Name { get; set; }

    [Column("email", TypeName = "varchar")]
    [MaxLength(30)]
    public string Email { get; set; }

    public DateTime? DateOfBirth { get; set; }

    [Column("email", TypeName = "varchar")]
    [MinLength(4)]
    [MaxLength(6)]
    public string? Gender { get; set; }


    [Column("address", TypeName = "varchar")]
    [MaxLength(150)]
    public string? Address { get; set; }

    [Column("receive_news_letters", TypeName = "varchar")]
    [DefaultValue(false)]
    public bool? ReceiveNewsLetters { get; set; }

    [Column("country_id")] public Guid? CountryId { get; set; } //to store foreign keys

    [ForeignKey("CountryId")] public Country? Country { get; set; }
}