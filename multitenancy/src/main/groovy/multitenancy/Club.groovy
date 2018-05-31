package multitenancy

import grails.gorm.MultiTenant
import grails.gorm.annotation.Entity

@Entity
class Club implements MultiTenant<Club>{

    String tenantId
    String name
    String stadium

    static constraints = {
        stadium nullable: true
    }

}