<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Bill, cccp.model.Bill.BillItem" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Bill</title>
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

            <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
                <div class="max-w-2xl mx-auto mt-10 bg-white shadow-lg rounded-lg overflow-hidden">
                    <div class="text-2xl py-4 px-6 bg-gray-900 text-white text-center font-bold uppercase">
                        Create New Bill
                    </div>
                    <form class="py-4 px-6 space-y-4" action="BillServlet" method="POST">
                        <div id="itemsContainer" class="space-y-4">
                            <div class="flex gap-4">
                                <input type="text" name="productId" placeholder="Product Code" required class="w-1/2 py-2 px-3 border rounded shadow focus:outline-none focus:shadow-outline text-gray-700">
                                <input type="number" name="quantity" placeholder="Quantity" min="1" required class="w-1/2 py-2 px-3 border rounded shadow focus:outline-none focus:shadow-outline text-gray-700">
                            </div>
                        </div>
                        <button type="button" onclick="addItemRow()" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Add Another Item</button>

                        <div>
                            <label for="discountRate" class="block font-bold mb-1 text-gray-700">Discount Rate (0-100%)</label>
                            <input type="number" id="discountRate" name="discountRate" min="0" max="100" step="0.01" value="0" class="w-full py-2 px-3 border rounded shadow focus:outline-none focus:shadow-outline text-gray-700">
                        </div>

                        <div>
                            <label for="cashTendered" class="block font-bold mb-1 text-gray-700">Cash Tendered</label>
                            <input type="number" id="cashTendered" name="cashTendered" min="0" step="0.01" required class="w-full py-2 px-3 border rounded shadow focus:outline-none focus:shadow-outline text-gray-700">
                        </div>

                        <div class="flex items-center justify-center">
                            <button type="submit" class="bg-gray-900 text-white px-4 py-2 rounded hover:bg-gray-800">Create Bill</button>
                        </div>
                    </form>

                    <%
                        String message = (String) request.getAttribute("success");
                        String error = (String) request.getAttribute("error");
                        Bill bill = (Bill) request.getAttribute("bill");
                        if (message != null && !message.isEmpty()) {
                    %>
                        <div class="text-green-600 text-center mt-4"><%= message %></div>
                    <%
                        }
                        if (error != null && !error.isEmpty()) {
                    %>
                        <div class="text-red-600 text-center mt-4"><%= error %></div>
                    <%
                        }
                        if (bill != null) {
                    %>
                        <div class="mt-6 px-6">
                            <h2 class="text-xl font-bold mb-2">Bill Details</h2>
                            <p><strong>Bill ID:</strong> <%= bill.getBillId() %></p>
                            <p><strong>Bill Date:</strong> <%= bill.getBillDate() %></p>
                            <p><strong>Total Price (Before Discount):</strong> <%= bill.getTotalPrice() + bill.getDiscount() %></p>
                            <p><strong>Discount Applied:</strong> <%= bill.getDiscount() %></p>
                            <p><strong>Total Price (After Discount):</strong> <%= bill.getTotalPrice() %></p>
                            <p><strong>Cash Tendered:</strong> <%= bill.getCashTendered() %></p>
                            <p><strong>Change Amount:</strong> <%= bill.getChangeAmount() %></p>
                            <h3 class="font-semibold mt-4 mb-2">Bill Items:</h3>
                            <ul class="list-disc list-inside">
                                <%
                                    for (BillItem item : bill.getBillItems()) {
                                %>
                                    <li>
                                        <p><strong>Product ID:</strong> <%= item.getProductCode() %></p>
                                        <p><strong>Product Name:</strong> <%= item.getproductName() %></p>
                                        <p><strong>Quantity:</strong> <%= item.getQuantity() %></p>
                                        <p><strong>Price:</strong> <%= item.getPrice() %></p>
                                        <p><strong>Total Price:</strong> <%= item.getTotalPrice() %></p>
                                    </li>
                                <%
                                    }
                                %>
                            </ul>
                        </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>

    <script>
        function addItemRow() {
            const container = document.getElementById("itemsContainer");
            const row = document.createElement("div");
            row.className = "flex gap-4";
            row.innerHTML = `
                <input type="text" name="productId" placeholder="Product Code" required class="w-1/2 py-2 px-3 border rounded shadow focus:outline-none focus:shadow-outline text-gray-700">
                <input type="number" name="quantity" placeholder="Quantity" min="1" required class="w-1/2 py-2 px-3 border rounded shadow focus:outline-none focus:shadow-outline text-gray-700">
            `;
            container.appendChild(row);
        }

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
    </script>
    <script src="https://cdn.jsdelivr.net/gh/alpinejs/alpine@v2.8.0/dist/alpine.min.js" defer></script>
</body>
