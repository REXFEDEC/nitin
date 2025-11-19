# Secure Notes Backend

A Spring Boot + MongoDB backend that provides JWT-protected APIs for registering users, logging in, and managing personal notes. The application boots from `SecureNotesApplication` and wires Spring Security with a JWT filter (`SecurityConfig`, `JwtAuthFilter`). Notes are stored per-user via Mongo repositories. See the source for implementation details @src/main/java/com/nitin/secure_notes/SecureNotesApplication.java#1-13 @src/main/java/com/nitin/secure_notes/config/SecurityConfig.java#22-40 @src/main/java/com/nitin/secure_notes/config/JwtAuthFilter.java#18-55.

---

## Prerequisites

1. **Railway account** with billing enabled if required by resources.
2. **Git repository access** to this project (fork or direct repo).
3. **MongoDB instance** reachable from Railway (Railway Mongo add-on or external cluster).
4. **Java 17** and **Maven** locally if you need to build/test before deployment.
5. Environment variables ready for deployment (see step 5 below for details).

---

## Deploying to Railway

1. **Prepare the repository**
   - Commit any changes if you have not already.
   - Push your repository to GitHub/GitLab.

2. **Create a new project on Railway**
   - Sign in at [https://railway.app/](https://railway.app/) and click **New Project → Deploy from GitHub** (or connect GitLab/Bitbucket).
   - Select your Secure Notes repository and authorize access.

3. **Configure build & start commands**
   - Railway auto-detects Maven. If prompted, set:
     - Build Command: `mvn -B -DskipTests package`
     - Start Command: `java -jar target/secure-notes-0.0.1-SNAPSHOT.jar`
   - Adjust the artifact name if your built JAR differs (see `pom.xml`).

4. **Provision MongoDB (optional)**
   - Inside the Railway project click **Add Plugin → MongoDB** to create a managed instance, or note the connection string of your external cluster.

5. **Set environment variables** (Project → Variables):
   - `SPRING_DATA_MONGODB_URI` → your Mongo URI (Railway Mongo plugin exposes this as `MONGODB_URL`).
   - `JWT_SECRET` → 32+ character secret used to sign tokens.
   - `JWT_EXPIRATION_MS` (optional) → token lifetime in milliseconds (default 3600000 for 1 hour).
   - `PORT` → leave unset; Railway injects it automatically and the app already consumes `${PORT}`.

6. **Redeploy**
   - Trigger a deploy from the Railway dashboard (Redeploy button) or push a new commit. Deployment logs will show Maven build → JAR launch.

7. **Verify health**
   - Once running, open the generated Railway domain (e.g., `https://secure-notes-production.up.railway.app`).
   - Use a REST client (Postman, Thunder Client) to hit `/api/auth/register` to confirm the service and Mongo connection are working.

8. **Set custom domain (optional)**
   - Under the service → **Settings → Domains**, map a custom domain if desired.

9. **Monitor & scale**
   - Use Railway metrics/logs for monitoring. Upgrade plan or adjust resources if needed.

---

## API Reference

> All note endpoints require a valid JWT in the `Authorization: Bearer <token>` header. Tokens are issued by the auth endpoints. Service logic resides in `UserService` and `NoteService`. @src/main/java/com/nitin/secure_notes/service/UserService.java#17-57 @src/main/java/com/nitin/secure_notes/service/NoteService.java#16-65

| Method | Endpoint | Description | Request Body | Response |
| ------ | -------- | ----------- | ------------ | -------- |
| POST | `/api/auth/register` | Register a new user; rejects duplicate emails. | `{ "name": "string", "email": "string", "password": "string" }` | `200 OK` with `{ token, message }` |
| POST | `/api/auth/login` | Authenticate existing user. | `{ "email": "string", "password": "string" }` | `200 OK` with `{ token, message }` |
| POST | `/api/notes/create` | Create a note for the authenticated user. | `{ "title": "string", "content": "string" }` | `200 OK` with `{ id, title, content }` |
| GET | `/api/notes/fetch` | Fetch all notes owned by the authenticated user. | none | `200 OK` with `[{ id, title, content }]` |
| PUT | `/api/notes/{id}` | Update note title/content by ID (must belong to user). | `{ "title": "string", "content": "string" }` | `200 OK` with updated note |
| DELETE | `/api/notes/{id}` | Delete a note by ID (must belong to user). | none | `200 OK` with empty body |

### Common headers
- `Content-Type: application/json`
- `Authorization: Bearer <jwt>` (omit for auth routes)

---

## Next Steps
- Review the source code for implementation details and further customization options.
