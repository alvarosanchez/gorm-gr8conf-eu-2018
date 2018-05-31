package multitenancy

import io.micronaut.http.HttpRequest

class RequestContext implements Runnable {

    private static final ThreadLocal<HttpRequest> REQUEST = new ThreadLocal<>()

    final HttpRequest request
    final Runnable runnable

    RequestContext(Runnable runnable) {
        request = REQUEST.get()
        this.runnable = runnable
    }

    @Override
    void run() {
        if(request != null) {
            with(request, runnable)
        } else {
            this.runnable.run()
        }
    }

    static void with(HttpRequest request, Runnable runnable) {
        try {
            REQUEST.set(request)
            runnable.run()
        } finally {
            REQUEST.remove()
        }
    }

    static HttpRequest current() {
        return REQUEST.get()
    }

    static void set(HttpRequest request) {
        REQUEST.set(request)
    }
}
