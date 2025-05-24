
<%@page import="cccp.model.Batch"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="cccp.model.User" %>
<%
    // Retrieve the user object from the session
    User user = (User) session.getAttribute("user");

    // Basic check if user is logged in
    if (user == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return; // Stop further processing of the page
    }

    // Assuming User has a getUsername() method
    String username = user.getUsername();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Batch Management</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
<div x-data="setup()" :class="{ 'dark': isDark }">
    <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
        <!-- Header -->
        <jsp:include page="employee_dashboard_header.jsp" />

        <!-- Sidebar -->
        <jsp:include page="employee_dashboard_sidebar.jsp" />

        <!-- Main Content -->
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64 px-4">
            <div class="max-w-4xl mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Batch Management</h1>

                <!-- Message Div for Success/Error -->
                <div id="message" class="hidden mb-6 p-4 rounded-md"></div>

                <!-- Add Batch Form -->
                <div class="mb-6">
                    <form id="add-batch-form" class="w-full" @submit.prevent="addBatch">
                        <input type="hidden" name="action" value="create">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <input type="text" name="product_id" placeholder="Product ID" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <input type="text" name="batch_id" placeholder="Batch ID" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <input type="number" name="quantity" placeholder="Quantity" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <input type="date" name="purchase_date" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <input type="date" name="expiry_date" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                        </div>
                        <button type="submit" class="mt-4 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition duration-300 w-full md:w-auto">Add Batch</button>
                    </form>
                </div>

                <!-- Search Form -->
                <div class="flex justify-end mb-6">
                    <form id="search-batch-form" class="w-full md:w-1/3" @submit.prevent="searchBatch">
                        <input type="hidden" name="action" value="searchBatch">
                        <input x-model="productId" type="text" name="product_id" placeholder="Enter Product ID..." class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                        <button type="submit" class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300 w-full md:w-auto">Search</button>
                    </form>
                </div>

                <!-- Batches Table -->
                <div id="batch-table-container">
                    <%
                        List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
                        boolean hasData = batchList != null && !batchList.isEmpty();
                    %>
                    <% if (hasData) { %>
                        <div class="overflow-x-auto">
                            <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                                <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                                    <tr>
                                        <th class="px-6 py-3">Batch ID</th>
                                        <th class="px-6 py-3">Quantity</th>
                                        <th class="px-6 py-3">Purchase Date</th>
                                        <th class="px-6 py-3">Expiry Date</th>
                                    </tr>
                                </thead>
                                <tbody id="batch-table-body" class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                                    <% 
                                        for (Batch batch : batchList) {
                                    %>
                                        <tr class="hover:bg-gray-100 dark:hover:bg-gray-700">
                                            <td class="px-6 py-4"><%= batch.getBatchcode() %></td>
                                            <td class="px-6 py-4"><%= batch.getQuantity() %></td>
                                            <td class="px-6 py-4"><%= batch.getPurchaseDate() %></td>
                                            <td class="px-6 py-4"><%= batch.getExpiryDate() %></td>
                                        </tr>
                                    <% 
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <p class="text-center text-gray-500 dark:text-gray-400 italic mt-6">No batches found.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Alpine.js for dark mode toggle and functionality -->
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
            productId: '', // Track the last searched product ID
            async addBatch(event) {
                const form = event.target;
                const formData = new FormData(form);
                const messageDiv = document.getElementById('message');

                try {
                    const response = await fetch('/CCCP/BatchServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams(formData).toString()
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }

                    // Since BatchServlet redirects on success, we need to refresh the table
                    messageDiv.classList.remove('hidden', 'bg-red-100', 'text-red-700', 'bg-green-100', 'text-green-700');
                    messageDiv.textContent = 'Batch added successfully';
                    messageDiv.classList.add('bg-green-100', 'text-green-700');

                    // Update productId if a search was active
                    this.productId = formData.get('product_id');
                    await this.refreshBatchTable();
                    form.reset();
                    setTimeout(() => messageDiv.classList.add('hidden'), 2000);
                } catch (error) {
                    console.error('Add batch error:', error);
                    messageDiv.classList.remove('hidden', 'bg-green-100', 'text-green-700');
                    messageDiv.classList.add('bg-red-100', 'text-red-700');
                    messageDiv.textContent = 'Error: Failed to add batch - ' + error.message;
                }
            },
            async searchBatch(event) {
                const form = event.target;
                const formData = new FormData(form);
                this.productId = formData.get('product_id'); // Update productId
                await this.refreshBatchTable();
            },
            async refreshBatchTable() {
                const messageDiv = document.getElementById('message');
                try {
                    if (!this.productId) return; // Skip refresh if no product ID is set
                    const response = await fetch('/CCCP/BatchServlet?t=' + Date.now(), {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            'action': 'searchBatch',
                            'product_id': this.productId
                        })
                    });
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    const html = await response.text();
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const newTableContainer = doc.querySelector('#batch-table-container');
                    if (!newTableContainer) {
                        throw new Error('Table container not found in response');
                    }
                    document.getElementById('batch-table-container').innerHTML = newTableContainer.innerHTML;
                } catch (error) {
                    console.error('Error refreshing batch table:', error);
                }
            }
        };
    };

    // Start polling every 5 seconds
    document.addEventListener('alpine:init', () => {
        setInterval(() => {
            Alpine.data('setup')().refreshBatchTable();
        }, 5000);
    });
</script>
</body>
</html>
