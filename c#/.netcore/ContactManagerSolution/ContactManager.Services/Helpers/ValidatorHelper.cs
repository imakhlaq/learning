using System.ComponentModel.DataAnnotations;

namespace ContactManager.Services.Helpers;

public static class ValidatorHelper
{
    public static void ValidateRequest(object obj)
    {
        //validate the Request object base on attributes [Required]
        var validationContext = new ValidationContext(obj);
        var validationResult = new List<ValidationResult>();
        var isValid = Validator.TryValidateObject(obj, validationContext, validationResult, true);

        if (!isValid) //there is at-least one error
            throw new ArgumentException(validationResult.FirstOrDefault()?.ErrorMessage);
    }
}