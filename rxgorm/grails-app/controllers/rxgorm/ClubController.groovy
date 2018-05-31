package rxgorm

import grails.rx.web.RxController
import grails.validation.ValidationException
import groovy.transform.CompileStatic

import static org.springframework.http.HttpStatus.*
import static rx.Observable.*
import grails.rx.web.*

@CompileStatic
class ClubController implements RxController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        zip( Club.list(params), Club.count() ) { List clubList, Number count ->
            rx.render view:"index", model:[clubList: clubList, clubCount: count]
        }
    }

    def show() {
        Club.get((Serializable)params.id)
    }

    def save() {
        rx.bindData(new Club(), request)
                .switchMap { Club club ->
            if(club.hasErrors()) {
                just(
                    rx.respond( club.errors, view:'create')
                )
            }
            else {
                club.save(flush:true)
                        .map { Club savedClub ->
                    rx.respond savedClub, [status: CREATED, view:"show"]
                }
                .onErrorReturn { Throwable e ->
                    if(e instanceof ValidationException) {
                        rx.respond e.errors, view:'create'
                    }
                    else {
                        log.error("Error saving entity: $e.message", e)
                        return INTERNAL_SERVER_ERROR
                    }
                }
            }

        }
    }

    def update() {
        def request = this.request
        Club.get((Serializable)params.id)
                    .switchMap { Club club ->
            rx.bindData( club, request )
                    .switchMap { Club updatedBook ->
                !updatedBook.hasErrors()? updatedBook.save() : updatedBook
            }
        }
        .map { Club club ->
            if(club.hasErrors()) {
                rx.respond club.errors, view:'edit'
            }
            else {
                rx.respond club, [status: OK, view:"show"]
            }
        }
        .switchIfEmpty(
            just( rx.render(status: NOT_FOUND) )
        )
        .onErrorReturn { Throwable e ->
            if(e instanceof ValidationException) {
                rx.respond e.errors, view:'edit'
            }
            else {
                log.error("Error saving entity: $e.message", e)
                return INTERNAL_SERVER_ERROR
            }
        }
    }

    def delete() {
        Club.get((Serializable)params.id)
                    .switchMap { Club club ->
            club.delete()
        }
        .map {
            rx.render status: NO_CONTENT
        }
    }
}
