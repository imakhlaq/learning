//from c# 9.0 u can write top level statement you don't need static main it will be generated at run time 
//-----------------------------------------|= this args comes from the main method args
var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapGet("/", async (HttpContext httpContext) =>
{
	

	httpContext.Request.Headers["my-header"] = "dadadadad"; //adding headers

	await httpContext.Response.WriteAsync("Hello"); //writing async method will be stopped till this line completes

	httpContext.Request.Headers["Content-Type"] = "text/html";//most of the time will be set automatically
	await httpContext.Response.WriteAsync("<h1>Hello</h1>"); //sending html

});

app.Run();
