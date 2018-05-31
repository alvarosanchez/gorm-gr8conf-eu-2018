package multitenancy

import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
@CompileStatic
class ClubController implements ClubsApi {

    final ClubService clubService
    final ApplicationContext applicationContext

    ClubController(ClubService clubService, ApplicationContext applicationContext) {
        this.clubService = clubService
        this.applicationContext = applicationContext
    }

    @Get("/")
    List<Club> listClubs() {
        println "listClubs: ${Thread.currentThread().name}"
        return clubService.findAll()
    }

    @Override
    @Get("/{id}")
    Club show(Long id) {
        return clubService.find(id)
    }
}