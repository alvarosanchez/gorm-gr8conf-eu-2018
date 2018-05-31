package org.example.gorm.standalone


import grails.gorm.services.Service

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Service(Club)
interface ClubService {

    int count()
    Club save(@NotBlank String name, @NotBlank String stadium)
    List<Club> findAll()
    Club find(@NotNull Long id)

}
