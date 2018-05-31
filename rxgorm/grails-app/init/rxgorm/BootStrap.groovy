package rxgorm

import rx.Observable

class BootStrap {

    def init = { servletContext ->

        Observable<Club> o1 = new Club(name: "Real Madrid CF", stadium: "Santiago Bernabeu").save(flush: true)
        Observable<Club> o2 = new Club(name: "FC Barcelona", stadium: "Camp Nou").save(flush: true)

        Observable.zip(o1, o2, { Club c1, Club c2 ->
            [c1, c2]
        }).subscribe { List<Club> clubs ->
            println "Clubs created!"
        }

    }
    def destroy = {
    }
}
