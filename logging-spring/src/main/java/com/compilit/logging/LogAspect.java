package com.compilit.logging;

import com.compilit.logging.api.Logged;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

  private final MethodPhaseChainBuilder methodPhaseChainBuilder;

  LogAspect(MethodPhaseChainBuilder methodPhaseChainBuilder, LoggingConfiguration loggingConfiguration) {
    this.methodPhaseChainBuilder = methodPhaseChainBuilder.addPhase(new OnCallMethodPhase())
                                                          .addPhase(new BeforeExecutionMethodPhase())
                                                          .addPhase(new AfterExecutionMethodPhase())
                                                          .addPhase(new OnResultMethodPhase())
                                                          .addPhase(new OnExceptionMethodPhase())
                                                          .configure(loggingConfiguration);
  }

  @Around(value = "@annotation(annotation)")
  public Object logScope(ProceedingJoinPoint joinPoint, Logged annotation) throws Throwable {
    var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var logEntries = List.of(annotation.logs());
    return methodPhaseChainBuilder.process(joinPoint, logger, logEntries, annotation);
  }

}
