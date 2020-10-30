package net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.service;

import io.eventuate.EntityWithMetadata;
import io.eventuate.EventuateAggregateStore;

import java.util.concurrent.CompletableFuture;

public class TaskHistoryService {

  private EventuateAggregateStore eventStore;

  public TaskHistoryService(EventuateAggregateStore eventStore) {
    this.eventStore = eventStore;
  }

  public CompletableFuture<EntityWithMetadata<TaskAggregate>> getHistoryEvents(String taskId) {
    return eventStore.find(TaskAggregate.class, taskId);
  }
}
