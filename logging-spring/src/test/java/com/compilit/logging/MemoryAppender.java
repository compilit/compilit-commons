package com.compilit.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class MemoryAppender extends ListAppender<ILoggingEvent> {

  public void reset() {
    this.list.clear();
  }

  public boolean contains(String string, Level level) {
    return this.list.stream()
                    .anyMatch(event -> event.toString().contains(string)
                      && event.getLevel().equals(level));
  }

  public boolean contains(Predicate<String> stringPredicate, Level level) {
    return this.list.stream()
                    .anyMatch(event -> stringPredicate.test(event.toString())
                      && event.getLevel().equals(level));
  }

  public int countEventsForLogger(String loggerName) {
    return (int) this.list.stream()
                          .filter(event -> event.getLoggerName().contains(loggerName))
                          .count();
  }

  public List<ILoggingEvent> search(String string) {
    return this.list.stream()
                    .filter(event -> event.toString().contains(string))
                    .collect(Collectors.toList());
  }

  public List<ILoggingEvent> search(String string, Level level) {
    return this.list.stream()
                    .filter(event -> event.toString().contains(string)
                      && event.getLevel().equals(level))
                    .collect(Collectors.toList());
  }

  public int getSize() {
    return this.list.size();
  }

  public List<ILoggingEvent> getLoggedEvents() {
    return Collections.unmodifiableList(this.list);
  }
}