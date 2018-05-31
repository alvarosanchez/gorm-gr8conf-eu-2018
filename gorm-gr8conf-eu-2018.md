footer: ##### ![inline](images/square-twitter-512.png) @alvaro_sanchez
slidenumbers: false

[.hide-footer]

# [fit] 6 things you need to
# [fit] know about GORM 6

![inline](images/oci.png) ![inline](images/gr8conf.png)

Álvaro Sánchez-Mariscal
![inline](images//square-twitter-512.png)@alvaro_sanchez

---

![](images/alvaro.jpg)

# About me

- Coming from Madrid :es:
- Developer since 2001 (Java / Spring stack).
- Grails fanboy since v0.4.
- Working @ OCI since 2015: Groovy, Grails & Micronaut!
- Father since 2017! :family:

---

<br/>
<br/>
<br/>

# [fit] 1. GORM without Grails / 
# [fit] Spring Boot / Micronaut

---

# `build.gradle`

```groovy
dependencies {
    compile 'org.codehaus.groovy:groovy:2.5.0'
    compile "org.grails:grails-datastore-gorm-hibernate5:6.1.9.RELEASE"
    compile 'org.hibernate:hibernate-validator:6.0.2.Final'

    compileOnly 'org.glassfish.web:el-impl:2.2.1-b05'

    runtime 'org.glassfish:javax.el:3.0.1-b08'
    runtime "com.h2database:h2:1.4.192"
    runtime "org.apache.tomcat:tomcat-jdbc:8.5.0"
    runtime "org.apache.tomcat.embed:tomcat-embed-logging-log4j:8.5.0"
    runtime "org.slf4j:slf4j-api:1.7.10"
    runtime "ch.qos.logback:logback-classic:1.2.3"
    
    testCompile "org.spockframework:spock-core:1.1-groovy-2.4"
}
```

---

<br/>
<br/>
<br/>

# [fit] 2. Data Services

---

# Data Services

Auto-generated persistence logic from interfaces and abstract classes

```java
@Service(Book)
interface BookService {
    Book getBook(Serializable id)
}
```

---

# Advantages

- *Type safety*.
    - Signatures statically compiled.
<br/>
- *Testing*
    - Since they are interfaces, can be easily tested with Spock mocks.


---

# Advantages

- *Performance*.
    - No runtime proxies, eveything is compile-time.
<br/>
- *Transaction management*.
    - Methods are wrapped in a transaction (read-only for read operations).

---

# Abstract classes support

```java
@Service(Book)
abstract class BookService  {
   abstract Book getBook(Serializable id) 
   abstract Author getAuthor(Serializable id) 

   Book updateBook(Serializable id, Serializable authorId) { 
      Book book = getBook(id)
      if(book != null) {
          Author author = getAuthor(authorId)
          if(author == null) throw new IllegalArgumentException("Author does not exist")
          book.author = author
          book.save()
      }
      return book
   }
}
```

---

# Pagination support

Data service:

```java
@Service(Book)
interface BookService {
    List<Book> findBooks(String title, Map args)
}
```

Usage:

```groovy
List<Book> books = bookService.findBooks(
"The Stand",
    [offset:10, max:10, sort:'title', order:'desc']
)
```
---

# "Dynamic finder"-like queries

Just that they are statically compiled!

```java
@Service(Book)
interface BookService {

    List<Book> findByTitleAndPublishDateGreaterThan(String title, Date publishDate)

}
```


---

# Where queries

```java
@Service(Book)
interface BookService {

    @Where({ title ==~ pattern && releaseDate > fromDate })
    Book searchBooks(String pattern, Date fromDate)

}
```

---

# Query joins

```java
@Service(Book)
interface BookService {

    @Join('author')
    Book find(String title) 

    @Join(value='author', type=LEFT) 
    Book findAnother(String title)

}
```

---

# HQL queries

```java
@Service(Book)
interface BookService {

    @Query("from ${Book book} where ${book.title} like $pattern")
    Book searchByTitle(String pattern)

}
```

---

# Projections

```java
@Service(Book)
interface BookService {

    Date findBookReleaseDate(String title)

    List<Date> findBookReleaseDate(String publisher)

    @Query("select $b.releaseDate from ${Book b} where $b.publisher = $publisher order by $b.releaseDate")
    List<Date> findBookReleaseDatesHql(String publisher)

}
```

---

<br/>
<br/>
<br/>

# [fit] 3. Unit testing

---

# Unit testing

```java
class ClubSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore hibernateDatastore

    void setupSpec() {  hibernateDatastore = new HibernateDatastore(Club) }

    @Rollback
    void "it can persist clubs"() {
        when:
        new Club(name: "Real Madrid").save(flush: true)

        then:
        Club.count() == old(Club.count()) + 1
    }

}
```

---

<br/>
<br/>
<br/>

# [fit] 4. Multiple datasources

---

# `application.yml`

```yml
dataSource:
    dbCreate: create-drop
    url: "jdbc:h2:mem:laliga"

dataSources:
    premier:
        dbCreate: create-drop
        url: "jdbc:h2:mem:premier"
```

---

# Domain class

```groovy, [.highlight: 3-5]
class Club {

    static mapping = {
        datasource ConnectionSource.ALL
    }

    String name
    String stadium

}
```

---

# Data source namespace

```java
Club.premier.findByName("Liverpool")

//...

Club rma = new Club(name: "Real Madrid")
club.save() // Default data source
```

---

<br/>
<br/>
<br/>

# [fit] 5. Multi-Tenancy

---

# Modes

- *`DATABASE`*: a **separate database** with a **separate connection pool** is used to store each tenants data.
<br>
- *`SCHEMA`*: the **same database**, but **different schemas** are used to store each tenants data.
<br>
- *`DISCRIMINATOR`* - The **same database** is used with a **discriminator** used to partition and isolate data.

---

# AST Transformations

- *`@CurrentTenant`*: resolve the **current tenant** for the context of a class or method.
<br>
- *`@Tenant`*: use a **specific tenant** for the context of a class or method
<br>
- *`@WithoutTenant`*: execute logic **without a specific tenant** (using the default connection)

---

# AST Transformations

```java
@CurrentTenant // resolve the current tenant for every method
class TeamService {

    @WithoutTenant // execute the countPlayers method without a tenant id
    int countPlayers() {
        Player.count()
    }

    @Tenant({"another"}) // use the tenant id "another" for all GORM logic within the method
    List<Team> allTwoTeams() {
        Team.list()
    }

    List<Team> listTeams() {
        Team.list(max:10)
    }

    @Transactional
    void addTeam(String name) {
        new Team(name:name).save(flush:true)
    }
}
```

---

# Configuration

```yml
grails:
    gorm:
        multiTenancy:
            mode: DATABASE
            tenantResolverClass: org.grails.datastore.mapping.multitenancy.web.SubDomainTenantResolver
```

---

# Domain classes

```java
class Book implements MultiTenant<Book> {
    String title
}
```

---

# Querying

```groovy
List<Book> books = withCurrent {
    Book.list()
}

//...

List<Book> books = withId("otherTenant") {
    Book.list()
}
```

---

<br/>
<br/>
<br/>

# [fit] 6. RxGORM

---

# Domain class

```java
class Club implements RxMongoEntity<Club> {

    ObjectId id
    String name
    String stadium

}
```

---

# GORM operations

```java
new Book(title:"The Stand")
        .save() // Returns an rx.Observable<Book>
        .subscribe { Book it ->  
            // If you don't subscribe, it won't be executed
            println "ID = ${book.id}"
        }
```

---

[.hide-footer]

# [fit] Q & A

![inline](images/oci.png) ![inline](images/gr8conf.png)

Álvaro Sánchez-Mariscal
![inline](images//square-twitter-512.png)@alvaro_sanchez