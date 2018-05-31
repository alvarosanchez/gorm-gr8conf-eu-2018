package multitenancy

import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener

import javax.inject.Singleton
import java.util.concurrent.ThreadFactory

@Singleton
class ThreadFactoryInstrumenter implements BeanCreatedEventListener<ThreadFactory> {

    @Override
    ThreadFactory onCreated(BeanCreatedEvent<ThreadFactory> event) {
        ThreadFactory factory = event.bean
        return { Runnable r ->
            factory.newThread(new RequestContext(r))
        }
    }

}
