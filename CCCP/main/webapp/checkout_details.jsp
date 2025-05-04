<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Delivery Details</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 2rem;
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .form-input {
            transition: all 0.2s ease-in-out;
        }
        .form-input:focus {
            ring: 2px solid #3b82f6;
            border-color: #3b82f6;
        }
        .submit-button:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body class="bg-gray-100 font-sans">
    <header class="bg-blue-600 text-white py-4">
        <div class="container mx-auto px-4">
            <h1 class="text-2xl font-bold">Online Shop - Checkout</h1>
        </div>
    </header>

    <main class="container mx-auto px-4 py-8">
        <% 
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded" role="alert">
                <%= errorMessage %>
            </div>
        <% 
            }
        %>

        <div class="form-container">
            <h2 class="text-xl font-semibold mb-4">Enter Delivery Details</h2>
            <form action="<%= request.getContextPath() %>/onlineShop" method="post" class="space-y-4">
                <input type="hidden" name="action" value="submitDeliveryDetails">
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700">Full Name</label>
                    <input type="text" id="name" name="name" required
                           class="form-input mt-1 block w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-2 focus:ring-blue-500"
                           aria-label="Full Name">
                </div>
                <div>
                    <label for="phone" class="block text-sm font-medium text-gray-700">Phone Number</label>
                    <input type="tel" id="phone" name="phone" required
                           class="form-input mt-1 block w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-2 focus:ring-blue-500"
                           aria-label="Phone Number">
                </div>
                <div>
                    <label for="address" class="block text-sm font-medium text-gray-700">Delivery Address</label>
                    <textarea id="address" name="address" required rows="4"
                              class="form-input mt-1 block w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-2 focus:ring-blue-500"
                              aria-label="Delivery Address"></textarea>
                </div>
                <div class="flex justify-end space-x-4">
                    <a href="<%= request.getContextPath() %>/onlineShop?action=viewCart"
                       class="inline-block bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700 transition-colors">
                        Back to Cart
                    </a>
                    <input type="submit" value="Confirm Checkout"
                           class="submit-button bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors cursor-pointer">
                </div>
            </form>
        </div>
    </main>
</body>
</html>