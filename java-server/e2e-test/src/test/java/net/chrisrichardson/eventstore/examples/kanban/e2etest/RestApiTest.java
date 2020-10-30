package net.chrisrichardson.eventstore.examples.kanban.e2etest;

import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.webapi.BoardResponse;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi.BoardQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi.BoardsQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskDescription;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.webapi.BacklogResponse;
import net.chrisrichardson.eventstore.examples.kanban.testutil.BaseTest;
import net.chrisrichardson.eventstore.examples.kanban.testutil.model.TestHistoryResponse;
import net.chrisrichardson.eventstore.examples.kanban.webapi.TaskResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = E2ETestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RestApiTest extends BaseTest {

  @Test
  public void shouldGetBoardById() throws IOException {
    withToken(t -> {
      BoardInfo boardInfo = generateBoardInfo();
      BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

      BoardQueryResponse savedBoardResponse = getBoard(t, boardResponse.getId());

      assertBoardInfoEquals(boardInfo, transformToBoardInfo(savedBoardResponse.getData()));
    });
  }

  @Test
  public void shouldGetAllBoards() throws IOException {
    withToken(t -> {
      BoardInfo boardInfo = generateBoardInfo();
      BoardResponse boardResponse = createBoardAndWaitInView(t, boardInfo);

      BoardsQueryResponse boardsResponse = getAllBoards(t);

      assertBoardContains(transformToBoard(boardResponse.getId(), boardResponse), boardsResponse.getBoards());
    });
  }

  @Test
  public void shouldGetBacklogTask() throws IOException {
    withToken(t -> {
      TaskInfo taskInfo = generateTaskInfo();

      TaskResponse taskResponse = createTaskAndWaitInView(t, taskInfo);

      assertBackLogContains(t, taskResponse.getId(), taskResponse);
    });
  }

  @Test
  public void shouldDeleteTask() throws IOException {
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
  public void shouldUpdateTask() throws IOException {
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
  public void shouldStartTask() throws IOException {
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
  public void shouldScheduleTask() throws IOException {
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
  public void shouldCompleteTask() throws IOException {
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
  public void shouldGetTaskHistory() throws IOException {
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
      assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskCreatedEvent");
      assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskStartedEvent");
      assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskScheduledEvent");
      assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskCompletedEvent");
      assertTaskHistoryContainsEvent(historyResponse.getData(), "net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskDeletedEvent");
    });
  }
}