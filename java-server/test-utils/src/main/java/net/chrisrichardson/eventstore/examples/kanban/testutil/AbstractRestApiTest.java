package net.chrisrichardson.eventstore.examples.kanban.testutil;

import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.BoardQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.BoardResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.BoardsQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDescription;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.BacklogResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.ChangeTaskStatusRequest;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.HistoryResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.TaskResponse;
import net.chrisrichardson.eventstore.examples.kanban.testutil.model.TestHistoryResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static net.chrisrichardson.eventstore.examples.kanban.testutil.util.TestUtil.awaitPredicatePasses;
import static org.junit.Assert.assertTrue;

/**
 * Created by popikyardo on 07.01.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest({"server.port=0"})
public abstract class AbstractRestApiTest extends BaseTest {

    @Test
    public void shouldGetBoardById() throws IOException, InterruptedException {
        withToken(t -> {
            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            BoardQueryResponse savedBoardResponse = getBoard(t, boardResponse.getId());

            assertBoardInfoEquals(boardInfo, transformToBoardInfo(savedBoardResponse.getData()));
        });
    }

    @Test
    public void shouldGetAllBoards() throws IOException, InterruptedException {
        withToken(t -> {
            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            BoardsQueryResponse boardsResponse = getAllBoards(t);

            assertBoardContains(transformToBoard(boardResponse.getId(), boardResponse), boardsResponse.getBoards());
        });
    }

    @Test
    public void shouldGetBacklogTask() throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            assertBackLogContains(t, taskResponse.getId(), taskResponse);
        });
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            deleteTaskAndWaitInView(t, taskResponse.getId());

            BacklogResponse backlogResponse = getBacklogTasks(t);

            assertTaskNotIn(transformToTask(taskResponse.getId(), taskResponse),
                    backlogResponse.getBacklog());
        });
    }


    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            TaskDetails taskDetails = new TaskDetails(
                    "small task",
                    new TaskDescription("description"));

            TaskResponse updatedTaskResponse = updateTaskAndWaitInView(t, taskResponse.getId(), taskDetails);

            BacklogResponse backlogResponse = getBacklogTasks(t);

            assertTrue(taskListContains(transformToTask(taskResponse.getId(), updatedTaskResponse),
                    backlogResponse.getBacklog()));
        });
    }

    @Test
    public void shouldStartTask() throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskResponse updatedTaskResponse = startTaskAndWaitInView(t, boardResponse.getId(), taskResponse.getId());

            assertTrue(taskListContains(transformToTask(taskResponse.getId(), updatedTaskResponse), getTasksForBoard(token, boardResponse.getId()).getTasks()));
        });
    }

    @Test
    public void shouldScheduleTask() throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskResponse updatedTaskResponse = scheduleTaskAndWaitInView(t, boardResponse.getId(), taskResponse.getId());

            assertTrue(taskListContains(transformToTask(taskResponse.getId(), updatedTaskResponse), getTasksForBoard(token, boardResponse.getId()).getTasks()));
        });
    }

    @Test
    public void shouldCompleteTask() throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            TaskResponse updatedTaskResponse = completeTaskAndWaitInView(t, boardResponse.getId(), taskResponse.getId());

            assertTrue(taskListContains(transformToTask(taskResponse.getId(), updatedTaskResponse), getTasksForBoard(token, boardResponse.getId()).getTasks()));
        });
    }

    @Test
    public void shouldGetTaskHistory()  throws IOException, InterruptedException {
        withToken(t -> {
            TaskInfo taskInfo = generateTaskInfo();

            TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

            BoardInfo boardInfo = generateBoardInfo();
            BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

            startTaskAndWaitInView(t, boardResponse.getId(), taskResponse.getId());
            scheduleTaskAndWaitInView(t, boardResponse.getId(), taskResponse.getId());
            completeTaskAndWaitInView(t, boardResponse.getId(), taskResponse.getId());
            deleteTaskAndWaitInView(t, taskResponse.getId());

            TestHistoryResponse historyResponse = getHistoryForTask(t, taskResponse.getId());
            assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskCreatedEvent");
            assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskStartedEvent");
            assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskScheduledEvent");
            assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskCompletedEvent");
            assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskDeletedEvent");
        });
    }
}