## Quick context

- Java 17 Swing desktop app (UI in `src/main/java/iuh/fit/se/group1/ui`).
- Raw JDBC repository pattern: `repository/Repository.java` and concrete classes (e.g. `AmenityRepository`) use `DatabaseUtil.getConnection()`.
- DB: Microsoft SQL Server (dependency `mssql-jdbc` in `pom.xml`); connection is created once by `DatabaseUtil` from `application-secret.properties` on the classpath.
- Build: Maven with `maven-shade-plugin` configured. Main class is `iuh.fit.se.group1.Main`.

## Primary goals for an AI coding agent

- Make concise, local changes that follow existing code patterns (raw JDBC + PreparedStatement). Do not replace with frameworks unless asked.
- Preserve the Swing UI patterns (custom components under `ui.swing` and layout under `ui.layout`).
- Keep DB config out of source: `src/main/resources/application-secret.properties` must contain `datasource.url`, `datasource.username`, `datasource.password`.

## How to build & run (developer workflows)

- Build a runnable JAR: `mvn clean package`. The shade plugin produces an executable jar (manifest mainClass = `iuh.fit.se.group1.Main`).
- Run the jar: `java -jar target/hotel-management-1.0-SNAPSHOT.jar` (verify the exact name in `target/`).
- IDE: run `iuh.fit.se.group1.Main` as a Java application. The `Main` class currently drives startup and should launch the Swing `Login` frame.

## Important project-specific patterns and examples

- Database connection: `DatabaseUtil` loads `application-secret.properties` from the classpath and creates a single static `Connection`. Repositories call `DatabaseUtil.getConnection()`.
  - Example: `AmenityRepository` uses `PreparedStatement`, `ResultSet`, `Statement.RETURN_GENERATED_KEYS`, and converts between `LocalDate` and `java.sql.Date`.
- Repository contract: `Repository<T,ID>` defines `save/findById/deleteById/findAll/update`. Follow the same signatures and error handling style (wrap SQLExceptions in RuntimeException or log and rethrow).
- UI: `Login` frame (in `ui.swing.Login`) orchestrates showing `panelBody` (main layout). When modifying startup, keep the UI thread rules (use `SwingUtilities.invokeLater` where appropriate).

## Files and locations to reference when making changes

- Startup / run: `src/main/java/iuh/fit/se/group1/Main.java`
- DB init: `src/main/java/iuh/fit/se/group1/infrastructure/DatabaseUtil.java`
- Repository examples: `src/main/java/iuh/fit/se/group1/repository/AmenityRepository.java`, `Repository.java`
- Entities: `src/main/java/iuh/fit/se/group1/entity/*` (models map directly to DB columns)
- UI entry: `src/main/java/iuh/fit/se/group1/ui/swing/Login.java`, custom UI components live under `ui/component` and `ui/layout`.
- Resources: `src/main/resources/` (put `application-secret.properties` here for local runs).

## Safety/compatibility notes for the agent

- Do not check secrets into the repo. If you need to add a default `application-secret.properties` for examples, create a template `application-secret.properties.example` and do NOT populate real credentials.
- Avoid large refactors that replace the JDBC approach with an ORM; this repo intentionally uses raw JDBC code.

## Small tasks examples (explicit actionable prompts)

- "Add a `findByName` method to `AmenityRepository` following the project's JDBC patterns; include PreparedStatement and null-safe mapping to `Amenity`." 
- "Implement `Main.main` to load `Login` (use `SwingUtilities.invokeLater`) and ensure `DatabaseUtil` initializes early with clear error logs." 
- "Create `src/main/resources/application-secret.properties.example` with keys and a commented sample JDBC URL for SQL Server." 

## If you need more context

- Open the `pom.xml` to confirm plugins/dependencies (shade plugin is used for the runnable jar).
- Inspect `ui.swing` for UI lifecycle patterns before changing frames or layout code.

---
If any section is unclear or you want more examples (e.g., common SQL table-to-entity mappings), tell me which parts to expand or where you'd like literal code examples inserted.
