<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Delivery Details</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- jQuery CDN -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
		.loading-spinner {
		    display: none;
		    text-align: center;
		    margin-top: 1rem;
		    position: relative;
		    z-index: 1000;
		}
		.loading-spinner::after {
		    content: '';
		    display: inline-block;
		    width: 24px;
		    height: 24px;
		    border: 3px solid #3b82f6;
		    border-radius: 50%;
		    border-top-color: transparent;
		    animation: spin 1s linear infinite;
		}
		@keyframes spin {
		    to { transform: rotate(360deg); }
		}
    </style>
</head>
<body class="bg-gray-100 font-sans">
    <header class="bg-blue-600 text-white py-4">
        <div class="container mx-auto px-4">
            <h1 class="text-2xl font-bold">Syntex - Checkout</h1>
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
            <form id="deliveryForm" action="<%= request.getContextPath() %>/onlineShop" method="post" class="space-y-4">
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
                <div id="formMessage" class="hidden text-center"></div>
                <div class="loading-spinner" id="loadingSpinner"></div>
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

    <script>
    $(document).ready(function() {
        $('#deliveryForm').on('submit', function(event) {
            event.preventDefault(); // Prevent default form submission

            // Disable submit button
            var $submitButton = $('input[type="submit"]');
            $submitButton.prop('disabled', true).addClass('opacity-50 cursor-not-allowed');

            // Show loading spinner
            $('#loadingSpinner').show();
            $('#formMessage').addClass('hidden').removeClass('text-green-700 text-red-700');

            // Collect form data
            var formData = $(this).serialize();

            // Record start time for minimum spinner display
            var startTime = Date.now();

            // Perform AJAX request
            $.ajax({
                url: '<%= request.getContextPath() %>/onlineShop',
                type: 'POST',
                data: formData,
                success: function(response, textStatus, jqXHR) {
                    // Calculate elapsed time
                    var elapsedTime = Date.now() - startTime;
                    var minSpinnerTime = 1000; // 1 second minimum

                    // Delay hiding spinner if request was too fast
                    setTimeout(function() {
                        $('#loadingSpinner').hide();
                        $submitButton.prop('disabled', false).removeClass('opacity-50 cursor-not-allowed');

                        var contentType = jqXHR.getResponseHeader('Content-Type');
                        if (contentType.includes('text/html')) {
                            if (jqXHR.responseText.includes('login.jsp')) {
                                $('#formMessage').removeClass('hidden').addClass('text-red-700').text('Session expired. Please login again.');
                                setTimeout(function() {
                                    window.location.href = 'login.jsp?error=Please login first';
                                }, 2000);
                            } else {
                                $('#formMessage').removeClass('hidden').addClass('text-red-700').text('Checkout failed: An unexpected error occurred.');
                            }
                        } else {
                            try {
                                var result = typeof response === 'string' ? JSON.parse(response) : response;
                                if (result.status === 'success') {
                                    $('#formMessage').removeClass('hidden').addClass('text-green-700').text(result.message || 'Order placed successfully!');
                                    setTimeout(function() {
                                        window.location.href = 'bill_display.jsp';
                                    }, 2000);
                                } else {
                                    $('#formMessage').removeClass('hidden').addClass('text-red-700').text(result.message || 'Checkout failed.');
                                }
                            } catch (e) {
                                $('#formMessage').removeClass('hidden').addClass('text-red-700').text('Checkout failed: Invalid response from server.');
                            }
                        }
                    }, Math.max(0, minSpinnerTime - elapsedTime));
                },
                error: function(xhr, status, error) {
                    var elapsedTime = Date.now() - startTime;
                    var minSpinnerTime = 1000;

                    setTimeout(function() {
                        $('#loadingSpinner').hide();
                        $submitButton.prop('disabled', false).removeClass('opacity-50 cursor-not-allowed');
                        $('#formMessage').removeClass('hidden').addClass('text-red-700').text('Checkout failed: ' + (xhr.responseText || 'An error occurred.'));
                    }, Math.max(0, minSpinnerTime - elapsedTime));
                }
            });
        });
    });
    </script>
</body>
</html>