package org.example.gorm.standalone

import groovy.util.logging.Slf4j
import org.grails.orm.hibernate.HibernateDatastore

@Slf4j
class Application {

    static void main(String[] args) {
        Map configuration = [
                'hibernate.hbm2ddl.auto':'create-drop',
                'dataSource.url':'jdbc:h2:mem:myDB'
        ]
        HibernateDatastore datastore = new HibernateDatastore(configuration, Club)
        ClubService clubService = datastore.getService(ClubService)

        clubService.save("Real Madrid CF", "Santiago Bernabeu")
        clubService.save("FC Barcelona", "Camp Nou")
        clubService.save("CD Leganes", "Butarque")
        clubService.save("Getafe CF", "Coliseum")

        clubService.findAll().each {
            log.info "Team ${it.name} with ID ${it.id} plays at ${it.stadium}"
        }
    }
}
