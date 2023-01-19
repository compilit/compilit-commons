package com.compilit.mediator;

import static com.compilit.functions.FunctionResultGuards.orNullOnException;

import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.annotations.OnSuccess;
import com.compilit.mediator.api.Request;
import com.compilit.mediator.api.RequestHandler;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.context.support.GenericApplicationContext;

class AnnotationBasedEventHandler {

  private final Map<Class<?>, List<Entry<Object, Method>>> eventListMap = new HashMap<>();
  private final Map<Class<?>, Event> instantiatedEvents = new HashMap<>();
  private final EventHandlerProvider eventHandlerProvider;

  AnnotationBasedEventHandler(EventHandlerProvider eventHandlerProvider) {this.eventHandlerProvider = eventHandlerProvider;}

  public <T extends Request> void preHandle(RequestHandler requestHandler, T command) {
    var annotations = Arrays.stream(requestHandler.getClass().getDeclaredMethods())
                            .filter(x -> Arrays.stream(x.getParameterTypes())
                                               .allMatch(z -> command.getClass().equals(z)))
                            .filter(x -> x.getAnnotation(OnSuccess.class) != null)
                            .map(x -> x.getAnnotation(OnSuccess.class))
                            .toList();
    if (!annotations.isEmpty()) {
      annotations.forEach(annotation -> {
        var clazz = annotation.emit();
        if (instantiatedEvents.entrySet().stream().anyMatch(x -> x.getKey().equals(clazz))) {
          handle(instantiatedEvents.get(annotation.emit()));
        } else {
          eventListMap.get(annotation.emit()).forEach(eventListMap -> {
            var event = getEvent(annotation);
            instantiatedEvents.put(clazz, event);
            handle(event);
          });
        }
      });
    }
  }

  private static Event getEvent(OnSuccess annotation) {
    return (Event) orNullOnException(() -> Arrays.stream(annotation.emit().getDeclaredConstructors())
                                                 .filter(ctor -> ctor.getParameterCount() == 0)
                                                 .findFirst()
                                                 .orElse(null)
                                                 .newInstance());
  }

  void resolveEventHandlers(GenericApplicationContext genericApplicationContext) {
    Arrays.stream(genericApplicationContext.getBeanDefinitionNames())
          .map(genericApplicationContext::getBean)
          .filter(beanDefinition -> Arrays.stream(beanDefinition.getClass().getDeclaredMethods())
                                          .anyMatch(m -> m.getAnnotation(OnSuccess.class) != null))
          .forEach(x -> Arrays.stream(x.getClass().getDeclaredMethods())
                              .filter(m -> m.getAnnotation(OnSuccess.class) != null)
                              .forEach(z -> {
                                var annotation = z.getAnnotation(OnSuccess.class);
                                var eventClass = annotation.emit();
                                eventListMap.put(eventClass, new ArrayList<>());
                                eventListMap.get(eventClass).add(new SimpleEntry<>(x, z));
                              }));
  }

  public void handle(Event event) {
    if (event == null) {
      return;
    }
    eventListMap.get(event.getClass()).forEach(entry -> {
//      try {
      eventHandlerProvider.getEventHandlers(event).forEach(x -> x.handle(event));
//        entry.getValue().invoke(entry.getKey(), event);
//      } catch (IllegalAccessException e) {
//        throw new RuntimeException(e);
//      } catch (InvocationTargetException e) {
//        throw new RuntimeException(e);
//      }
    });
  }

}
