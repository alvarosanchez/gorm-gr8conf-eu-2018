package multitenancy


import io.micronaut.http.annotation.Get

interface ClubsApi {

    @Get("/")
    List<Club> listClubs()

    @Get("/{id}")
    Club show(Long id)

}