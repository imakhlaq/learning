using ContactManager.Entities.AppDbContext;
using ContactManager.ServiceContracts;
using ContactManager.Services;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllersWithViews(); // registers controllers and views as Bean in IOC container

//adding services to IOC
builder.Services.AddScoped<PersonService, PersonService>();
builder.Services.AddScoped<ICountryService, CountriesService>();

//you cant add scoped (DbContext) service in a singleton service
//builder.Services.AddSingleton<ICountryService, CountriesService>();

//adding Db context to IOC
//now you can inject the DBContext in any bean manage by IOC to perform operations on Entities
//DBContext is a scoped service
builder.Services.AddEntityFrameworkNpgsql().AddDbContext<AppDbContext>(options =>
{
    var connectionString = builder.Configuration["ConnectionStrings:PostgresConnection"];
    options.UseNpgsql(connectionString);
});

/*
 In case of mySQL
 builder.ContactManager.Services.AddDbContext<MyDbContext>(options =>
{
    var connectionString = builder.Configuration["ConnectionStrings:PostgresConnection"];
    options.UseSqlServer(connectionString);
});
 */

var app = builder.Build();

if (builder.Environment.IsDevelopment())
    app.UseDeveloperExceptionPage(); //if current environment is development then use developer exception page

app.UseStaticFiles(); // to server static files from wwwroot folder
app.UseRouting(); //enable routing in the application to route the request to the appropriate controller
app.MapControllers(); //enable routing to the controllers


app.Run();