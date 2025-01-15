using Entities.MyDbContext;
using Microsoft.EntityFrameworkCore;
using serviceContracts;
using services;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllersWithViews(); // registers controllers and views as Bean in IOC container

//adding services to IOC
builder.Services.AddSingleton<IPersonService, PersonService>();
builder.Services.AddSingleton<ICountryService, CountriesService>();

//adding Db context to IOC
//now you can inject the DBContext in any bean manage by IOC to perform operations on ContactManager.Entities.
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