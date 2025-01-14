namespace Entities;

/// <summary>
///     Domain Model for Country
/// </summary>
public class Country
{
    public Guid id { get; set; }
    public string name { get; set; }


    public override bool Equals(object? obj)
    {
        var country2 = (Country)obj;
        return id == country2.id;
    }
}