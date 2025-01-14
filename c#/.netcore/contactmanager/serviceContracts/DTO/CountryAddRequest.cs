﻿using Entities;

namespace serviceContracts.DTO;

public class CountryAddRequest
{
    public string CountryName { get; set; }

    //this will create and return CountryEntity from country DTO
    public Country ToCountryEntity()
    {
        return new() { id = Guid.NewGuid(), name = CountryName };
    }
}

