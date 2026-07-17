# DMIT2015 Secure Multi-Tenant REST API Demo

## Purpose

This instructor demonstration project shows how to secure personal REST API
data using **MicroProfile JWT**, role-based authorization, and record ownership.

The project uses the `TodoItem` REST endpoints to demonstrate how a regular
authenticated user can access only their own data while a privileged role may
be allowed to read data belonging to all users.

This is a teaching example. It is **not** an Assignment 7 solution.

---

## Main Learning Objectives

After reviewing this project, students should be able to explain how to:

- Read the authenticated username from a JWT claim
- Assign ownership when a new record is created
- Ignore or overwrite ownership values sent by the client
- Filter query results by the authenticated user
- Check record ownership before find-by-ID, update, and delete operations
- Combine role-based authorization with ownership checks
- Test both permitted and denied personal-data requests

---

## Demo Roles

This project intentionally uses role names that are different from Assignment 7:

- `Sales`
- `Shipping`
- `Administration`

The project may use one role as a regular user and another role as a privileged
user that can view all data.

Students must map the demonstrated ideas to the roles and permission rules
published in the Assignment 7 specification.

Do not copy these role names directly into Assignment 7.

---

## Role and Ownership Permissions

This project demonstrates **multi-tenant personal data security** using the
`TodoItem` REST endpoints.

Both the authenticated user's role and the TodoItem owner are considered when
access is granted.

The authenticated username must be read from the JWT. A username submitted by
the REST client must not be trusted as the owner of a TodoItem.

### Sales

A user with the `Sales` role is a regular personal-data user.

A Sales user can:

- Create a TodoItem owned by their authenticated username
- Retrieve only their own TodoItems
- Retrieve one of their own TodoItems by ID
- Update only their own TodoItems
- Delete only their own TodoItems

A Sales user cannot:

- View TodoItems owned by another user
- Update TodoItems owned by another user
- Delete TodoItems owned by another user
- View all users' TodoItems

### Shipping

A user with the `Shipping` role is also a regular personal-data user.

A Shipping user can:

- Create a TodoItem owned by their authenticated username
- Retrieve only their own TodoItems
- Retrieve one of their own TodoItems by ID
- Update only their own TodoItems
- Delete only their own TodoItems

A Shipping user cannot:

- View TodoItems owned by another user
- Update TodoItems owned by another user
- Delete TodoItems owned by another user
- View all users' TodoItems

Using both Sales and Shipping accounts makes it possible to demonstrate that
different authenticated users have separate personal data.

### Administration

A user with the `Administration` role is a privileged read-only user.

An Administration user can:

- Retrieve all TodoItems belonging to all users
- Retrieve any TodoItem by ID

An Administration user cannot:

- Create a personal TodoItem
- Update a user's TodoItem
- Delete a user's TodoItem

Administration has visibility across tenants but does not become the owner of
the data and cannot modify personal records.

### Endpoint Permission Summary

| Operation | Sales | Shipping | Administration |
|---|:---:|:---:|:---:|
| Retrieve TodoItems | Own only | Own only | All users |
| Retrieve TodoItem by ID | Own only | Own only | Any user |
| Create TodoItem | Allowed | Allowed | Denied |
| Update TodoItem | Own only | Own only | Denied |
| Delete TodoItem | Own only | Own only | Denied |

### Ownership Rules

When a TodoItem is created:

1. Read the authenticated username from the JWT.
2. Set the TodoItem owner on the server.
3. Ignore or overwrite any owner value submitted by the client.

Before retrieving by ID, updating, or deleting a TodoItem:

1. Load the existing TodoItem.
2. Compare its owner with the authenticated username.
3. Permit the operation only when the ownership and role rules allow it.

A valid JWT with insufficient role or ownership permission should normally
result in:

```text
403 Forbidden
```

The purpose of this project is to demonstrate that role authorization and record ownership are separate security checks.

---

## Conceptual Model

Role authorization answers:

```text
Is this authenticated user allowed to call this endpoint?
```

Ownership authorization answers:

```text
Is this authenticated user allowed to access this specific record?
```

A secure multi-tenant API usually applies both checks.

```text
Authenticated request
  |
  v
Does the role allow this operation?
  |
  +-- No --> 403 Forbidden
  |
  +-- Yes
       |
       v
     Does the record belong to this user?
       |
       +-- No --> 403 Forbidden
       |
       +-- Yes --> Complete the operation
```

A privileged read-only role may be allowed to view all records without becoming
the owner of those records.

---

## Running the Project

Start the project in development mode:

```bash
mvn wildfly:dev
```

The REST API is configured to run on:

```text
http://localhost:8091
```

Press `Ctrl+C` in the terminal to stop WildFly.

---

## What to Observe During the Demo

Focus on how the `TodoItem` endpoints:

- Obtain the current username from the JWT
- Set ownership on the server when creating a record
- Avoid trusting a username supplied by the REST client
- Return only the authenticated user's personal data
- Check ownership before returning, updating, or deleting a record
- Allow a privileged role to read all data when required
- Deny access when a user attempts to access another user's data

The backend must enforce ownership. Filtering records only in the frontend is
not sufficient.

---

## Connection to Assignment 7

Assignment 7 applies the same pattern to `Bill` data.

Students should transfer the demonstrated approach by:

1. Reading the authenticated username from the JWT claim required by the
   assignment.
2. Setting the Bill username on the server.
3. Filtering personal queries by the authenticated username.
4. Allowing the published privileged role to view all Bills.
5. Checking ownership before retrieve-by-ID, update, and delete operations.
6. Testing with two different regular-user accounts.

The goal is to understand and apply the pattern, not copy this project as an
Assignment 7 solution.
