﻿using System.ComponentModel.DataAnnotations;

namespace Entities;

public class Person
{
    [Key] public Guid Id { get; set; }

    [StringLength(40)] public string Name { get; set; }

    [StringLength(40)] public string Email { get; set; }

    public DateTime? DateOfBirth { get; set; }

    [StringLength(10)] public string? Gender { get; set; }

    public Guid? CountryId { get; set; }

    [StringLength(250)] public string? Address { get; set; }

    public bool? ReceiveNewsLetters { get; set; }
}