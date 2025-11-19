# Secure Notes Backend

A Spring Boot + MongoDB backend that provides JWT-protected APIs for registering users, logging in, and managing personal notes. The application boots from `SecureNotesApplication` and wires Spring Security with a JWT filter (`SecurityConfig`, `JwtAuthFilter`). Notes are stored per-user via Mongo repositories. See the source for implementation details @src/main/java/com/nitin/secure_notes/SecureNotesApplication.java#1-13 @src/main/java/com/nitin/secure_notes/config/SecurityConfig.java#22-40 @src/main/java/com/nitin/secure_notes/config/JwtAuthFilter.java#18-55.

---

## Prerequisites

1. **Railway account** with billing enabled if required by resources.
2. **Git repository access** to this project (fork or direct repo).
3. **MongoDB instance** reachable from Railway (Railway Mongo add-on or external cluster).
4. **Java 17** and **Maven** locally if you need to build/test before deployment.

> **Note:** Before deploying, update `src/main/resources/application.properties` so the server picks up Railway's injected port: `server.port=${PORT:9090}`. Railway sets `PORT` at runtime; without this change the service will still listen on 9090.

---

## Deploying to Railway

1. **Prepare the repository**
   - Commit the port change mentioned above if you have not already.
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
   - `JWT_SECRET` (optional) → replace the hardcoded secret if you refactor `JwtUtil` to read from env.
   - `PORT` → leave blank; Railway injects it automatically. Ensure the property override described earlier is present.

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

## Suggested Vercel v0 Prompt

Use the following prompt in [Vercel v0](https://v0.dev/) to scaffold a frontend that integrates with this backend:

```text
Create a responsive React (Next.js) UI for a "Secure Notes" app that consumes these backend endpoints:
- POST /api/auth/register
- POST /api/auth/login
- POST /api/notes/create
- GET /api/notes/fetch
- PUT /api/notes/{id}
- DELETE /api/notes/{id}

Requirements:
1. Auth pages for register + login with validation. Store JWT in memory (React state) and automatically attach it as an Authorization Bearer header for protected requests.
2. Notes dashboard showing a list of the user's notes (title + content) fetched from /api/notes/fetch on load. Provide create, edit, and delete interactions with optimistic UI updates.
3. Modal or inline form for creating/editing notes. Use Tailwind CSS for styling with a muted neutral palette and accent color for primary buttons.
4. Global error + success toasts, loading indicators, and graceful handling of expired tokens (redirect to login).
5. Configuration section (e.g., environment or .env example) where the API base URL can be set, defaulting to a placeholder Railway deployment URL.

Generate React components, pages, and a simple state management approach (React Context or hooks) to coordinate auth state and API calls.
```

---

## Next Steps
- Replace the hard-coded JWT secret in `JwtUtil` with an environment variable for production.
- Add integration tests or Postman collection documenting sample requests and responses.
