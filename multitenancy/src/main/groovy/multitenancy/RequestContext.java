package multitenancy;

import io.micronaut.http.HttpRequest;

import java.util.concurrent.Callable;

public class RequestContext implements Runnable {

    private static final ThreadLocal<HttpRequest> REQUEST = new ThreadLocal<>();

    private final HttpRequest request;
    private final Runnable runnable;

    RequestContext(Runnable runnable) {
        request = REQUEST.get();
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if(request != null) {
            with(request, runnable);
        } else {
            this.runnable.run();
        }
    }

    static void with(HttpRequest request, Runnable runnable) {
        HttpRequest existing = REQUEST.get();
        boolean isSet = false;
        try {
//            System.out.println("storing request in " + Thread.currentThread().getName());
            if(existing == null) {
                isSet = true;
                REQUEST.set(request);
            }
            runnable.run();
        } finally {
//            System.out.println("removing request from " + Thread.currentThread().getName());
            if(isSet) {
                REQUEST.remove();
            }
        }
    }

    static <T> T with(HttpRequest request, Callable<T> runnable) throws Exception {
        try {
//            System.out.println("storing request in " + Thread.currentThread().getName());
            REQUEST.set(request);
            return runnable.call();
        } finally {
//            System.out.println("removing request from " + Thread.currentThread().getName());
            REQUEST.remove();
        }
    }

    static HttpRequest current() {
        return REQUEST.get();
    }
}

