package multitenancy

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.services.Service

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Service(Club)
@CurrentTenant
interface ClubService {

//tag::operations[]
    int count()
    Club save(@NotBlank String name, @NotBlank String stadium)
    List<Club> findAll()
    Club find(@NotNull Long id)
//end::operations[]

}
