package multiple.datasources

import org.grails.datastore.mapping.core.connections.ConnectionSource

class Club {

    static mapping = {
        datasource ConnectionSource.ALL
    }

    String name
    String stadium

    static constraints = {
        stadium nullable: true
    }

}