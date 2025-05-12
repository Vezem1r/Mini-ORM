# Forum Database Application

A Java application for working with an Oracle forum database, demonstrating the use of DAO pattern, stored procedures, and transaction management.

## Database Setup

### 1. Configure Database Connection

Update the connection parameters in `Database.java`:

```java
private static final String URL = "jdbc:oracle:thin:@your-host:1521:oracle";
private static final String USERNAME = "your-username";
private static final String PASSWORD = "your-password";
```

### 2. Create Database Structure

Execute SQL scripts in the following order:

```sql
-- 1. Create tables and structure
@create_db.sql

-- 2. Create stored procedures and types
@procedure.sql

-- 3. Populate with test data
@populate_db.sql

-- 4. (Optional) Test stored procedures
@test_procedure.sql
```

### 3. Drop Database (if needed)

```sql
@drop_db.sql
```