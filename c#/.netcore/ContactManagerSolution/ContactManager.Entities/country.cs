using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ContactManager.Entities;

/**
 * Domain Model for Country
 */
[Table("student_table")]
public class Country
{
    //one-to-many relation
    public List<Person>? Persons;

    [Key] public Guid Id { get; set; }

    [Column("country_name", TypeName = "varchar")]
    [MaxLength(30)]
    //this will be the column name and datatype in table
    public string Name { get; init; }

    public override bool Equals(object? obj)
    {
        if (obj == null) return false;
        var country2 = (Country)obj;
        return Id == country2.Id;
    }
}