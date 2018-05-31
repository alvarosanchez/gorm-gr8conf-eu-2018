package multitenancy

import grails.gorm.multitenancy.Tenants
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent

import javax.inject.Singleton

//tag::class[]
@Slf4j
@CompileStatic
@Singleton // <1>
@Requires(notEnv = Environment.TEST) // <2>
class DataLoader implements ApplicationEventListener<ServerStartupEvent> { // <3>

    final ClubService clubService

    DataLoader(ClubService clubService) {
        this.clubService = clubService
    }

    @Override
    void onApplicationEvent(ServerStartupEvent event) { // <4>
        if (!clubService.count()) {
            log.debug "Loading sample data"
            Tenants.withId("laliga") {
                clubService.save("Real Madrid CF", "Santiago Bernabeu")
                clubService.save("FC Barcelona", "Camp Nou")
                clubService.save("CD Leganes", "Butarque")
                clubService.save("Getafe CF", "Coliseum")
            }
            Tenants.withId("premierleague") {
                clubService.save("Manchester City", "Etihad Stadium")
                clubService.save("Manchester United", "Old Trafford")
                clubService.save("Liverpool", "Anfield")
                clubService.save("Chelsea", "Stamford Bridge")
            }
        }
    }
}
//end::class[]
