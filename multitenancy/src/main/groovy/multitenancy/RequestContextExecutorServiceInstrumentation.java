package multitenancy;

import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.http.HttpRequest;
import io.micronaut.scheduling.instrument.InstrumentedExecutorService;
import io.micronaut.scheduling.instrument.InstrumentedScheduledExecutorService;

import javax.inject.Singleton;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
public class RequestContextExecutorServiceInstrumentation implements BeanCreatedEventListener<ExecutorService> {
    @Override
    public ExecutorService onCreated(BeanCreatedEvent<ExecutorService> event) {
        ExecutorService executorService = event.getBean();
        if(executorService instanceof ScheduledExecutorService) {
            return new InstrumentedScheduledExecutorService() {
                @Override
                public ScheduledExecutorService getTarget() {
                    return (ScheduledExecutorService) executorService;
                }

                @Override
                public <T> Callable<T> instrument(Callable<T> task) {
                    return doInstrument(task);
                }

                @Override
                public Runnable instrument(Runnable command) {
                    return doInstrument(command);
                }
            };
        }
        else {
            return new InstrumentedExecutorService() {
                @Override
                public ExecutorService getTarget() {
                    return executorService;
                }

                @Override
                public <T> Callable<T> instrument(Callable<T> task) {
                    return doInstrument(task);
                }

                @Override
                public Runnable instrument(Runnable command) {
                    return doInstrument(command);
                }
            };
        }
    }

    private Runnable doInstrument(Runnable command) {
        HttpRequest request = RequestContext.current();
        if(request != null) {
            return () -> RequestContext.with(request, command);
        }
        return command;
    }

    private <T> Callable<T> doInstrument(Callable<T> task) {
        HttpRequest request = RequestContext.current();
        if(request != null) {
            return () -> RequestContext.with(request, task);
        }
        return task;
    }
}
