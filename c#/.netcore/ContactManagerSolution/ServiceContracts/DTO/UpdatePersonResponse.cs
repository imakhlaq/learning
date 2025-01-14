namespace serviceContracts.DTO;

public class UpdatePersonResponse
{
    public Guid id { set; get; }
    public string Name { set; get; }
    public string Email { set; get; }
}