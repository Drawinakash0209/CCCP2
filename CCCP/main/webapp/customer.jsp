<%-- customer.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<html>
<head>
    <title>Customer Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        /* Custom styles for hero section */
        .hero-bg {
            background-image: url('https://images.unsplash.com/photo-1516594798947-e65505dbb29d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            height: 320px;
            position: relative;
        }
        .hero-overlay {
            background-color: rgba(0, 0, 0, 0.5);
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body class="bg-gray-100">
    <!-- Navigation Bar -->
    <nav class="flex flex-wrap items-center justify-between p-3 bg-[#e8e8e5]">
        <div class="flex-grow text-center text-xl font-bold">SYNTEX OUTLET</div>
        <div class="flex md:hidden">
            <button id="hamburger">
                <img class="toggle block" src="https://img.icons8.com/fluent-systems-regular/2x/menu-squared-2.png" width="40" height="40" />
                <img class="toggle hidden" src="https://img.icons8.com/fluent-systems-regular/2x/close-window.png" width="40" height="40" />
            </button>
        </div>
        <div class="toggle hidden w-full md:w-auto md:flex text-right text-bold mt-5 md:mt-0 md:border-none">
            <a href="logout" class="block md:inline-block hover:text-blue-500 px-3 py-3 md:border-none">Logout</a>
        </div>
    </nav>

    <!-- Hero Section -->
    <div class="hero-bg" id="dashboard">
        <div class="hero-overlay">
            <div class="text-center text-white">
                <h1 class="text-4xl md:text-5xl font-bold mb-2">Welcome, <%= username %>!</h1>
                <p class="text-xl mb-8">Your one-stop solution for flour grinding and specialty products</p>
                <a href="onlineShop?action=viewProducts" class="px-6 py-3 bg-[#c8a876] text-white font-medium rounded-full hover:bg-[#c09858] transition duration-200">Start Shopping</a>
            </div>
        </div>
    </div>

    <!-- Customer Actions Section -->
    <section class="py-10" id="actions">
        <div class="container mx-auto px-4">
            <h2 class="text-3xl font-bold text-gray-800 mb-8 text-center">What Would You Like to Do?</h2>
            <div class="flex flex-wrap justify-center gap-8">
                <!-- Card 1 -->
                <div class="bg-white rounded-lg shadow-md overflow-hidden max-w-sm w-full">
                    <a href="onlineShop?action=viewProducts">
                        <img src="https://images.unsplash.com/photo-1586090643003-b2bfb4fbd833?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGwufDB8fHx8fA%3D%3D" alt="Shop Products" class="w-full h-64 object-cover">
                        <div class="p-6 text-center">
                            <h3 class="text-xl font-medium text-gray-800 mb-2">Shop Products</h3>
                            <p class="text-gray-700 text-base">Browse our wide range of flours, spices, and specialty products.</p>
                            <span class="mt-4 inline-block px-4 py-2 bg-[#c8a876] text-white rounded hover:bg-[#c09858]">Shop Now</span>
                        </div>
                    </a>
                </div>

                <!-- Card 2 -->
                <div class="bg-white rounded-lg shadow-md overflow-hidden max-w-sm w-full">
                    <img src="https://optimise2.assets-servd.host/learned-skunk/production/What-Is-a-POS-Transaction.jpg?w=1152&h=1152&auto=compress%2Cformat&fit=clip&dm=1675116763&s=8bd3ed922d380deb312696cf7f665a85" class="w-full h-64 object-cover">
                    <div class="p-6 text-center">
                        <h3 class="text-xl font-medium text-gray-800 mb-2">View Orders</h3>
                        <p class="text-gray-700 text-base">Check the status of your past and current orders.</p>
                        <a href="viewOrders" class="mt-4 inline-block px-4 py-2 bg-[#c8a876] text-white rounded hover:bg-[#c09858]">View Orders</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- About Us -->
    <section class="bg-gray-100" id="aboutus">
        <div class="container mx-auto py-16 px-4 sm:px-6 lg:px-8">
            <div class="grid grid-cols-1 md:grid-cols-2 items-center gap-8">
                <div class="max-w-lg">
                    <h2 class="text-3xl font-bold text-gray-800 mb-8 text-center">About Us</h2>
                    <p class="mt-4 text-gray-600 text-lg">
                        SYNTEXT OUTLET provides our customers with the highest quality products and services. We offer a
                        wide variety of flours and spices to choose from, and we are always happy to help our customers find
                        the perfect products for their needs.
                        We are committed to providing our customers with the best possible experience. We offer competitive
                        prices, fast shipping, and excellent customer service. We are also happy to answer any questions
                        that our customers may have about our products or services.
                        If you are looking for a flour and spices service business that can provide you with the highest
                        quality products and services, then we are the company for you. We look forward to serving you!
                    </p>
                </div>
                <div class="mt-12 md:mt-0">
                    <img src="https://images.unsplash.com/photo-1534723452862-4c874018d66d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="About Us Image" class="object-cover rounded-lg shadow-md">
                </div>
            </div>
        </div>
    </section>
    
    
    
    
    <section id="features" class="relative block px-6 py-10 md:py-20 md:px-10 bg-gray-100 border-t border-b border-gray-300">
    <div class="relative mx-auto max-w-5xl text-center">
        <span class="text-gray-600 my-3 flex items-center justify-center font-medium uppercase tracking-wider">
            Why Choose SYNTEXT OUTLET
        </span>
        <h2 class="block w-full bg-gradient-to-b from-gray-800 to-gray-600 bg-clip-text font-bold text-transparent text-3xl sm:text-4xl">
            Premium Flours and Spices for Every Kitchen
        </h2>
        <p class="mx-auto my-4 w-full max-w-xl bg-transparent text-center font-medium leading-relaxed tracking-wide text-gray-600">
            Discover the finest quality products, crafted with care to elevate your culinary creations, delivered with exceptional service.
        </p>
    </div>

    <div class="relative mx-auto max-w-7xl z-10 grid grid-cols-1 gap-10 pt-14 sm:grid-cols-2 lg:grid-cols-3">
        <div class="rounded-md border border-gray-300 bg-white p-8 text-center shadow-md">
            <div class="mx-auto flex h-12 w-12 items-center justify-center rounded-md border bg-[#c8a876] text-white">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
            </div>
            <h3 class="mt-6 text-gray-800 font-medium">Superior Quality</h3>
            <p class="my-4 mb-0 font-normal leading-relaxed tracking-wide text-gray-600">
                Our flours and spices are sourced and processed to ensure unmatched freshness and flavor for your dishes.
            </p>
        </div>

        <div class="rounded-md border border-gray-300 bg-white p-8 text-center shadow-md">
            <div class="mx-auto flex h-12 w-12 items-center justify-center rounded-md border bg-[#c8a876] text-white">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" />
                </svg>
            </div>
            <h3 class="mt-6 text-gray-800 font-medium">Wide Variety</h3>
            <p class="my-4 mb-0 font-normal leading-relaxed tracking-wide text-gray-600">
                Explore a diverse range of flours, spices, and specialty products tailored to meet all your culinary needs.
            </p>
        </div>

        <div class="rounded-md border border-gray-300 bg-white p-8 text-center shadow-md">
            <div class="mx-auto flex h-12 w-12 items-center justify-center rounded-md border bg-[#c8a876] text-white">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                </svg>
            </div>
            <h3 class="mt-6 text-gray-800 font-medium">Convenient Shopping</h3>
            <p class="my-4 mb-0 font-normal leading-relaxed tracking-wide text-gray-600">
                Enjoy fast shipping, competitive prices, and excellent customer service with our easy-to-use online platform.
            </p>
        </div>
    </div>

    <div class="absolute bottom-0 left-0 z-0 h-1/3 w-full border-b"
         style="background-image: linear-gradient(to right top, rgba(200, 168, 118, 0.2) 0%, transparent 50%, transparent 100%); border-color: rgba(200, 168, 118, 0.2);">
    </div>
    <div class="absolute bottom-0 right-0 z-0 h-1/3 w-full"
         style="background-image: linear-gradient(to left top, rgba(200, 168, 118, 0.2) 0%, transparent 50%, transparent 100%); border-color: rgba(200, 168, 118, 0.2);">
    </div>
</section>

    <!-- Footer -->
    <footer class="bg-gray-200 text-white py-4 px-3">
        <div class="container mx-auto flex flex-wrap items-center justify-between">
            <div class="w-full md:w-1/2 md:text-center md:mb-4 mb-8">
                <p class="text-xs text-gray-400 md:text-sm">Copyright 2024 © All Rights Reserved</p>
            </div>
            <div class="w-full md:w-1/2 md:text-center md:mb-0 mb-8">
                <ul class="list-reset flex justify-center flex-wrap text-xs md:text-sm gap-3">
                    <li><a href="#contactUs" class="text-gray-400 hover:text-white">Contact</a></li>
                    <li class="mx-4"><a href="/privacy" class="text-gray-400 hover:text-white">Privacy Policy</a></li>
                </ul>
            </div>
        </div>
    </footer>

    <script>
        document.getElementById("hamburger").onclick = function toggleMenu() {
            const navToggle = document.getElementsByClassName("toggle");
            for (let i = 0; i < navToggle.length; i++) {
                navToggle.item(i).classList.toggle("hidden");
            }
        };
    </script>
</body>
</html>