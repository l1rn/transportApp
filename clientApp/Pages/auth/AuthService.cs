using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.JSInterop;
using System.Net.Http.Json;

namespace clientApp.Pages.auth
{
    public interface IAuthService
    {
        Task LoginAsync(string username, string password);
        Task LogoutAsync();
    }
    public class AuthService : IAuthService
    {
        private readonly HttpClient _http;
        private readonly IJSRuntime _jSRuntime;
        private readonly AuthenticationStateProvider _authStateProvider;
        public AuthService(HttpClient http, IJSRuntime jSRuntime, AuthenticationStateProvider authStateProvider)
        {
            _http = http;
            _jSRuntime = jSRuntime;
            _authStateProvider = authStateProvider;
        }
        public async Task LoginAsync(string username, string password)
        {
            var response = await _http.PostAsJsonAsync("auth/sign-in",
                new
                {
                    Username = username,
                    Password = password
                });
            if (response.IsSuccessStatusCode)
            {
                var token = await response.Content.ReadAsStringAsync();
                await _jSRuntime.InvokeVoidAsync("localStorage.setItem", "authToken", token);
                ((CustomAuthStateProvider)_authStateProvider).NotifyUserAuthentication(token);
            }
        }
        public async Task LogoutAsync()
        {
            await _jSRuntime.InvokeVoidAsync("localStorage.removeItem", "authtoken");
            ((CustomAuthStateProvider)_authStateProvider).NotifyUserAuthentication(null);
        }
    }
}
