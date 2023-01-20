//package com.compilit.mediator;
//
//import static com.compilit.functions.FunctionResultGuards.orNull;
//import static com.compilit.functions.FunctionResultGuards.orNullOnException;
//
//import com.compilit.mediator.api.Event;
//import com.compilit.mediator.api.Request;
//import com.compilit.mediator.api.RequestHandler;
//import com.compilit.mediator.api.annotations.Emittable;
//import com.compilit.mediator.api.annotations.OnExecution;
//import com.compilit.mediator.api.annotations.OnSuccess;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.AbstractMap.SimpleEntry;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import org.springframework.context.support.GenericApplicationContext;
//
//class AnnotationBasedEventHandler {
//
//  private final Map<Class<?>, List<Entry<Object, Method>>> handlerMethodCache = new HashMap<>();
//  private final Map<Class<?>, Event> instantiatedEventCache = new HashMap<>();
//  private final EventHandlerProvider eventHandlerProvider;
//
//  AnnotationBasedEventHandler(EventHandlerProvider eventHandlerProvider) {this.eventHandlerProvider = eventHandlerProvider;}
//
//  public <T extends Request> void preHandle(RequestHandler<T, ?> requestHandler, T request) {
//    handle(emitOnExecution(), requestHandler, request, OnExecution.class);
//  }
//
//  public <T extends Request> void postHandle(RequestHandler<T, ?> requestHandler, T request) {
//    handle(emitOnSuccess(), requestHandler, request, OnSuccess.class);
//  }
//
//  void resolveEventHandlers(GenericApplicationContext genericApplicationContext) {
//    genericApplicationContext.getBeansWithAnnotation(Emittable.class)
//                             .entrySet()
//                             .stream()
//                             .filter(methodsContainsEventAnnotations())
//                             .forEach(processCache());
//  }
//
//  private <T extends Request> void handle(Function<Annotation, Class<? extends Event>> function,
//                                          RequestHandler<T, ?> requestHandler,
//                                          T request,
//                                          Class<? extends Annotation> annotation) {
//    var onExecutionAnnotations = findAll(requestHandler, request, annotation);
//    if (!onExecutionAnnotations.isEmpty()) {
//      onExecutionAnnotations.forEach(handleEvent(function));
//    }
//  }
//
//  private Consumer<Annotation> handleEvent(Function<Annotation, Class<? extends Event>> function) {
//    return annotation -> {
//      var clazz = function.apply(annotation);
//      if (instantiatedEventCache.containsKey(clazz)) {
//        handle(instantiatedEventCache.get(clazz));
//      } else {
//        handlerMethodCache.get(clazz).forEach(eventListMap -> {
//          var event = instantiateEvent(clazz);
//          instantiatedEventCache.put(clazz, event);
//          handle(event);
//        });
//      }
//    };
//  }
//
//  private void handle(Event event) {
//    if (event == null || !instantiatedEventCache.containsKey(event.getClass())) {
//      return;
//    }
//    handlerMethodCache.get(event.getClass())
//                      .forEach(entry -> eventHandlerProvider.getEventHandlers(event).forEach(x -> x.handle(event)));
//  }
//
//  private <T extends Request> List<? extends Annotation> findAll(RequestHandler<T, ?> requestHandler,
//                                                                 T request,
//                                                                 Class<? extends Annotation> annotation) {
//    return Arrays.stream(requestHandler.getClass().getDeclaredMethods())
//                 .filter(x -> Arrays.stream(x.getParameterTypes())
//                                    .allMatch(z -> request.getClass().equals(z)))
//                 .filter(x -> x.getAnnotation(annotation) != null)
//                 .map(x -> x.getAnnotation(annotation))
//                 .toList();
//  }
//
//  private Consumer<Entry<String, Object>> processCache() {
//    return annotatedBean -> Arrays.stream(annotatedBean.getValue()
//                                                       .getClass()
//                                                       .getDeclaredMethods())
//                                  .forEach(resolveCache(annotatedBean));
//  }
//
//  private Consumer<Method> resolveCache(Object annotatedBean) {
//    return method -> {
//      var onExecution = orNull(() -> method.getAnnotation(OnExecution.class));
//      var onSuccess = orNull(() -> method.getAnnotation(OnSuccess.class));
//      if (onExecution != null) {
//        putInCache(annotatedBean, method, onExecution.emit());
//      }
//      if (onSuccess != null) {
//        putInCache(annotatedBean, method, onSuccess.emit());
//      }
//    };
//  }
//
//  private void putInCache(Object annotatedBean, Method method, Class<? extends Event> eventClass) {
//    var entries = new ArrayList<Entry<Object, Method>>();
//    entries.add(new SimpleEntry<>(annotatedBean, method));
//    handlerMethodCache.put(eventClass, entries);
//  }
//
//  private static Function<Annotation, Class<? extends Event>> emitOnExecution() {
//    return annotation -> {
//      var annotationClass = OnExecution.class;
//      var onExecution = annotationClass.cast(annotation);
//      return onExecution.emit();
//    };
//  }
//
//  private static Function<Annotation, Class<? extends Event>> emitOnSuccess() {
//    return annotation -> {
//      var annotationClass = OnSuccess.class;
//      var onExecution = annotationClass.cast(annotation);
//      return onExecution.emit();
//    };
//  }
//
//  private static Event instantiateEvent(Class<? extends Event> annotation) {
//    return (Event) orNullOnException(() -> Arrays.stream(annotation.getDeclaredConstructors())
//                                                 .filter(ctor -> ctor.getParameterCount() == 0)
//                                                 .findFirst()
//                                                 .orElseThrow()
//                                                 .newInstance());
//  }
//
//  private static Predicate<Entry<String, Object>> methodsContainsEventAnnotations() {
//    return beanDefinitionEntry -> Arrays.stream(beanDefinitionEntry.getValue().getClass().getDeclaredMethods())
//                                        .anyMatch(methodsIsAnnotatedWithEventAnnotations());
//  }
//
//  private static Predicate<Method> methodsIsAnnotatedWithEventAnnotations() {
//    return m -> m.getAnnotation(OnExecution.class) != null
//      || m.getAnnotation(OnSuccess.class) != null;
//  }
//
//}
