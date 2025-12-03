<%@ page import="java.util.*, in.kenz.dashboard.entity.User, in.kenz.dashboard.entity.Book" %>
<%
    // session check
    User loggedIn = (User) session.getAttribute("loggedInUser");
    if (loggedIn == null) {
        response.sendRedirect("index.jsp?error=Please login first");
        return;
    }

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
  <title>Library Dashboard</title>
  <style>
    :root {
      --bg: #f5f7fb;
      --card: #ffffff;
      --accent: #2563eb;
      --muted: #6b7280;
      --danger: #ef4444;
      --glass: rgba(255, 255, 255, 0.6);
      --pill-bg: #ff63e6;
      /* purple pill */
      --pill-border: #ff9fe6;
      font-family: Inter, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial;
    }

    * {
      box-sizing: border-box
    }

    html,
    body {
      height: 100%;
      margin: 0;
      background: linear-gradient(180deg, #eef2ff 0%, var(--bg) 100%);
      color: #111
    }

    /* Header */
    .header {
      width: 100%;
      padding: 14px 22px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: var(--card);
      box-shadow: 0 4px 18px rgba(16, 24, 40, 0.06);
      position: sticky;
      top: 0;
      z-index: 100;
      border-radius: 0 0 12px 12px;
      margin-bottom: 18px;
    }

    .welcome {
      font-size: 18px;
      font-weight: 600
    }

    .profile {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      background: #dbeafe;
      display: flex;
      justify-content: center;
      align-items: center;
      font-weight: 700;
      color: var(--accent);
      font-size: 18px;
      cursor: pointer;
      border: 2px solid #bfdbfe;
    }

    .app {
      max-width: 1100px;
      margin: 0 auto;
      padding: 20px;
      display: grid;
      grid-template-columns: 320px 1fr;
      gap: 20px;
      align-items: start;
    }

    .panel {
      background: var(--card);
      border-radius: 12px;
      padding: 16px;
      box-shadow: 0 6px 18px rgba(16, 24, 40, 0.06)
    }



    h2 {
      margin: 0 0 12px 0;
      font-size: 18px
    }

    form>div {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    label {
      font-size: 13px;
      color: var(--muted)
    }

    input[type=text] {
      padding: 10px;
      border-radius: 8px;
      border: 1px solid #e6e9ee;
      font-size: 14px
    }

    .row {
        display: flex !important;
        flex-direction: row !important;
        gap: 10px;
        margin-top: 10px;
    }


    .btn {
      padding: 10px 12px;
      border-radius: 10px;
      border: 0;
      background: var(--accent);
      color: white;
      font-weight: 600;
      cursor: pointer
    }

    .btn.secondary {
      background: #e6eefc;
      color: var(--accent);
      font-weight: 600
    }

    .btn.danger {
      background: var(--danger)
    }

    /* pills (loaned books) */
    .loan-pills {
      display: flex;
      gap: 18px;
      align-items: center;
      padding: 8px 12px;
      margin-bottom: 14px;
      flex-wrap: wrap;
    }

    .pill {
      background: var(--pill-bg);
      border: 2px solid var(--pill-border);
      color: white;
      padding: 10px 26px;
      border-radius: 24px;
      font-weight: 700;
      box-shadow: 0 6px 18px rgba(255, 99, 230, 0.14);
      min-width: 120px;
      text-align: center;
    }

    .pill small {
      display: block;
      font-weight: 600;
      opacity: 0.95;
      font-size: 13px;
    }

    /* book list */
    .book-list {
      display: flex;
      flex-direction: column;
      gap: 8px
    }

    .book-card {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px;
      border-radius: 10px;
      background: var(--glass);
      border: 1px solid #f1f5f9;
    }

    .no-data {
      padding: 12px;
      text-align: center;
      color: var(--muted)
    }

    .muted {
      color: var(--muted);
      font-size: 13px
    }

    @media (max-width:880px) {
      .app {
        grid-template-columns: 1fr;
      }

      .loan-pills {
        justify-content: center
      }

      .pill {
        min-width: 100px;
        padding: 8px 18px
      }
    }
  </style>
</head>

<body>

  <div class="header">
    <div class="welcome">Welcome, <%= loggedIn.getUserName() %>
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
      <h2 id="listHeading">Books Available to Loan</h2>

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

            <form action="deleteBook" method="POST" style="margin:0">
              <input type="hidden" name="id" value="<%= b.getBookId() %>">
              <button class="btn danger" type="submit">Delete</button>
            </form>
          </div>
        </div>
        <%       } // end for
            } %>
      </div>
    </section>

  </div>

</body>

</html>
