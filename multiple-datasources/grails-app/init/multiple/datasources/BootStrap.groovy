package multiple.datasources

class BootStrap {

    ClubService clubService

    def init = { servletContext ->
        [
            new Club(name: "Real Madrid CF", stadium: "Santiago Bernabeu"),
            new Club(name: "FC Barcelona", stadium: "Camp Nou"),
        ].each {
            it.save()
        }

        [
            new Club(name: "Manchester City", stadium: "Etihad Stadium"),
            new Club(name: "Liverpool", stadium: "Anfield"),
        ].each {
            it.premier.save()
        }

    }
    def destroy = {
    }
}
