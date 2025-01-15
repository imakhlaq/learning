using System.Text.Json;
using Microsoft.EntityFrameworkCore;

namespace Entities.MyDbContext;

public class AppDbContext : DbContext
{
    //one db set for each table
    public DbSet<Person> Persons { get; set; }
    public DbSet<Country> Countries { get; set; }


    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        //providing actual table names for entities
        modelBuilder.Entity<Country>().ToTable("countries");
        modelBuilder.Entity<Person>().ToTable("persons");


        //seed the data
        var personJson = File.ReadAllText("seed/Person.json");
        var personsSeedData = JsonSerializer.Deserialize<List<Person>>(personJson);

        modelBuilder.Entity<Person>().HasData(personsSeedData);

        var countryJson = File.ReadAllText("seed/Country.json");
        var countrySeedData = JsonSerializer.Deserialize<List<Person>>(countryJson);

        modelBuilder.Entity<Country>().HasData(countrySeedData);
    }
}