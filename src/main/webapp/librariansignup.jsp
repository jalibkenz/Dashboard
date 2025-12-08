<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library Signup</title>
    <style>

        * {
            box-sizing: border-box;   /* IMPORTANT FIX */
        }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            height: 100vh;
            display: flex;
            overflow: hidden;
        }

        /* LEFT SIDE IMAGE */
        .left-side {
            flex: 1;
            background-image: url('images/library.png'); /* same image as index.jsp */
            background-size: cover;
            background-position: center;
        }

        /* RIGHT SIDE SIGNUP FORM */
        .right-side {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #f4f4f4;
        }

        .signup-box {
            width: 350px;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0,0,0,0.2);
        }

        .signup-box h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .signup-box input {
            width: 100%;
            padding: 12px;
            margin-top: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .signup-box button {
            width: 100%;
            padding: 12px;
            background: #0066ff;
            color: white;
            border: none;
            border-radius: 5px;
            margin-top: 15px;
            cursor: pointer;
            font-size: 16px;
        }

        .signup-box button:hover {
            background: #0047b3;
        }

        .message {
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .error {
            background: #ffe6e6;
            color: #990000;
        }

        .success {
            background: #e6ffea;
            color: #007c2e;
        }

        .login-link {
            margin-top: 12px;
            text-align: center;
        }

        .login-link a {
            text-decoration: none;
            color: #0066ff;
        }
    </style>
</head>

<body>

<!-- LEFT IMAGE -->
<div class="left-side"></div>

<!-- RIGHT SIGNUP FORM -->
<div class="right-side">

    <div class="signup-box">
        <h2>Sign Up</h2>

        <%
            String error = request.getParameter("error");
            String success = request.getParameter("success");
        %>

        <% if (error != null) { %>
            <div class="message error"><%= URLDecoder.decode(error, "UTF-8") %></div>
        <% } %>

        <% if (success != null) { %>
            <div class="message success"><%= URLDecoder.decode(success, "UTF-8") %></div>
        <% } %>

        <form action="signup" method="post">
            <input type="text" name="name" placeholder="fullname" required>
            <input type="text" name="username" placeholder="username" required>
            <input type="password" name="password" placeholder="Password (min 6 chars)" required>
            <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
            <button type="submit">Create Account</button>
        </form>

        <div class="login-link">
            Already registered? <a href="index.jsp">Login</a>
        </div>

    </div>
</div>

</body>
</html>
