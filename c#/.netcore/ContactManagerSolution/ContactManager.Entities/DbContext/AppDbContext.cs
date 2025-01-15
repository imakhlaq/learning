using System.Text.Json;
using Microsoft.EntityFrameworkCore;

namespace Entities.DbContext;

public class AppDbContext : Microsoft.EntityFrameworkCore.DbContext
{
    //options supplied while adding db context to IOC
    public AppDbContext(DbContextOptions options) : base(options)
    {
    }

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
        var countrySeedData = JsonSerializer.Deserialize<List<Country>>(countryJson);

        modelBuilder.Entity<Country>().HasData(countrySeedData);

        //fluent API
        modelBuilder.Entity<Person>()
            .Property(e => e.Email)
            .HasColumnName("email_column") //this will be the column name in table
            .HasColumnType("varchar(40)") //set column datatype in db
            .HasDefaultValue("text@email.com");

        //adding unique constraint and also setting up the index on email
        modelBuilder.Entity<Person>()
            .HasIndex(p => p.Email)
            .IsUnique();

        //adding check constraint (condition will be checked before insert and update)
        modelBuilder.Entity<Person>()
            .ToTable(t =>
                t.HasCheckConstraint("constraint_name", "len(email)>=5")
            );

        //Relations
        modelBuilder.Entity<Person>(entity =>
        {
            entity.HasOne<Country>(c => c.Country)
                .WithMany(p => p.Persons)
                .HasForeignKey(p => p.CountryId);
        });
    }
}