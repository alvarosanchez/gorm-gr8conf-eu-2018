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
            if(existing == null) {
                isSet = true;
                REQUEST.set(request);
            }
            runnable.run();
        } finally {
            if(isSet) {
                REQUEST.remove();
            }
        }
    }

    static <T> T with(HttpRequest request, Callable<T> runnable) throws Exception {
        try {
            REQUEST.set(request);
            return runnable.call();
        } finally {
            REQUEST.remove();
        }
    }

    static HttpRequest current() {
        return REQUEST.get();
    }
}

