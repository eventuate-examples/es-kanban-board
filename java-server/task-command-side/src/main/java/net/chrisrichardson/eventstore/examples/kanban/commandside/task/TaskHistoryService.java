package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.EntityIdentifier;
import net.chrisrichardson.eventstore.EntityWithMetadata;
import net.chrisrichardson.eventstore.EventStore;
import rx.Observable;

/**
 * Created by popikyardo on 03.11.15.
 */
public class TaskHistoryService {

    private EventStore eventStore;

    public TaskHistoryService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Observable<EntityWithMetadata<TaskAggregate>> getHistoryEvents(String taskId) {
        return eventStore.find(TaskAggregate.class, new EntityIdentifier(taskId));
    }
}
