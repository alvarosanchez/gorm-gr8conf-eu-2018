package multitenancy

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher

import java.util.concurrent.Callable

@Filter("/**")
class RequestContextFilter implements HttpServerFilter {

    @Override
    Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return RequestContext.with(request, {
            return new RequestTracingPublisher(request, chain.proceed(request))
        } as Callable<Publisher<MutableHttpResponse<?>>>)
    }
}
