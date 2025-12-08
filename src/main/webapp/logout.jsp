<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Logged Out</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <style>
        body { margin: 0; font-family: Arial, sans-serif; height: 100vh; display: flex; }
        .left-side { flex: 1; background-image: url('images/library.png'); background-size: cover; background-position: center; }
        .right-side { flex: 1; display: flex; justify-content: center; align-items: center; background: #f4f4f4; }

        .logout-box {
            width: 480px; background: white; padding: 30px;
            border-radius: 10px; box-shadow: 0 0 12px rgba(0,0,0,0.2);
        }
        .logout-box h2 { text-align: center; margin-bottom: 6px; }
        .logout-message { text-align: center; color: #007c2e; margin-bottom: 20px; }

        .debug-info {
            background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px;
            border-radius: 5px; margin-bottom: 20px; font-family: monospace; font-size: 14px;
        }
        .debug-info strong { color: #856404; }

        .loan-list table { width: 100%; border-collapse: collapse; }
        .loan-list th, .loan-list td { padding: 8px; border-bottom: 1px solid #eee; text-align: left; }
        .loan-list th { background: #f8f9fa; font-weight: bold; }

        .login-again { text-align: center; margin-top: 20px; }
        .login-again a {
            display:inline-block; padding:10px 18px; background:#0066ff;
            color:#fff; text-decoration:none; border-radius:5px;
        }
        .login-again a:hover { background:#0047b3; }
    </style>
</head>

<body>
<div class="left-side"></div>

<div class="right-side">
    <div class="logout-box">
        <h2>You Have Logged Out</h2>
        <div class="logout-message">Thank you — please return your books on time.</div>

        <h3>Your Loaned Books</h3>

        <c:choose>
            <c:when test="${loanedBooks != null && not empty loanedBooks}">
                <div class="loan-list">
                    <table>
                        <thead>
                            <tr><th>Title</th><th>Author</th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="book" items="${loanedBooks}">
                            <tr>
                                <td><c:out value="${book.bookName}" /></td>
                                <td><c:out value="${book.bookAuthor}" /></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p style="text-align:center; color:#555;">You have no books loaned.</p>
            </c:otherwise>
        </c:choose>

        <div class="login-again">
            <a href="index.jsp">Login Again</a>
        </div>
    </div>
</div>
</body>
</html>
