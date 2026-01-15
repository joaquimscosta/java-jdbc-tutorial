---
theme: default
title: "Java JDBC ku PostgreSQL"
info: |
  ## Java JDBC ku PostgreSQL
  Gia Konpletu pa Prinsipianti - Skola.dev
language: kea
drawings:
  persist: false
transition: slide-left
mdc: true
---

# Java JDBC ku PostgreSQL

Gia Konpletu pa Prinsipianti

<div class="pt-12">
  <span class="text-sm opacity-75">Skola.dev - Nu ta kria djuntu!</span>
</div>

---

# Objetivus di Aprendizajen

Nes aula, bu ta prende:

- Konekta Java ku PostgreSQL uzandu JDBC
- Faze operason CRUD (Create, Read, Update, Delete)
- Skrebe query seguru ku PreparedStatement
- Intendi objetu prinsipal di JDBC: Connection, PreparedStatement, ResultSet

<div class="pt-8">
  <span class="text-sm opacity-75">Preparadu pa mergulha na mundu di JDBC?</span>
</div>

---

# Kuzé JDBC?

**JDBC é tradutor universal entri bu kódigu Java i kualker bazi di dadus SQL**

Pensa n-el komu un tradutor universal:
- Bu skrebe Java
- JDBC konverte pa linguajen ki bazi di dadus ta intendi
- Bazi di dadus manda resposta
- JDBC torna konverte pa Java

<div class="pt-8">
  <span class="text-sm opacity-75">Pamodi prende JDBC? É fundamentu pa Spring Data, Hibernate, i otu framework.</span>
</div>

<!-- notes
JDBC signifika Java Database Connectivity. É API standard pa konekta Java ku bazi di dadus SQL.
Mésmu ku bu ta uza framework modernu, konxe JDBC ta djuda bu intendi komu kuza ta funsiona tras di sena.
-->

---

# Arkitetura JDBC

<img src="/images/01-jdbc-architecture.jpg" class="h-80 mx-auto" alt="JDBC Architecture" />

<div class="pt-4">
  <span class="text-sm">
    Fluxu di três kamada: Aplikason Java → JDBC API → Driver PostgreSQL → Bazi di Dadus
  </span>
</div>

<!-- notes
Arkitetura JDBC ta konsiste di três kamada:
1. Aplikason Java - bu kódigu
2. JDBC API - ponti standard
3. Driver spesífiku (PostgreSQL, MySQL, etc.)
-->

---

# Três Objetu Prinsipal

<img src="/images/03-core-objects.png" class="h-64 mx-auto" alt="Core JDBC Objects" />

<div class="pt-4 grid grid-cols-3 gap-4 text-sm">
  <div>
    <strong>Connection</strong>
    <p>Konexon ku bazi di dadus</p>
  </div>
  <div>
    <strong>PreparedStatement</strong>
    <p>Prepara i ezekuta query</p>
  </div>
  <div>
    <strong>ResultSet</strong>
    <p>Rezultadu di SELECT query</p>
  </div>
</div>

<!-- notes
Es três objetu é núkleu di JDBC:
- Connection: Stabelese konexon ku bazi di dadus
- PreparedStatement: Skrebe i ezekuta SQL di forma seguru
- ResultSet: Itera sobri rezultadu di SELECT
-->

---

# URL di Konexon

<img src="/images/02-connection-url.png" class="h-64 mx-auto" alt="JDBC Connection URL" />

<div class="pt-4">
  <code class="text-sm">jdbc:postgresql://localhost:5432/skola_dev</code>
</div>

Konponenti:
- `jdbc:postgresql` - Protokolu i driver
- `localhost:5432` - Host i porta
- `skola_dev` - Nomi di bazi di dadus

<!-- notes
URL di konexon é komu enderesu di kaza di bu bazi di dadus.
Kada parti ta informa JDBC undi i komu konekta.
Porta 5432 é porta default di PostgreSQL.
-->

---

# Operason CRUD

Kuatu operason báziku pa manipula dadus:

| Operason | SQL Keyword | Diskrison |
|----------|-------------|-----------|
| **Create** | INSERT | Kria dadus nobu |
| **Read** | SELECT | Le/buska dadus |
| **Update** | UPDATE | Atualiza dadus izistenti |
| **Delete** | DELETE | Apaga dadus |

<div class="pt-8">
  <span class="text-sm opacity-75">Nu ta splika kada un ku ezénplu di kódigu.</span>
</div>

---

# Inseri Dadus (Create)

<img src="/images/04-preparedstatement-safety.jpg" class="h-48 mx-auto" alt="PreparedStatement Safety" />

```java
String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setString(1, "Maria Silva");
    stmt.setString(2, "maria@skola.dev");

    int rows = stmt.executeUpdate();
    System.out.println("Inseri " + rows + " linha!");
}
```

<div class="pt-2">
  <span class="text-sm text-red-500">⚠️ Sénpri uza <code>?</code> placeholder pa seguransa!</span>
</div>

<!-- notes
PreparedStatement proteje kontra SQL injection.
Nunka konkatenar strings diretamenti na query - sénpri uza placeholders (?) i setString/setInt.
-->

---

# Le Dadus (Read)

```java
String sql = "SELECT id, name, email FROM users WHERE email LIKE ?";

try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setString(1, "%@skola.dev");

    try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String email = rs.getString("email");

            System.out.println(id + ": " + name + " - " + email);
        }
    }
}
```

<div class="pt-2">
  <span class="text-sm">Uza <code>executeQuery()</code> pa SELECT i itera ku <code>while (rs.next())</code></span>
</div>

<!-- notes
executeQuery() ta retorna ResultSet.
rs.next() ta move kursor pa prósimu linha - retorna false kuandu kaba dadus.
-->

---

# ResultSet Komu Kursor

Konseitu xave: **ResultSet é kursor, KA É koleson!**

```
┌─────────────────────┐
│  Bazi di Dadus     │ ← Konexon ta fika abertu
└──────────┬──────────┘
           │
           ▼
    ┌──────────┐
    │ ResultSet│ ← Kursor ta aponta pa linha atual
    └──────────┘
```

- ResultSet é link bibu ku bazi di dadus
- Ten ki manten Connection abertu duranti iterason
- Kada `rs.next()` ta karrega prósimu linha

<!-- notes
Txeu jenti pensa ResultSet é lista, ma KA É!
É kursor ki ta aponta pa linha atual na bazi di dadus.
Si bu fetxa Connection, ResultSet para di funsiona.
-->

---

# Atualiza i Apaga (Update/Delete)

**Atualiza dadus:**
```java
String sql = "UPDATE users SET email = ? WHERE id = ?";
try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setString(1, "novo@skola.dev");
    stmt.setLong(2, 1L);
    stmt.executeUpdate();
}
```

**Apaga dadus:**
```java
String sql = "DELETE FROM users WHERE id = ?";
try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    stmt.setLong(1, 1L);
    stmt.executeUpdate();
}
```

<div class="pt-2">
  <span class="text-sm text-orange-500">⚠️ Kuidadu! Sénpri uza WHERE kláuzula pa evita apaga tudu!</span>
</div>

---

# Jeston di Rekursus

**Sénpri fetxa rekursus! Uza try-with-resources:**

```java
// FORMA KORETA - Rekursus fetxa otomatikamenti
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement stmt = conn.prepareStatement(sql);
     ResultSet rs = stmt.executeQuery()) {

    while (rs.next()) {
        // Prosesa dadus
    }
} // conn, stmt, i rs fetxa otomatikamenti!
```

<div class="pt-4">
  <span class="text-sm">Pamodi importanti? Evita memory leak i libera konexon pa otu prosesu.</span>
</div>

<!-- notes
Try-with-resources (Java 7+) garante ki rekursus fetxa, mésmu ku éru akontese.
Ordem di fetxamentu é inversu: ResultSet, dipôs PreparedStatement, dipôs Connection.
-->

---

# Rezolve Problema

| Éru | Soluson |
|------|----------|
| `No suitable driver found` | Adisiona driver PostgreSQL na classpath (`pom.xml` or `build.gradle`) |
| `Connection refused` | Verifika si PostgreSQL ta roda (`pg_isready` or `docker ps`) |
| `Password authentication failed` | Konfirma kredensial (`user`, `password`) ta koreta |
| `PSQLException: column not found` | Verifika nomi di koluna na tabela |

<div class="pt-8">
  <span class="text-sm opacity-75">Dika: Sénpri le mensajen di éru ku atenson - txeu bês el ta fla izatamenti kuzé ki é problema!</span>
</div>

<!-- notes
Éru di konexon é éru mas komun.
Antis di debugga kódigu, sénpri verifika:
1. PostgreSQL ta roda?
2. Porta ta koreta (default: 5432)?
3. Kredensial ta koreta?
-->

---

# Dezafiu

**Kria un Ajenda di Kontatu (CLI):**

Rekizitus:
- Menu ku opsons: Kria, Buska, Atualiza, Apaga kontatu
- Tabela `contacts` ku koluna: `id`, `name`, `phone`, `email`
- Validason di input (ex: email ten ki ten `@`)
- Jeston di éru apropriadu

<div class="pt-8">
  <span class="text-sm">Prátika é midjor forma pa prende! Kumesa ku kuzinha sinples i dipôs konplika.</span>
</div>

<!-- notes
Es dezafiu ta kobre tudu ki nu prende.
Sujestons:
- Kria tabela primeru ku Flyway ô manualmenti
- Kumesa ku operason Read (mas sinples)
- Dipôs adisiona Create, Update, Delete
- Testa ku dadus real!
-->

---

# Rezumu i Prósimus Pasus

**Kuzé bu prende:**
- JDBC é ponti entri Java i bazi di dadus SQL
- Três objetu prinsipal: Connection, PreparedStatement, ResultSet
- Sénpri uza PreparedStatement pa seguransa
- Try-with-resources pa jeston di rekursus

**Prósimus pasus:**
- HikariCP pa connection pooling
- Transasons (commit/rollback)
- Integra JDBC ku REST API (Spring Boot)

<div class="pt-8">
  <span class="text-sm opacity-75">Parabéns! Bu da primeru pasu na integrason di Java ku bazi di dadus. 🎉</span>
</div>

<!-- notes
JDBC é fundamental skill pa backend development Java.
Mésmu ku framework modernu abstrae JDBC, konxe el ta djuda debugga problema i otimiza performance.
Kontinua pratika i explora!
-->