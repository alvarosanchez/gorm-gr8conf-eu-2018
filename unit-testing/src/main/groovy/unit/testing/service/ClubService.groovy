package unit.testing.service

import grails.gorm.services.Service
import unit.testing.domain.Club

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Service(Club)
interface ClubService {

//tag::operations[]
    int count()
    Club save(@NotBlank String name, @NotBlank String stadium)
    List<Club> findAll()
    Club find(@NotNull Long id)
//end::operations[]

}
