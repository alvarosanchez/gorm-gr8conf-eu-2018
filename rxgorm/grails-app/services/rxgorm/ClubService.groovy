package rxgorm


import grails.gorm.services.Service
import org.bson.types.ObjectId

@Service(Club)
interface ClubService {

    int count()
    Club save(String name, String stadium)
    List<Club> findAll()
    Club find(ObjectId id)

}
