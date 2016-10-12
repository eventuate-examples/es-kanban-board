/**
 * Created by andrew on 28/01/16.
 */
import BoardCreatedEventSchema from '../../schemas/websocket-events-schema/BoardCreatedEvent.json';
import TaskCreatedEventSchema from '../../schemas/websocket-events-schema/TaskCreatedEvent.json';
import TaskDeletedEventSchema from '../../schemas/websocket-events-schema/TaskDeletedEvent.json';
import TaskScheduledEventSchema from '../../schemas/websocket-events-schema/TaskScheduledEvent.json';
import TaskStartedEventSchema from '../../schemas/websocket-events-schema/TaskStartedEvent.json';
import TaskCompletedEventSchema from '../../schemas/websocket-events-schema/TaskCompletedEvent.json';
import TaskBacklogEventSchema from '../../schemas/websocket-events-schema/TaskBacklogEvent.json';

export const BoardCreatedEvent = BoardCreatedEventSchema;
export const TaskCreatedEvent = TaskCreatedEventSchema;
export const TaskDeletedEvent = TaskDeletedEventSchema;
export const TaskScheduledEvent = TaskScheduledEventSchema;
export const TaskStartedEvent = TaskStartedEventSchema;
export const TaskCompletedEvent = TaskCompletedEventSchema;
export const TaskBacklogEvent = TaskBacklogEventSchema;