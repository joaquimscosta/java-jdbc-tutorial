# Java JDBC ku PostgreSQL

**Tutorial pa Prinsipianti - Skola.dev**

Java 21 | PostgreSQL 16 | Maven

---

## Kuzê ki é

Es projetu é un tutorial konpletu pa prende konekta Java ku PostgreSQL uzandu JDBC. Inklui ezénplus pasu-pa-pasu ku TODO pa bu implementa.

**Bu ta prende:**
- Konekta Java ku PostgreSQL
- Faze operason CRUD (Create, Read, Update, Delete)
- Uza PreparedStatement pa query seguru
- Jeri rekursus ku try-with-resources

---

## Branchis di Tutorial

Es repositóriu ten branchis separadu pa kada seksun di tutorial:

| Branch | Konteúdu |
|--------|----------|
| `main` | **Scaffolding ku TODO** - Kumesa li! |
| `01-connection` | Soluson E01: Konexon ku bazi di dadus |
| `02-crud-insert` | Soluson E02: INSERT ku PreparedStatement |
| `03-crud-select` | Soluson E03: SELECT ku ResultSet |
| `04-crud-update` | Soluson E04: UPDATE ku WHERE |
| `05-crud-delete` | Soluson E05: DELETE ku WHERE |
| `challenge-solution` | Dezafiu: Ajenda di Kontatu (CLI + DAO) |

**Manera di uza:**
```bash
# Odja bu implementason atual
git status

# Odja soluson di un ezénplu
git checkout 01-connection

# Kompara bu kódigu ku soluson
git diff main 01-connection

# Volta pa scaffolding pa tenta novamenti
git checkout main
```

---

## Pre-rekizitus

Antis di kumesa, verifika ki bu tene:

- **Java 21+** (JDK) - `java -version`
- **Docker** i Docker Compose - `docker --version`
- **Maven 3.x** - `mvn -version`
- **Terminal** (Bash, Zsh, ô similar)

---

## Setup Rápidu

```bash
# 1. Klona repozitóriu
git clone <url-di-repositoriu>
cd java-jdbc-tutorial

# 2. Inisia PostgreSQL ku Docker
docker compose -f docker/docker-compose.yml up -d

# 3. Konpila projetu
cd demo-app
mvn compile

# 4. Verifika ki projeto ta konpila (ezénplus ten TODO)
```

---

## Estrutura di Projetu

```
java-jdbc-tutorial/
├── demo-app/                          # Kódigu Java
│   ├── pom.xml                        # Dependénsias Maven
│   └── src/main/java/com/example/
│       ├── config/
│       │   └── DatabaseConfig.java    # Konfigurason di bazi di dadus
│       └── examples/
│           ├── E01_Connection.java    # TODO: Konexon báziku
│           ├── E02_InsertUser.java    # TODO: INSERT (Create)
│           ├── E03_SelectUsers.java   # TODO: SELECT (Read)
│           ├── E04_UpdateUser.java    # TODO: UPDATE
│           └── E05_DeleteUser.java    # TODO: DELETE
├── docker/
│   ├── docker-compose.yml             # Setup PostgreSQL
│   └── init/01-schema.sql             # Tabelas inisial
└── README.md
```

---

## Kori Ezénplus

Kada ezénplu demonstra un konseitu JDBC. Implementa TODO i kori:

| Ezénplu | Diskrison | Komandu |
|---------|-----------|---------|
| **E01** | Konexon ku bazi di dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E01_Connection"` |
| **E02** | INSERT - inseri dadus novu | `mvn exec:java -Dexec.mainClass="com.example.examples.E02_InsertUser"` |
| **E03** | SELECT - le dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E03_SelectUsers"` |
| **E04** | UPDATE - atualiza dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E04_UpdateUser"` |
| **E05** | DELETE - apaga dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E05_DeleteUser"` |

> **Nota:** Tudu komandu ten ki ser ezekutadu déntu di pasta `demo-app/`

---

## Dezafiu: Ajenda di Kontatu

Dipôs di kaba tudu ezénplus, tenta konstrui un aplikason CLI konpletu!

**Rekizitus:**
- Kria tabela `contacts` ku koluna: `id`, `name`, `phone`, `email`
- Implementa menu interativu ku CRUD konpletu
- Uza DAO pattern pa organiza kódigu

**Odja soluson:**
```bash
git checkout challenge-solution
```

---

## Verifika Dadus na PostgreSQL

Pa odja dadus diretamenti na bazi:

```bash
# Konekta ku PostgreSQL
docker exec -it skola_postgres psql -U skola -d skola_dev

# Déntu di psql:
SELECT * FROM users;
SELECT * FROM contacts;
\q  -- sai
```

---

## Problema Komun

| Éru | Soluson |
|------|----------|
| `Connection refused` | Verifika si Docker ta kori: `docker ps` |
| `No suitable driver found` | Kori `mvn compile` primeru |
| `Password authentication failed` | Verifika kredensial na `DatabaseConfig.java` |

---

## Lisénsia

Es projetu é pa fins edukativu. Sinti-bu livri pa uza i modifika.

---

**Feitu ku ❤️ pa [Skola.dev](https://skola.dev)**

*Nu ta kria djuntu!*