# Java JDBC ku PostgreSQL: Gia Konpletu pa Prinsipianti

**Audiénsia:** Prinsipianti
**Linguajen/Stack:** Java, PostgreSQL
**Tópiku:** Konexon ku Bazi di Dadus
**Ténpu Estimadu:** 35 minutu
**Prekizitu:** Java instaladu (JDK 21+), PostgreSQL ta kori, sintaxi báziku di Java

---

## 1. Kuzé ki Bu Ta Kria

Na fin di es tutorial, bu ta konsigi:
- Konekta bu aplikason Java ku un bazi di dadus PostgreSQL
- Faze tudu operason CRUD (Create, Read, Update, Delete)
- Skrebi query seguru ki ta privini ataki di SQL injection
- Intendi objetu prinsipal di JDBC i manera ki es ta trabadja djuntu

**Resultadu Final:** Un programa Java ki ta le i skrebi dadus na PostgreSQL di forma seguru.

---

## 2. Vizon Jeral di Konseitu (Pamodi)

JDBC (Java Database Connectivity) é API padron ki ta permiti aplikason Java komunika ku bazi di dadus. Pensa n-el komu un tradutor universal entri bu kódigu Java i kualker bazi di dadus SQL.

**Pamodi prende JDBC?**
- É fundason di tudu trabadju ku bazi di dadus na Java, mésmu si más tardi bu uza framework komu Spring Data ô Hibernate
- Intendi JDBC ta djuda-bu debug prublema na framework di nível más altu
- Asvês bu meste kontrolu di nível baxu ki sô JDBC ta da

**Três Objetu Prinsipal**

JDBC ta trabadja ku três konponenti prinsipal:

| Komponenti | Analogia | Objetivu |
| --- | --- | --- |
| **Connection** | Txamada telefóniku | Abri linha pa bazi di dadus |
| **Statement / PreparedStatement** | Mensajen | Leva bu query SQL |
| **ResultSet** | Resposta | Konten dadus ki bazi di dadus ta devolve |

---

## 3. Ezénplu Mínimu ki Ta Funsiona

Nu faze algu ta funsiona primeru, dipôs nu intendi.

**Setup (Maven):**

Djunta driver di PostgreSQL na bu `pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.2</version>
</dependency>
```

> 💡 **Dika:** Si bu ka ta uza Maven, baxa `.jar` di [jdbc.postgresql.org/download](https://jdbc.postgresql.org/download/) i djunta-l na bu classpath.

**Kódigu:**

Kria un fixeru txomadu `JdbcDemo.java`:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcDemo {

    // Konfigurason di bazi di dadus
    private static final String URL = "jdbc:postgresql://localhost:5432/bu_bazi";
    private static final String USER = "bu_uzuariu";
    private static final String PASSWORD = "bu_senha";

    public static void main(String[] args) {

        // "try-with-resources" ta fetxa konexon automatikamenti
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.println("Konektadu ku PostgreSQL!");

            // Query sinples
            String sql = "SELECT version()";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    System.out.println("Verson di bazi: " + rs.getString(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Konexon falha!");
            e.printStackTrace();
        }
    }
}
```

**Saída Esperadu:**

```
Konektadu ku PostgreSQL!
Verson di bazi: PostgreSQL 16.2 on x86_64-pc-linux-gnu...
```

> ⚠️ **Atenson:** Troka `bu_bazi`, `bu_uzuariu`, i `bu_senha` ku bu kredensial real di PostgreSQL.

---

## 4. Pasu Gia

### Pasu 1: Intendi URL di Konexon

URL di JDBC é bu enderesu pa bazi di dadus. Nu diskonpô-l:

```
jdbc:postgresql://localhost:5432/bu_bazi
│    │           │         │    │
│    │           │         │    └── Nomi di bazi di dadus
│    │           │         └── Porta (5432 é padron di PostgreSQL)
│    │           └── Host (localhost pa lokal, ô IP/domíniu)
│    └── Tipu di bazi (postgresql)
└── Protokolu (sénpri "jdbc:")
```

**Kódigu:**

```java
// Bazi di dadus lokal
String URL = "jdbc:postgresql://localhost:5432/nhaapp";

// Bazi di dadus remotu
String URL = "jdbc:postgresql://db.ezemplu.com:5432/produson";

// Ku SSL
String URL = "jdbc:postgresql://localhost:5432/nhaapp?ssl=true";
```

**Kuzé ki ta kontise:**
- Java ta uza es URL pa atxa driver di PostgreSQL automatikamenti
- Driver ta traduzi bu txamada Java pa protokolu di rede di PostgreSQL
- Si URL sta eradu, bu ta odja éru "No suitable driver found"

---


### Pasu 2: Kria un Tabela

Antis di nu inseri dadus, nu meste un tabela. Odja manera di kria un na Java:

```java
public static void criaTabela(Connection conn) throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS uzuarius (
            id SERIAL PRIMARY KEY,
            nomi_uzuariu VARCHAR(50) NOT NULL,
            email VARCHAR(100) UNIQUE NOT NULL,
            kriadu_na TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.executeUpdate();
        System.out.println("Tabela 'uzuarius' sta prontu.");
    }
}
```

**Kuzé ki ta kontise:**
- `CREATE TABLE IF NOT EXISTS` ta evita éru si tabela dja izisti
- `SERIAL` ta kria un ID ki ta inkrementa automatikamenti
- `executeUpdate()` ta uzadu pa SQL ki ta modifika dadus (CREATE, INSERT, UPDATE, DELETE)

**Testa-l:**

```java
public static void main(String[] args) {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        criaTabela(conn);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Resultadu esperadu:** `Tabela 'uzuarius' sta prontu.`

---


### Pasu 3: Inseri Dadus (Create)

Agora nu inseri un uzuáriu. É li ki **PreparedStatement** ta bira krítiku.

```java
public static int inseriUzuariu(Connection conn, String nomiUzuariu, String email)
        throws SQLException {

    String sql = "INSERT INTO uzuarius (nomi_uzuariu, email) VALUES (?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, nomiUzuariu);  // Primeru ?
        pstmt.setString(2, email);         // Segundu ?

        int linhasAfetadus = pstmt.executeUpdate();
        System.out.println("Inseridu " + linhasAfetadus + " uzuáriu(s).");
        return linhasAfetadus;
    }
}
```

**Kuzé ki ta kontise:**
- Placeholder `?` ta numeradu kumesandu di 1
- `setString(1, nomiUzuariu)` ta entxi primeru `?` ku valor di nomiUzuariu
- Bazi di dadus ta trata input di uzuáriu estritamenti komu DADUS, nunka komu kódigu SQL
- Es ta privini ataki di SQL injection (más sobri es más tardi)

**Testa-l:**

```java
inseriUzuariu(conn, "maria_silva", "maria@ezemplu.com");
inseriUzuariu(conn, "joao_santos", "joao@ezemplu.com");
```

**Resultadu esperadu:**

```
Inseridu 1 uzuáriu(s).
Inseridu 1 uzuáriu(s).
```

---


### Pasu 4: Le Dadus (Read)

Óra di ritrova dadus uzandu `SELECT` i prosesa `ResultSet`:

```java
public static void listaUzuariusTudu(Connection conn) throws SQLException {
    String sql = "SELECT id, nomi_uzuariu, email, kriadu_na FROM uzuarius";

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        System.out.println("\n--- Tudu Uzuárius ---");
        while (rs.next()) {
            int id = rs.getInt("id");
            String nomiUzuariu = rs.getString("nomi_uzuariu");
            String email = rs.getString("email");

            System.out.printf("ID: %d | Uzuáriu: %s | Email: %s%n",
                              id, nomiUzuariu, email);
        }
    }
}
```

**Kuzé ki ta kontise:**
- `executeQuery()` ta devolve un `ResultSet` (uza es pa statement SELECT)
- `rs.next()` ta move pa prósima linha i ta devolve `false` kuandu kaba
- `rs.getString("nomi_koluna")` ta estrai dadus pa nomi di koluna
- Bu pode tanbé uza `rs.getString(1)` pa odja pa pozison di koluna (kumesa na 1)

> 💡 **Dika:** Sénpri uza nomi di koluna envês di pozison - é más lejível i ta sobrevive reordenason di koluna.

**Resultadu esperadu:**

```
--- Tudu Uzuárius ---
ID: 1 | Uzuáriu: maria_silva | Email: maria@ezemplu.com
ID: 2 | Uzuáriu: joao_santos | Email: joao@ezemplu.com
```

---


### Pasu 5: Atxa un Uzuáriu Spesífiku

Txeu bês bu meste atxa dadus bazadu na kondison:

```java
public static void atxaUzuariuPorEmail(Connection conn, String email)
        throws SQLException {

    String sql = "SELECT id, nomi_uzuariu FROM uzuarius WHERE email = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, email);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                System.out.printf("Atxadu: %s (ID: %d)%n",
                                  rs.getString("nomi_uzuariu"),
                                  rs.getInt("id"));
            } else {
                System.out.println("Uzuáriu ka atxadu.");
            }
        }
    }
}
```

**Testa-l:**

```java
atxaUzuariuPorEmail(conn, "maria@ezemplu.com");
atxaUzuariuPorEmail(conn, "naexiste@ezemplu.com");
```

**Resultadu esperadu:**

```
Atxadu: maria_silva (ID: 1)
Uzuáriu ka atxadu.
```

---


### Verifikason di Progresu

```
--- Verifikason di Progresu ---

Na es pontu, bu debe tene:
- ✅ Konektadu ku PostgreSQL di Java
- ✅ Kriadu un tabela ku CREATE TABLE
- ✅ Inseridu linhas ku INSERT
- ✅ Ritrovadu dadus ku SELECT
- ✅ Uzadu query parametrizadu ku ?

Si ka, revê Pasu 1-5 ô konsulta sekson Rezolve Problema abaxu.
```

---


### Pasu 6: Atualiza Dadus (Update)

Modifika rejistru izistenti ku UPDATE:

```java
public static int atualizaEmailUzuariu(Connection conn, int uzuariuId, String nobuEmail)
        throws SQLException {

    String sql = "UPDATE uzuarius SET email = ? WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, nobuEmail);
        pstmt.setInt(2, uzuariuId);

        int linhasAfetadus = pstmt.executeUpdate();
        System.out.println("Atualizadu " + linhasAfetadus + " uzuáriu(s).");
        return linhasAfetadus;
    }
}
```

**Kuzé ki ta kontise:**
- `executeUpdate()` ta devolve númeru di linhas afetadus
- Si 0 linha foi atualizadu, uzuáriu ku es ID ka izisti
- Sénpri uza kláuzula WHERE, ô bu ta atualiza TUDU linhas!

> ⚠️ **Atenson:** Kori `UPDATE uzuarius SET email = 'test@test.com'` sen WHERE ta muda email di TUDU uzuáriu. Sénpri verifika dublu bu kláuzula WHERE.

---


### Pasu 7: Apaga Dadus (Delete)

Remove rejistru ku DELETE:

```java
public static int apagaUzuariu(Connection conn, int uzuariuId) throws SQLException {
    String sql = "DELETE FROM uzuarius WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, uzuariuId);

        int linhasAfetadus = pstmt.executeUpdate();
        System.out.println("Apagadu " + linhasAfetadus + " uzuáriu(s).");
        return linhasAfetadus;
    }
}
```

**Testa-l:**

```java
apagaUzuariu(conn, 1);      // Apaga uzuáriu ku ID 1
listaUzuariusTudu(conn);    // Verifika ki el bai
```

---


## 5. Intendi ResultSet Más Fundu

Dizenvolvedor nobu txeu bês ta konfundi `ResultSet` ku un `List` ô `Array` di Java. El **KA É** un koleson di dadus na mimória di bu konputador.

### Manera ki ResultSet Ta Funsiona Mésmu

```
Bazi di dadus atxa 10,000 linhas ki ta korresponde ku bu query
          │
          ▼
   ┌─────────────────┐
   │   ResultSet     │ ← Kursor ta ponta pa linha atual
   │   (link bibu)   │
   └─────────────────┘
          │
          ▼
      rs.next()  →  Pidi bazi di dadus: "Manda-m PRÓSIMA linha"
          │
          ▼
   rs.getString()  →  Le dadus di linha atual na mimória
```

**Implikason importanti:**
- `Connection` debe fika ABERTU enkuantu bu ta loop através di ResultSet
- Bu sô pode move pa frenti pa padron (ka pode volta atras)
- Si bu fetxa Connection, ResultSet ta móre na mésmu óra

**Ezénplu di kuzé ki KA DEBE faze:**

```java
// ÉRU - Connection ta fetxa antis di nu le resultadu!
ResultSet pegaUzuarius() throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM uzuarius");
    return pstmt.executeQuery();  // Connection fetxa, ResultSet móre!
}
```

**Abordajen koretu - prosesa déntu di konexon:**

```java
List<Uzuariu> pegaUzuarius() throws SQLException {
    List<Uzuariu> uzuarius = new ArrayList<>();

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM uzuarius");
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            uzuarius.add(new Uzuariu(rs.getInt("id"), rs.getString("nomi_uzuariu")));
        }
    }

    return uzuarius;  // Connection fetxadu, mas dadus sta seguru na lista
}
```

---


## 6. Dezafiu

**Tarefa:** Konstrui un livru di enderesus sinples na linha di komandu ki ta permiti-bu djunta, lista, buska, i apaga kontatus.

**Rekizitus:**
1. Kria un tabela `kontatus` ku: `id`, `nomi`, `telefoni`, `email`
2. Inplementa es operason:
   - Djunta un nobu kontatu
   - Lista tudu kontatus
   - Buska pa nomi (match parsial uzandu LIKE)
   - Apaga un kontatu pa ID
3. Uza `PreparedStatement` pa tudu query

**Dikas:**
- Pa match parsial, uza: `WHERE nomi ILIKE ?` i seta parâmetru pa `"%termuBuska%"`
- Uza un menu loop sinples ku `Scanner` pa input di uzuáriu

<details>
<summary>Soluson</summary>

```java
import java.sql.*;
import java.util.Scanner;

public class LivruEnderesus {
    private static final String URL = "jdbc:postgresql://localhost:5432/livruenderesus";
    private static final String USER = "bu_uzuariu";
    private static final String PASSWORD = "bu_senha";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            criaTabelaKontatus(conn);
            koriMenu(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void criaTabelaKontatus(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS kontatus (
                id SERIAL PRIMARY KEY,
                nomi VARCHAR(100) NOT NULL,
                telefoni VARCHAR(20),
                email VARCHAR(100)
            )
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    static void koriMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Djunta  2. Lista  3. Buska  4. Apaga  5. Sai");
            String skolha = scanner.nextLine();

            switch (skolha) {
                case "1" -> djuntaKontatu(conn, scanner);
                case "2" -> listaKontatus(conn);
                case "3" -> buskaKontatus(conn, scanner);
                case "4" -> apagaKontatu(conn, scanner);
                case "5" -> { return; }
            }
        }
    }

    static void djuntaKontatu(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nomi: ");
        String nomi = scanner.nextLine();
        System.out.print("Telefoni: ");
        String telefoni = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO kontatus (nomi, telefoni, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomi);
            pstmt.setString(2, telefoni);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println("Kontatu djuntadu!");
        }
    }

    static void listaKontatus(Connection conn) throws SQLException {
        String sql = "SELECT id, nomi, telefoni, email FROM kontatus ORDER BY nomi";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- Kontatus ---");
            while (rs.next()) {
                System.out.printf("%d. %s | %s | %s%n",
                    rs.getInt("id"),
                    rs.getString("nomi"),
                    rs.getString("telefoni"),
                    rs.getString("email"));
            }
        }
    }

    static void buskaKontatus(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Buska nomi: ");
        String termu = scanner.nextLine();

        String sql = "SELECT id, nomi, telefoni FROM kontatus WHERE nomi ILIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termu + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%d. %s - %s%n",
                        rs.getInt("id"),
                        rs.getString("nomi"),
                        rs.getString("telefoni"));
                }
            }
        }
    }

    static void apagaKontatu(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID di kontatu pa apaga: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM kontatus WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int apagadu = pstmt.executeUpdate();
            System.out.println(apagadu > 0 ? "Apagadu!" : "Ka atxadu.");
        }
    }
}
```

**Saída:**

```
1. Djunta  2. Lista  3. Buska  4. Apaga  5. Sai
1
Nomi: Ana Costa
Telefoni: 555-1234
Email: ana@email.com
Kontatu djuntadu!
```

</details>

---


## 7. Rezolve Problema & Éru Komun

### Éru: "No suitable driver found for jdbc:postgresql://..."

**Kauza:** JAR di driver JDBC di PostgreSQL ka sta na bu classpath.

**Soluson:**

```xml
<!-- Djunta na pom.xml si ta uza Maven -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.2</version>
</dependency>
```

Ô baxa JAR i djunta manualmenti:

```bash
java -cp ".:postgresql-42.7.2.jar" JdbcDemo
```

---


### Éru: "Connection refused" ô "Connection timed out"

**Kauza:** PostgreSQL ka ta kori, ô host/porta eradu.

**Soluson:**

1. Verifika si PostgreSQL ta kori:

   ```bash
   # Linux/Mac
   pg_isready

   # Ô verifika servisu
   sudo systemctl status postgresql
   ```

2. Verifika porta (padron é 5432):

   ```bash
   psql -h localhost -p 5432 -U bu_uzuariu
   ```

3. Verifika `pg_hba.conf` ta permiti konexon di bu IP

---


### Éru: "FATAL: password authentication failed"

**Kauza:** Nomi di uzuáriu ô senha eradu.

**Soluson:**

1. Verifika ki kredensial ta funsiona na psql:

   ```bash
   psql -U bu_uzuariu -d bu_bazi
   ```

2. Verifika éru di digitason na bu kódigu Java

3. Garanti ki uzuáriu tene permison CONNECT na bazi di dadus

---


### Éru Komun: SQL Injection

**Prublema:** Konkatenar input di uzuáriu diretamenti na string SQL.

**Pamodi ta falha:**

```java
// PERIGOZU - Nunka faze es!
String sql = "SELECT * FROM uzuarius WHERE nomi_uzuariu = '" + inputUzuariu + "'";

// Si inputUzuariu é: "'; DROP TABLE uzuarius; --"
// Query ta bira: SELECT * FROM uzuarius WHERE nomi_uzuariu = ''; DROP TABLE uzuarius; --' 
```

**Abordajen koretu:**

```java
// SEGURU - Sénpri uza PreparedStatement
String sql = "SELECT * FROM uzuarius WHERE nomi_uzuariu = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, inputUzuariu);  // Input ta tratadu komu dadus, nunka komu kódigu
```

---


### Éru Komun: Vazamentu di Rekursus

**Prublema:** Ka fetxa objetu Connection, Statement, ô ResultSet.

**Pamodi ta falha:**
- Konexon di bazi di dadus é karu i limitadu
- Konexon vazadu ta sgota pool di konexon di bazi di dadus
- Bu aplikason eventualmenti ta krax ku "too many connections"

**Abordajen koretu:**

```java
// SÉNPRI uza try-with-resources
try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {

    // Trabadja ku resultadus

} // Tudu três ta fetxadu automatikamenti li, mésmu si exeson akontese
```

---


## 8. Rezumu & Prósimus Pasus

### Kuzé ki Bu Prende

- ✅ Manera di konekta Java ku PostgreSQL uzandu JDBC
- ✅ Três objetu prinsipal: Connection, PreparedStatement, ResultSet
- ✅ Manera di faze operason CRUD di forma seguru
- ✅ Pamodi PreparedStatement ta privini SQL injection
- ✅ Manera ki ResultSet ta funsiona komu kursor (ka é koleson)
- ✅ Jeston koretu di rekursus ku try-with-resources

### Referénsia Rápidu

```java
// Konekta
Connection conn = DriverManager.getConnection(
    "jdbc:postgresql://localhost:5432/nomibazi", "uzuariu", "senha");

// Query ku parâmetrus (SELECT)
PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM uzuarius WHERE id = ?");
pstmt.setInt(1, uzuariuId);
ResultSet rs = pstmt.executeQuery();

// Modifika dadus (INSERT, UPDATE, DELETE)
PreparedStatement pstmt = conn.prepareStatement("INSERT INTO uzuarius (nomi) VALUES (?)");
pstmt.setString(1, nomi);
int linhasAfetadus = pstmt.executeUpdate();

// Sénpri fetxa rekursus (uza try-with-resources)
```

### Prósimus Pasus

1. **Imediatu:** Tenta dezafiu di Livru di Enderesus li nriba
2. **Explora:** Prende sobri Connection Pooling ku HikariCP (esensial pa produson)
3. **Fundu:** Studa Transason - manera di agrupa operason multiplu ki ta susede ô falha djuntu
4. **Konstrui:** Kria un REST API sinples ki ta uza JDBC pa armazenamentu di dadus

**Rekursus Rekumendadu:**
- [Dokumentason JDBC di PostgreSQL](https://jdbc.postgresql.org/documentation/) - Docs ofisial di driver
- [Tutorial JDBC di Baeldung](https://www.baeldung.com/java-jdbc) - Más ezénplus
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Connection pooling pa produson

---


## Apéndisi

### Notas di Seguransa

- **Nunka** guarda kredensial di bazi di dadus na kódigu fonti pa produson
- Uza variável di anbienti ô jestor di segredu:

  ```java
  String url = System.getenv("DATABASE_URL");
  String user = System.getenv("DATABASE_USER");
  String password = System.getenv("DATABASE_PASSWORD");
  ```

- Sénpri uza PreparedStatement, nunka konkatenar input di uzuáriu
- Da uzuárius di bazi di dadus sô permison ki es meste (prinsípiu di mínimu priviléjiu)

### Txeklista pa Produson

- [ ] Ta uza connection pooling (HikariCP, c3p0, ô similar)
- [ ] Kredensial guardadu di forma seguru (variável anbienti, jestor di segredu)
- [ ] Tudu query ta uza PreparedStatement
- [ ] Tudu rekursus fetxadu ku try-with-resources
- [ ] Timeout konfiguradu pa konexon i query
- [ ] Tratamentu di éru i logging adekuadu
- [ ] Uzuáriu di bazi di dadus tene permison mínimu nesesáriu
