package multiple.datasources


import grails.gorm.services.Service

@Service(Club)
interface ClubService {

    int count()
    Club save(String name, String stadium)
    List<Club> findAll()
    Club find(Long id)

}
