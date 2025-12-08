<%
    // Set headers to prevent caching in modern browsers
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

    // Set header for compatibility with older HTTP 1.0 proxies/browsers
    response.setHeader("Pragma", "no-cache");

    // Set header for expiration, usually set to a time in the past
    response.setDateHeader("Expires", 0);
%>
<%@ page import="java.util.*, in.kenz.dashboard.entity.User, in.kenz.dashboard.entity.Book" %>
<%
    // session check
    User loggedIn = (User) session.getAttribute("loggedInUser");
    if (loggedIn == null) {
        response.sendRedirect("index.jsp?error=Please login first");
        return;
    }


    // Print confirmation after successful validation
    System.out.println("Session validated - DASHBOARD.JSP");

    // lists supplied by servlet
    List<Book> loanedBooks = (List<Book>) request.getAttribute("loanedBooks");
    if (loanedBooks == null) loanedBooks = new ArrayList<>();


    List<Book> availableBooks = (List<Book>) request.getAttribute("availableBooks");
    if (availableBooks == null) availableBooks = new ArrayList<>();
%>

<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <link rel="stylesheet" href="css/iphoneSwitch.css">
  <link rel="stylesheet" href="css/dashboard.css">
  <title>Library Dashboard</title>
</head>

<body>

  <div class="header">
    <div class="welcome">Welcome, <%= loggedIn.getUserName() %>
    </div>



<div class="logout-wrapper">
    <label class="ios-switch">
        <input type="checkbox" id="logoutSwitch">
        <span class="slider"></span>
        <span class="label-text">Logout</span>
    </label>
</div>


    <div class="profile" title="<%= loggedIn.getUserName() %>">
      <%= loggedIn.getUserName().substring(0,1).toUpperCase() %>
    </div>
  </div>

  <div class="app" role="application" aria-label="Library Dashboard">





    <!-- LEFT column: Add book, Search (no loaned block here) -->
    <div class="left-stack">
      <!-- Add Book -->
      <section class="panel" aria-labelledby="addHeading">
        <h2 id="addHeading">Add New Book</h2>
        <form action="addBook" method="POST" autocomplete="off">
          <div>
            <label for="title">Title</label>
            <input id="title" name="title" type="text" placeholder="Name of book" required />
          </div>

          <div>
            <label for="author">Author</label>
            <input id="author" name="author" type="text" placeholder="Book written by" required />
          </div>
          <div>
            <label for="category">Category</label>
            <input id="category" name="category" type="text" placeholder="Book Genre" required />
          </div>

          <div class="row">
            <button class="btn" type="submit">Add Book</button>
            <button class="btn secondary" type="reset">Clear</button>
          </div>
        </form>
      </section>








      <!-- Search Books -->
      <br>
      <section class="panel" aria-labelledby="searchHeading">
        <h2 id="searchHeading">Search Books</h2>
        <form action="searchBook" method="GET" autocomplete="off">
          <label for="q">Book Title</label>
          <input id="q" name="q" type="text" placeholder="Search by title" />
          <div style="margin-top:10px;">
            <button class="btn secondary" type="submit">Search</button>
          </div>
        </form>
      </section>
    </div>

    <!-- RIGHT column: Top loaned pills + Available books list -->
    <section class="panel" aria-labelledby="listHeading">
      <h2>My Loaned Books</h2>

      <!-- LOANED PILLS: appear at top of right panel -->
      <div class="loan-pills" role="list" aria-label="Books currently on loan">
        <%
            int pillCount = Math.min(loanedBooks.size(), 3);
            for (int i = 0; i < pillCount; i++) {
                Book p = loanedBooks.get(i);
        %>
        <div class="pill" role="listitem" title="<%= p.getBookName() %>">
          <span><%= p.getBookName() %></span>
          <small>by <%= p.getBookAuthor() %></small>
        </div>
        <%
            } // end for
        %>

        <%
            if (loanedBooks.size() > 3) {
        %>
        <div style="font-weight:700;color:var(--muted);padding-left:6px;">+ <%= loanedBooks.size() - 3 %> more</div>
        <%
            }
        %>
      </div>

      <h2>Books Available in Library</h2>

      <div style="margin-bottom:10px;color:var(--muted);font-size:13px">
        Click Loan to lend the book, or Delete to remove it.
      </div>

      <div class="book-list" id="booksList">
        <% if (availableBooks.isEmpty()) { %>
        <div class="no-data">No available books. All books may be loaned.</div>
        <% } else {
                for (Book b : availableBooks) {
        %>
        <div class="book-card" role="article" aria-label="Available book">
          <div>
            <div><strong><%= b.getBookName() %></strong></div>
            <div class="muted">by <%= b.getBookAuthor() %></div>
          </div>

          <div class="row">

            <form action="loanBook" method="POST" style="margin:0">
              <input type="hidden" name="id" value="<%= b.getBookId() %>">
              <button class="btn" type="submit">Loan</button>
            </form>

            <%--
            <form action="deleteBook" method="POST" style="margin:0">
                <input type="hidden" name="id" value="<%= b.getBookId() %>">
                <button class="btn danger" type="submit">Delete</button>
            </form>
            --%>


          </div>
        </div>
        <%       } // end for
            } %>
      </div>
    </section>

  </div>
<script src="js/logoutSwitch.js"></script>

</body>

</html>
