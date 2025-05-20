<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Login</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="min-h-screen flex justify-center items-center">
        <div class="max-w-screen-xl sm:w-8/12 lg:w-6/12 xl:w-4/12 bg-white shadow-lg sm:rounded-lg p-6">
            <div class="text-center">
                <img src="images/logo.png" class="mx-auto" alt="Logo">
            </div>
            <form id="loginForm" onsubmit="handleLogin(event)">
                <div class="mb-6">
                    <input type="text" name="username" id="username" class="w-full p-4 rounded-lg bg-gray-100 border border-gray-300 placeholder-gray-500" placeholder="Username" required>
                </div>
                <div class="mb-6">
                    <input type="password" name="password" id="password" class="w-full p-4 rounded-lg bg-gray-100 border border-gray-300 placeholder-gray-500" placeholder="Password" required>
                </div>
                <div class="mb-6">
                    <button type="submit" id="signInButton" class="w-full py-3 bg-green-400 text-white rounded-lg hover:bg-green-700 focus:outline-none transition duration-300 flex justify-center items-center">
                        <span id="buttonText">Sign In</span>
                        <svg id="loadingSpinner" class="animate-spin h-5 w-5 ml-2 hidden" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8 8 8 0 01-8-8z"></path>
                        </svg>
                    </button>
                </div>
            </form>
            <div id="errorMessage" class="text-red-500 text-center mt-4 hidden"></div>
            <div class="my-6 text-center">
                <span class="text-gray-600"><a href="register.jsp">Or create an account</a></span>
            </div>
            <div class="text-center mt-6">
                <p class="text-xs text-gray-600">
                    By logging in, you agree to the 
                    <a href="#" class="text-gray-500 underline">Terms of Service</a> 
                    and 
                    <a href="#" class="text-gray-500 underline">Privacy Policy</a>.
                </p>
            </div>
        </div>
    </div>

    <script>
        async function handleLogin(event) {
            event.preventDefault(); // Prevent default form submission

            // Get form elements
            const form = document.getElementById('loginForm');
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const signInButton = document.getElementById('signInButton');
            const buttonText = document.getElementById('buttonText');
            const loadingSpinner = document.getElementById('loadingSpinner');
            const errorMessage = document.getElementById('errorMessage');

            // Disable button and show loading spinner
            signInButton.disabled = true;
            buttonText.textContent = 'Signing In...';
            loadingSpinner.classList.remove('hidden');
            errorMessage.classList.add('hidden');

            try {
                const response = await fetch('login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({
                        'username': username,
                        'password': password
                    })
                });

                if (response.redirected) {
                    // Handle successful redirect
                    window.location.href = response.url;
                    return;
                }

                // If not redirected, assume error
                const errorText = await response.text();
                // Extract error message from JSP if present Editorial note: This is a simple approach; you may need to parse the response more robustly
                const errorMatch = errorText.match(/<p class="text-red-500 text-center mt-4">(.*?)<\/p>/);
                const errorMsg = errorMatch ? errorMatch[1] : 'Login failed. Please try again.';
                throw new Error(errorMsg);
            } catch (error) {
                // Show error message
                errorMessage.textContent = error.message;
                errorMessage.classList.remove('hidden');
            } finally {
                // Re-enable button and hide loading spinner
                signInButton.disabled = false;
                buttonText.textContent = 'Sign In';
                loadingSpinner.classList.add('hidden');
            }
        }
    </script>
</body>
</html>