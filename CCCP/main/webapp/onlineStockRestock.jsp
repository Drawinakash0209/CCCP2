<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return;
    }
    String username = user.getUsername();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restock Online Inventories</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
<div x-data="setup()" :class="{ 'dark': isDark }">
    <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
        <jsp:include page="employee_dashboard_header.jsp" />
        <jsp:include page="employee_dashboard_sidebar.jsp" />
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64 px-4">
            <div class="max-w-md mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Restock Online Inventories</h1>
                <div id="message" class="hidden mb-6 p-4 rounded-md text-center"></div>
                <form id="restock-online-form" class="space-y-4">
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="productId">Product ID</label>
                        <input class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                               id="productId" name="productId" type="text" required placeholder="Enter Product ID">
                    </div>
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="quantity">Quantity</label>
                        <input class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                               id="quantity" name="quantity" type="number" min="1" required placeholder="Enter quantity">
                    </div>
                    <div class="flex items-center justify-center">
                        <button class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300" type="submit">
                            Restock Online Inventories
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/gh/alpinejs/alpine@v2.8.0/dist/alpine.min.js" defer></script>
<script>
    const setup = () => {
        const getTheme = () => {
            if (window.localStorage.getItem('dark')) {
                return JSON.parse(window.localStorage.getItem('dark'));
            }
            return !!window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
        };
        const setTheme = (value) => {
            window.localStorage.setItem('dark', value);
        };
        return {
            isDark: getTheme(),
            toggleTheme() {
                this.isDark = !this.isDark;
                setTheme(this.isDark);
            },
        };
    };
    document.getElementById('restock-online-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const formData = new FormData(form);
        const messageDiv = document.getElementById('message');
        const urlEncodedData = new URLSearchParams(formData).toString();
        try {
            const response = await fetch('/CCCP/OnlineOrderServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: urlEncodedData
            });
            const result = await response.json();
            messageDiv.classList.remove('hidden', 'bg-red-100', 'text-red-700', 'bg-green-100', 'text-green-700');
            messageDiv.textContent = result.message;
            messageDiv.classList.add(result.success ? 'bg-green-100' : 'bg-red-100', result.success ? 'text-green-700' : 'text-red-700');
            if (result.success) {
                form.reset();
                setTimeout(() => {
                    messageDiv.classList.add('hidden');
                    window.location.href = '/CCCP/OnlineOrderServlet';
                }, 2000);
            }
        } catch (error) {
            messageDiv.classList.remove('hidden', 'bg-green-100', 'text-green-700');
            messageDiv.classList.add('bg-red-100', 'text-red-700');
            messageDiv.textContent = 'Error: Failed to restock online inventories';
        }
    });
</script>
</body>
</html>