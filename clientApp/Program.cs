using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using clientApp;
using System.Net.Http.Headers;
using clientApp.Pages.auth;
using Blazored.LocalStorage;
using Microsoft.AspNetCore.Components.Authorization;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");
builder.Services.AddBlazoredLocalStorage();
builder.Services.AddScoped<AuthenticationStateProvider, CustomAuthStateProvider>();
builder.Services.AddScoped<IAuthService, AuthService>();
builder.Services.AddScoped(sp =>
{
    var client = new HttpClient { BaseAddress = new Uri("http://localhost:8080/") };
    return client;
});
builder.Services.AddTransient<AuthInterceptor>();
builder.Services.AddHttpClient("SecureClient", client =>
{
    // Если Spring Boot работает на localhost:8080
    client.BaseAddress = new Uri("http://localhost:8080/");
}).AddHttpMessageHandler<AuthInterceptor>();

await builder.Build().RunAsync();
