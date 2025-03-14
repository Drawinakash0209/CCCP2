<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="min-h-screen flex justify-center items-center">
        <div class="max-w-screen-xl sm:w-8/12 lg:w-6/12 xl:w-4/12 bg-white shadow-lg sm:rounded-lg p-6">
            <div class="text-center">
                <img src="images/logo.png" class="mx-auto" alt="Logo">
            </div>
            <form action="register" method="post">
                <div class="mb-6">
                    <input type="text" name="username" class="w-full p-4 rounded-lg bg-gray-100 border border-gray-300 placeholder-gray-500" placeholder="Username" required>
                </div>
                <div class="mb-6">
                    <input type="password" name="password" class="w-full p-4 rounded-lg bg-gray-100 border border-gray-300 placeholder-gray-500" placeholder="Password" required>
                </div>
                <div class="mb-6">
                    <button type="submit" class="w-full py-3 bg-green-400 text-white rounded-lg hover:bg-green-700 focus:outline-none transition duration-300">Register</button>
                </div>
            </form>
            <div class="my-6 text-center">
                <span class="text-gray-600"><a href="login.jsp" class="text-gray-500 underline">Already have an account? Sign in</a></span>
            </div>
            <div class="text-center mt-6">
                <p class="text-xs text-gray-600">
                    By registering, you agree to the 
                    <a href="#" class="text-gray-500 underline">Terms of Service</a> 
                    and 
                    <a href="#" class="text-gray-500 underline">Privacy Policy</a>.
                </p>
            </div>
            <% if (request.getAttribute("registrationSuccess") != null) { %>
                <p class="text-green-500 text-center mt-4"><%= request.getAttribute("registrationSuccess") %></p>
            <% } %>
            <% if (request.getAttribute("registrationError") != null) { %>
                <p class="text-red-500 text-center mt-4"><%= request.getAttribute("registrationError") %></p>
            <% } %>
        </div>
    </div>
</body>
</html>
