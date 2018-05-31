package unit.testing

import grails.gorm.transactions.Rollback
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import unit.testing.domain.Club

class ClubSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore hibernateDatastore

    void setupSpec() {
        hibernateDatastore = new HibernateDatastore(Club)
    }

    @Rollback
    void "it can persist clubs"() {
        when:
        new Club(name: "Real Madrid").save(flush: true)

        then:
        Club.count() == old(Club.count()) + 1
    }

}
