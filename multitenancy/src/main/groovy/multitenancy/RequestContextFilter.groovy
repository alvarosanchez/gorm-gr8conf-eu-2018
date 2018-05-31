package multitenancy

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher

@Filter("/**")
class RequestContextFilter implements HttpServerFilter {

    @Override
    Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        println "Host within filter: ${request.headers.get('Host')}"
        return new RequestTracingPublisher(request, chain.proceed(request))
    }
}
