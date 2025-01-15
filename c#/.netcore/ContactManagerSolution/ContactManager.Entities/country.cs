using System.ComponentModel.DataAnnotations;

namespace Entities;

/// <summary>
///     Domain Model for Country
/// </summary>
public class Country
{
    [Key] public Guid Id { get; set; }

    [StringLength(40)] public string name { get; init; }


    public override bool Equals(object? obj)
    {
        if (obj == null) return false;
        var country2 = (Country)obj;
        return Id == country2.Id;
    }
}