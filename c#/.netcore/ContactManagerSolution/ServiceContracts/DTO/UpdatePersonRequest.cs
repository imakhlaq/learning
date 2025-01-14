namespace serviceContracts.DTO;

public class UpdatePersonRequest
{
    public Guid id { set; get; }
    public string Name { set; get; }
    public string Email { set; get; }
}