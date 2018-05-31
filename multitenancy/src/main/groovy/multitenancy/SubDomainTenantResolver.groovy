package multitenancy

import io.micronaut.http.HttpRequest
import org.grails.datastore.mapping.core.connections.ConnectionSource
import org.grails.datastore.mapping.multitenancy.AllTenantsResolver
import org.grails.datastore.mapping.multitenancy.TenantResolver
import org.grails.datastore.mapping.multitenancy.exceptions.TenantNotFoundException

class SubDomainTenantResolver implements TenantResolver, AllTenantsResolver {

    @Override
    Serializable resolveTenantIdentifier() throws TenantNotFoundException {
        HttpRequest request = RequestContext.current()
        if (request) {
            String subdomain = request.headers.get('Host')
            println "Host within tenant resolver: ${subdomain}"
            if (subdomain.indexOf(".") > -1) {
                return "laliga"
            } else {
                return "laliga"
            }
        } else {
            println "No request, using default"
            return ConnectionSource.DEFAULT
        }
    }

    @Override
    Iterable<Serializable> resolveTenantIds() {
        return ['laliga', 'premierleague']
    }
}
