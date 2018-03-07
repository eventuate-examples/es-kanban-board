
package net.chrisrichardson.eventstore.examples.kanban.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.Board;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.BoardResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDescription;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.*;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.ChangeTaskStatusRequest;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.Task;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.TaskResponse;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.KanbanWebSocketEvent;
import net.chrisrichardson.eventstore.examples.kanban.testutil.util.StompListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static net.chrisrichardson.eventstore.examples.kanban.testutil.util.TestUtil.awaitPredicatePasses;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractStompApiTest extends BaseTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldCreateBoards() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);
            assertBoardInfoEquals(boardInfo, transformToBoardInfo(boardResponse));

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "BoardCreatedEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "BoardCreatedEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(BoardCreatedEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Board board = parseStompEvent(wsEvent, Board.class);
            assertBoardInfoEquals(boardInfo, transformToBoardInfo(board));
        });
    }

    @Test
    public void shouldCreateTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            assertTaskInfoEquals(taskInfo, transformToTaskInfo(taskResponse));

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskCreatedEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskCreatedEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskCreatedEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);
            assertTaskInfoEquals(taskInfo, transformToTaskInfo(task));
        });
    }

    @Test
    public void shouldUpdateTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);
            TaskDetails taskDetails = new TaskDetails(
                    "small task",
                    new TaskDescription("description"));

            TaskResponse taskUpdatedResponse = updateTaskAndWaitInView(t, taskResponse.getId(), taskDetails);

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskUpdatedEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskUpdatedEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskUpdatedEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);

            Assert.assertEquals(taskUpdatedResponse.getDescription().getDescription(), task.getDescription());
            Assert.assertEquals(taskUpdatedResponse.getUpdatedBy(), task.getUpdatedBy());
            Assert.assertEquals(taskUpdatedResponse.getUpdatedDate(), task.getUpdatedDate());
            Assert.assertEquals(taskUpdatedResponse.getTitle(), task.getTitle());
        });
    }

    @Test
    public void shouldBacklogTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskInfo taskInfo = generateTaskInfo();
            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            TaskResponse taskChangedResponse = changeTaskState(token, backlogTaskUrl(taskResponse.getId()), new ChangeTaskStatusRequest(boardResponse.getId()));

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskBacklogEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskBacklogEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskBacklogEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);

            Assert.assertEquals(taskChangedResponse.getId(), task.getId());
            Assert.assertEquals(taskChangedResponse.getUpdatedBy(), task.getUpdatedBy());
            Assert.assertEquals(taskChangedResponse.getUpdatedDate(), task.getUpdatedDate());
            Assert.assertEquals("", task.getBoardId());
            Assert.assertEquals(taskChangedResponse.getStatus(), task.getStatus());
        });
    }

    @Test
    public void shouldCompleteTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskInfo taskInfo = generateTaskInfo();
            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            TaskResponse taskChangedResponse = changeTaskState(token, completeTaskUrl(taskResponse.getId()), new ChangeTaskStatusRequest(boardResponse.getId()));

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskCompletedEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskCompletedEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskCompletedEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);

            Assert.assertEquals(taskChangedResponse.getId(), task.getId());
            Assert.assertEquals(taskChangedResponse.getUpdatedBy(), task.getUpdatedBy());
            Assert.assertEquals(taskChangedResponse.getUpdatedDate(), task.getUpdatedDate());
            Assert.assertEquals(taskChangedResponse.getBoardId(), task.getBoardId());
            Assert.assertEquals(taskChangedResponse.getStatus(), task.getStatus());
        });
    }

    @Test
    public void shouldDeleteTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            TaskInfo taskInfo = generateTaskInfo();
            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            TaskResponse taskChangedResponse = deleteTaskAndWaitInView(token, taskResponse.getId());

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskDeletedEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskDeletedEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskDeletedEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);

            Assert.assertEquals(taskChangedResponse.getId(), task.getId());
            Assert.assertEquals(taskChangedResponse.getUpdatedBy(), task.getUpdatedBy());
            Assert.assertEquals(taskChangedResponse.getUpdatedDate(), task.getUpdatedDate());
            Assert.assertTrue(task.isDeleted());
        });
    }

    @Test
    public void shouldScheduleTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskInfo taskInfo = generateTaskInfo();
            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            TaskResponse taskChangedResponse = changeTaskState(token, scheduleTaskUrl(taskResponse.getId()), new ChangeTaskStatusRequest(boardResponse.getId()));

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskScheduledEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskScheduledEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskScheduledEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);

            Assert.assertEquals(taskChangedResponse.getId(), task.getId());
            Assert.assertEquals(taskChangedResponse.getUpdatedBy(), task.getUpdatedBy());
            Assert.assertEquals(taskChangedResponse.getUpdatedDate(), task.getUpdatedDate());
            Assert.assertEquals(taskChangedResponse.getBoardId(), task.getBoardId());
            Assert.assertEquals(taskChangedResponse.getStatus(), task.getStatus());
        });
    }

    @Test
    public void shouldStartTasks() throws IOException, InterruptedException {
        withToken(t -> {
            StompListener taskEventsListener = new StompListener(t, "/events", getHost(), getPort());

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskInfo taskInfo = generateTaskInfo();
            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            TaskResponse taskChangedResponse = changeTaskState(token, startTaskUrl(taskResponse.getId()), new ChangeTaskStatusRequest(boardResponse.getId()));

            awaitPredicatePasses(idx -> taskEventsListener.getEvents(),
                    (list) -> wsEventsContainsEvent(list, "TaskStartedEvent"));
            KanbanWebSocketEvent wsEvent = filterWSEvents(taskEventsListener.getEvents(), "TaskStartedEvent");
            Assert.assertNotNull(wsEvent.getEntityId());
            Assert.assertEquals(TaskStartedEvent.class.getName(), wsEvent.getEventType());

            validateWSMessage(wsEvent.getEventType(), wsEvent.getEventData());

            Task task = parseStompEvent(wsEvent, Task.class);

            Assert.assertEquals(taskChangedResponse.getId(), task.getId());
            Assert.assertEquals(taskChangedResponse.getUpdatedBy(), task.getUpdatedBy());
            Assert.assertEquals(taskChangedResponse.getUpdatedDate(), task.getUpdatedDate());
            Assert.assertEquals(taskChangedResponse.getBoardId(), task.getBoardId());
            Assert.assertEquals(taskChangedResponse.getStatus(), task.getStatus());
        });
    }

    private <T> T parseStompEvent(KanbanWebSocketEvent stompEvent, Class<T> clazz) {
        try {
            return mapper.readValue(stompEvent.getEventData(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}