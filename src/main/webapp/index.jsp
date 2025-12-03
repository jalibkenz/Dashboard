<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library Login</title>
    <style>
        body { margin: 0; font-family: Arial, sans-serif; height: 100vh; display: flex; }

        .left-side {
            flex: 1;
            background-image: url('images/library.jpg');
            background-size: cover;
            background-position: center;
        }

        .right-side {
            flex: 1;
            display: flex; justify-content: center; align-items: center; background: #f4f4f4;
        }

        .login-box { width: 350px; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 12px rgba(0,0,0,0.2); }

        .login-box h2 { text-align: center; margin-bottom: 20px; }

        .login-box input { width: 100%; padding: 12px; margin-top: 10px; border: 1px solid #ccc; border-radius: 5px; }

        .login-box button { width: 100%; padding: 12px; background: #0066ff; color: white; border: none; border-radius: 5px; margin-top: 15px; cursor: pointer; font-size: 16px; }

        .login-box button:hover { background: #0047b3; }

        .signup { margin-top: 15px; text-align: center; }

        .message { padding: 10px; border-radius: 4px; margin-bottom: 10px; }
        .error { background: #ffe6e6; color: #990000; }
        .success { background: #e6ffea; color: #007c2e; }
        .signup a { display:inline-block; margin-top:10px; padding:10px 18px; background:#0066ff; color:#fff; text-decoration:none; border-radius:5px; }
        .signup a:hover { background:#0047b3; }
    </style>
</head>
<body>

<div class="left-side"></div>

<div class="right-side">
    <div class="login-box">
        <h2>Login</h2>

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

        <form action="auth" method="post">
            <input type="text" name="username" placeholder="Enter Username" required>
            <input type="password" name="password" placeholder="Enter Password" required>
            <button type="submit">Login</button>
        </form>

        <div class="signup">
            Don't have an account?
            <a href="signup.jsp">Sign Up</a>
        </div>
    </div>
</div>

</body>
</html>
