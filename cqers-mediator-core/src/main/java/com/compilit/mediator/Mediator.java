package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.Query;

interface Mediator {

  <T extends Command<R>, R> R mediateCommand(T command);

  <T extends Query<R>, R> R mediateQuery(T query);

  <T extends Event> void mediateEvent(T event);

}
