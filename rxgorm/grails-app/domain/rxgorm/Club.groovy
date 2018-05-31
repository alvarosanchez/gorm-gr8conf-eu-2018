package rxgorm

import grails.gorm.rx.mongodb.RxMongoEntity
import org.bson.types.ObjectId

class Club implements RxMongoEntity<Club> {

    ObjectId id
    String name
    String stadium

    static constraints = {
        stadium nullable: true
    }

}