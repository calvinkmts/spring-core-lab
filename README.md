# spring-core-lab

A hands-on Spring Framework lab project for exploring core Spring concepts — dependency injection, bean lifecycle, and web layer configuration — using a clean domain-driven structure.

## Tech Stack

| Technology | Version |
| --- | --- |
| Java | 25 |
| Spring Framework | 6.1.3 |
| Jakarta Servlet API | 6.0.0 |
| Lombok | 1.18.40 |
| JUnit Jupiter | 5.13.4 |
| AssertJ | 3.27.3 |
| Mockito | 5.14.2 |
| Maven | 3.9.x |

## Prerequisites

- JDK 25+
- Maven 3.9.12+ (recommended to avoid Guice `sun.misc.Unsafe` warning)
- [SDKMAN](https://sdkman.io/) (optional, recommended)

## Getting Started

Clone and build the project:

```bash
git clone https://github.com/your-username/spring-core-lab.git
cd spring-core-lab
mvn clean install
```

Test coverage includes:

- **Domain layer** — constructor validation, business rules, edge cases
- **No Lombok tests** — library behavior is trusted, not re-tested

## Project Structure

```plaintext
src/
├── main/
│   ├── java/com/calvin/lab/
│   │   └── domain/
│   └── resources/
└── test/
    └── java/com/calvin/lab/
        └── domain/
```

## License

MIT
