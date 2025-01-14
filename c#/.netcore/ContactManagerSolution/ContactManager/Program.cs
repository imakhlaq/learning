using serviceContracts;
using services;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllersWithViews(); // registers controllers and views as Bean in IOC container

//adding services to IOC
builder.Services.AddSingleton<IPersonService, PersonService>();
builder.Services.AddSingleton<ICountryService, CountriesService>();

var app = builder.Build();


if (builder.Environment.IsDevelopment())
    app.UseDeveloperExceptionPage(); //if current environment is development then use developer exception page

app.UseStaticFiles(); // to server static files from wwwroot folder
app.UseRouting(); //enable routing in the application to route the request to the appropriate controller
app.MapControllers(); //enable routing to the controllers


app.Run();