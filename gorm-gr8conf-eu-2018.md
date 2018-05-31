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

# [fit] 1. Data Services

---

# Data Services

- Auto-generated persistence logic from interfaces and abstract classes

```groovy
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

```groovy, [.highlight: 7-18]
@Service(Book)
abstract class BookService  {

   abstract Book getBook(Serializable id) 
   abstract Author getAuthor(Serializable id) 

   Book updateBook(Serializable id, Serializable authorId) { 
      Book book = getBook(id)
      if(book != null) {
          Author author = getAuthor(authorId)
          if(author == null) {
              throw new IllegalArgumentException("Author does not exist")
          }
          book.author = author
          book.save()
      }
      return book
   }
}
```

---

[.hide-footer]

# [fit] Q & A

![inline](images/oci.png) ![inline](images/gr8conf.png)

Álvaro Sánchez-Mariscal
![inline](images//square-twitter-512.png)@alvaro_sanchez