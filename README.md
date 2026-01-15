# Java JDBC ku PostgreSQL

**Tutorial pa Prinsipianti - Skola.dev**

Java 21 | PostgreSQL 16 | Maven

---

## Kuzé ki E

Es projetu é un tutorial konpletu pa prende konekta Java ku PostgreSQL uzandu JDBC. Inklui ezénplus pasu-pa-pasu i un aplikason CLI konpletu (Ajenda di Kontatu).

**Bu ta prende:**
- Konekta Java ku PostgreSQL
- Faze operason CRUD (Create, Read, Update, Delete)
- Uza PreparedStatement pa query seguru
- Organiza kódigu ku DAO pattern

---

## Rekizitu

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

# 4. Testa konexon
mvn exec:java -Dexec.mainClass="com.example.examples.E01_Connection"
```

Si bu odja "Konexon stabelecidu ku susesu!" - bu sta prontu!

---

## Estrutura di Projetu

```
java-jdbc-tutorial/
├── demo-app/                          # Kódigu Java
│   ├── pom.xml                        # Dependénsias Maven
│   └── src/main/java/com/example/
│       ├── config/
│       │   └── DatabaseConfig.java    # Konfigurason di bazi di dadus
│       ├── examples/
│       │   ├── E01_Connection.java    # Konexon báziku
│       │   ├── E02_InsertUser.java    # INSERT (Create)
│       │   ├── E03_SelectUsers.java   # SELECT (Read)
│       │   ├── E04_UpdateUser.java    # UPDATE
│       │   ├── E05_DeleteUser.java    # DELETE
│       ├── dao/
│       │   └── ContactDao.java        # Data Access Object
│       └── ContactAgendaApp.java      # Aplikason CLI (dezafiu)
├── docker/
│   ├── docker-compose.yml             # Setup PostgreSQL
│   └── init/01-schema.sql             # Tabelas inisial
├── tutorial.md                        # Tutorial konpletu
└── cheatsheet.md                      # Referénsia rápidu
```

---

## Kori Ezénplus

Kada ezénplu demonstra un konseitu JDBC. Kori es na orden:

| Ezénplu | Diskrison | Komandu |
|---------|-----------|---------|
| **E01** | Konexon ku bazi di dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E01_Connection"` |
| **E02** | INSERT - inseri dadus nobu | `mvn exec:java -Dexec.mainClass="com.example.examples.E02_InsertUser"` |
| **E03** | SELECT - le dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E03_SelectUsers"` |
| **E04** | UPDATE - atualiza dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E04_UpdateUser"` |
| **E05** | DELETE - apaga dadus | `mvn exec:java -Dexec.mainClass="com.example.examples.E05_DeleteUser"` |

> **Nota:** Tudu komandu ten ki ser ezekutadu déntu di pasta `demo-app/`

---

## Aplikason di Kontatu (Dezafiu)

Dipôs di kori ezénplus, tenta aplikason CLI konpletu:

```bash
mvn exec:java -Dexec.mainClass="com.example.ContactAgendaApp"
```

Bu ta odja menu interativu:

```
========================================
    AJENDA DI KONTATU - Skola.dev
========================================

=== Menu Prinsipal ===
1. Lista tudu kontatu
2. Buska kontatu pa ID
3. Kria kontatu nobu
4. Atualiza kontatu
5. Apaga kontatu
0. Sai

Skodje opson:
```

Uza menu pa testa tudu operason CRUD!

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

## Rekursus Adisional

- [Tutorial Konpletu](tutorial.md) - Gia pasu-pa-pasu ku splikason detalhadu
- [Cheatsheet](cheatsheet.md) - Referénsia rápidu di sintaxi JDBC
- [Slides di Apresentason (PDF)](https://drive.google.com/file/d/1scpW32mZVklz433-kdWHbaMQBzAwY3hl/view?usp=sharing) - Material vizual di aula
- [Skola.dev](https://skola.dev) - Más kursus i tutoriais

---

## Prublema Komun

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