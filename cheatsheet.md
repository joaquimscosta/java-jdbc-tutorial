# Java JDBC ku PostgreSQL - Referénsia Rápidu (v42.7.2)

**Persona/Kontestu:** Dizenvolvedor Java prinsipianti — buska sintaxi JDBC
**Testadu/Asunson:** Java 21+, PostgreSQL 16, Driver JDBC 42.7.2
**Linguajen:** kea

---

## Golden Path: Konekta i Faze Query

```java
import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/nhaapp";

        try (Connection conn = DriverManager.getConnection(url, "uzuariu", "senha");
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM uzuarius WHERE id = ?")) {

            pstmt.setInt(1, 42);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("nomi"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## Três Objetu Prinsipal

| Komponenti | Analogia | Objetivu |
|------------|----------|----------|
| **Connection** | Txamada telefóniku | Abri linha pa bazi di dadus |
| **PreparedStatement** | Mensajen | Leva bu query SQL ku parâmetrus |
| **ResultSet** | Resposta | Konten dadus ki bazi ta devolve |

---

## URL di Konexon

```
jdbc:postgresql://localhost:5432/nhaapp
│    │           │         │    │
│    │           │         │    └── Nomi di bazi
│    │           │         └── Porta (5432 = padron)
│    │           └── Host
│    └── Tipu di bazi
└── Protokolu
```

| Variason | Ezénplu |
|----------|---------|
| Lokal | `jdbc:postgresql://localhost:5432/nhaapp` |
| Remotu | `jdbc:postgresql://db.ezemplu.com:5432/produson` |
| Ku SSL | `jdbc:postgresql://localhost:5432/nhaapp?ssl=true` |

---

## CRUD - Operason Prinsipal

### CREATE (Insert)

```java
String sql = "INSERT INTO uzuarius (nomi, email) VALUES (?, ?)";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, "maria_silva");
    pstmt.setString(2, "maria@ezemplu.com");
    int linhas = pstmt.executeUpdate();  // Devolve númeru di linhas inseridu
}
```

### READ (Select)

```java
String sql = "SELECT id, nomi, email FROM uzuarius WHERE email = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, "maria@ezemplu.com");
    try (ResultSet rs = pstmt.executeQuery()) {  // Devolve ResultSet
        while (rs.next()) {
            int id = rs.getInt("id");
            String nomi = rs.getString("nomi");
        }
    }
}
```

### UPDATE

```java
String sql = "UPDATE uzuarius SET email = ? WHERE id = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, "nobu@email.com");
    pstmt.setInt(2, 1);
    int linhas = pstmt.executeUpdate();  // 0 = ninhun linha atualizadu
}
```

### DELETE

```java
String sql = "DELETE FROM uzuarius WHERE id = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setInt(1, 1);
    int linhas = pstmt.executeUpdate();
}
```

---

## PreparedStatement - Setters

| Tipu Java | Métodu | Ezénplu |
|-----------|--------|---------|
| `String` | `setString(pos, val)` | `pstmt.setString(1, "Maria")` |
| `int` | `setInt(pos, val)` | `pstmt.setInt(2, 42)` |
| `long` | `setLong(pos, val)` | `pstmt.setLong(1, 999999L)` |
| `double` | `setDouble(pos, val)` | `pstmt.setDouble(1, 3.14)` |
| `boolean` | `setBoolean(pos, val)` | `pstmt.setBoolean(1, true)` |
| `Date` | `setDate(pos, val)` | `pstmt.setDate(1, Date.valueOf("2024-01-15"))` |
| `Timestamp` | `setTimestamp(pos, val)` | `pstmt.setTimestamp(1, Timestamp.valueOf(...))` |
| `null` | `setNull(pos, type)` | `pstmt.setNull(1, Types.VARCHAR)` |

> Pozison `?` ta kumesa na **1**, ka na 0.

---

## ResultSet - Getters

| Métodu | Devolve | Uzu |
|--------|---------|-----|
| `rs.next()` | `boolean` | Move pa prósima linha; `false` kuandu kaba |
| `rs.getString("koluna")` | `String` | Le testu pa nomi di koluna |
| `rs.getInt("koluna")` | `int` | Le interu |
| `rs.getBoolean("koluna")` | `boolean` | Le verdadeiru/falsu |
| `rs.getDate("koluna")` | `Date` | Le data |
| `rs.wasNull()` | `boolean` | Verifika si últimu valor era NULL |

> Sénpri uza nomi di koluna (`rs.getString("nomi")`) envês di pozison (`rs.getString(1)`).

---

## Métodus di Ezekuson

| Métodu | Uza Pa | Devolve |
|--------|--------|---------|
| `executeQuery()` | SELECT | `ResultSet` |
| `executeUpdate()` | INSERT, UPDATE, DELETE, CREATE | `int` (linhas afetadus) |

---

## 6 Pitfalls - Éru Komun

| Ka Faze | Porkê | Faze Envês |
|---------|-------|------------|
| `"SELECT * WHERE nomi = '" + input + "'"` | SQL Injection! | Uza `?` ku `PreparedStatement` |
| Fetxa `Connection` antis di le `ResultSet` | ResultSet ta móre | Prosesa déntu di try-with-resources |
| Skese fetxa Connection/Statement/ResultSet | Vazamentu di rekursus | Sénpri uza try-with-resources |
| `UPDATE ... SET email = ?` sen WHERE | Atualiza TUDU linhas! | Sénpri verifika WHERE |
| Trata ResultSet komu List | El é kursor bibu, ka koleson | Kopia dadus pa List déntu di konexon |
| `catch (Exception e) {}` vaziu | Éru ta pasa sukundidu | Log éru ô re-throw |

---

## Rezolve Problema

### "No suitable driver found"

```xml
<!-- Djunta na pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.2</version>
</dependency>
```

Ô ku JAR manual:
```bash
java -cp ".:postgresql-42.7.2.jar" JdbcDemo
```

### "Connection refused"

```bash
# Verifika si PostgreSQL ta kori
pg_isready

# Verifika servisu
sudo systemctl status postgresql
```

### "Password authentication failed"

1. Testa kredensial na psql: `psql -U uzuariu -d bazi`
2. Verifika éru di digitason na kódigu
3. Verifika permison CONNECT na bazi

---

## Seguransa - Txeklista

- [ ] **Nunka** guarda kredensial na kódigu fonti
- [ ] Uza variável di anbienti:
  ```java
  String url = System.getenv("DATABASE_URL");
  String user = System.getenv("DATABASE_USER");
  String password = System.getenv("DATABASE_PASSWORD");
  ```
- [ ] Sénpri uza `PreparedStatement`, nunka konkatenar input
- [ ] Da uzuárius sô permison ki es meste (prinsípiu di mínimu priviléjiu)

---

## Txeklista pa Produson

- [ ] Ta uza connection pooling (HikariCP)
- [ ] Kredensial guardadu di forma seguru
- [ ] Tudu query ta uza `PreparedStatement`
- [ ] Tudu rekursus fetxadu ku try-with-resources
- [ ] Timeout konfiguradu pa konexon i query
- [ ] Tratamentu di éru i logging adekuadu

---

## Verson & Manutenson

* **Verson di doc:** 1.0.0
* **Validadu na:** 2026-01-14
* **Risku di mudansa:** Atualizason di driver JDBC, mudansa na API di PostgreSQL
* **Txeklista di revalidason:**
  - [ ] Verifika changelog di driver JDBC
  - [ ] Kori ezénplus di kódigu
  - [ ] Verifika sintaxi di flags

---

## Rekursus

- [Dokumentason JDBC di PostgreSQL](https://jdbc.postgresql.org/documentation/)
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Connection pooling pa produson