# DMIT2015 Secure RBAC REST API Demo

## Purpose

This instructor demonstration project shows how to secure Jakarta REST endpoints
using **MicroProfile JWT** and **role-based access control (RBAC)**.

The project uses the `Student` REST endpoints to demonstrate how different
operations can be restricted to different application roles.

This is a teaching example. It is **not** an Assignment 7 solution.

---

## Main Learning Objectives

After reviewing this project, students should be able to explain how to:

- Require JWT authentication for REST API requests
- Read application roles from the JWT
- Use `@RolesAllowed` to restrict individual endpoints
- Distinguish authentication from authorization
- Test permitted and denied requests
- Interpret common security responses such as `401 Unauthorized` and
  `403 Forbidden`

---

## Demo Roles

This project intentionally uses business roles that are different from the
roles in Assignment 7:

- `Sales`
- `Shipping`
- `Administration`

The role names are different so that students must transfer the security
pattern to the roles and permission rules published in the Assignment 7
specification.

Do not copy these role names directly into Assignment 7.

---

## Role Permissions

This project demonstrates **role-based access control** using the `Student`
REST endpoints.

Access is determined by the authenticated user's role. Student records are not
owned by individual users.

### Sales

A user with the `Sales` role can:

- Retrieve all Students
- Retrieve a Student by ID
- Create a new Student

A Sales user cannot:

- Update an existing Student
- Delete a Student

### Shipping

A user with the `Shipping` role can:

- Retrieve all Students
- Retrieve a Student by ID
- Update an existing Student

A Shipping user cannot:

- Create a new Student
- Delete a Student

### Administration

A user with the `Administration` role has full access and can:

- Retrieve all Students
- Retrieve a Student by ID
- Create a new Student
- Update an existing Student
- Delete a Student

### Endpoint Permission Summary

| Operation | Sales | Shipping | Administration |
|---|:---:|:---:|:---:|
| Retrieve all Students | Allowed | Allowed | Allowed |
| Retrieve Student by ID | Allowed | Allowed | Allowed |
| Create Student | Allowed | Denied | Allowed |
| Update Student | Denied | Allowed | Allowed |
| Delete Student | Denied | Denied | Allowed |

A denied request made with a valid JWT should normally return:

```text
403 Forbidden
```

The purpose of this project is to demonstrate that different roles may be
allowed to perform different operations on the same REST resource.

---

## Conceptual Model

```text
Request
  |
  v
Was a JWT supplied?
  |
  +-- No
  |    |
  |    +--> Access is denied
  |
  +-- Yes
       |
       v
     Is the JWT valid?
       |
       +-- No --> 401 Unauthorized
       |
       +-- Yes
            |
            v
          Does the user have the required role?
            |
            +-- No --> 403 Forbidden
            |
            +-- Yes --> Run the REST endpoint
```

With the WildFly configuration used in this course, a request with no JWT may
return `403 Forbidden` when an `@RolesAllowed` endpoint evaluates an anonymous
caller.

---

## Running the Project

Start the project in development mode:

```bash
mvn wildfly:dev
```

The REST API is configured to run on:

```text
http://localhost:8090
```

Press `Ctrl+C` in the terminal to stop WildFly.

---

## What to Observe During the Demo

Focus on how the `Student` endpoints:

- Declare which roles may perform each operation
- Accept requests from users with permitted roles
- Reject requests from users without the required role
- Keep authorization rules in the backend REST API

Frontend controls such as hidden buttons are not a replacement for backend
authorization.

---

## Connection to Assignment 7

Assignment 7 uses different resources, roles, ports, and permission rules.

Apply the same process:

1. Read the Assignment 7 RBAC permission table.
2. Identify which roles may call each endpoint.
3. Apply the correct `@RolesAllowed` values.
4. Test both permitted and denied requests using valid JWTs.

The goal is to transfer the demonstrated pattern, not copy this project as an
Assignment 7 solution.
