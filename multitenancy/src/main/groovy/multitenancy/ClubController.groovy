package multitenancy

import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.TaskScheduler

import java.util.concurrent.ExecutorService

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
//        ExecutorService executorxService = applicationContext.getBean(ExecutorService, Qualifiers.byName(TaskExecutors.IO))
//        println "executorSerxvxice = $executorService"
        println "listClubs: ${Thread.currentThread().name}"
        return clubService.findAll()
    }

    @Override
    @Get("/{id}")
    Club show(Long id) {
        return clubService.find(id)
    }
}