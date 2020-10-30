package net.chrisrichardson.eventstore.examples.kanban.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.webapi.BoardResponse;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi.BoardQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi.BoardsQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.Board;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskDescription;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.webapi.AuthRequest;
import net.chrisrichardson.eventstore.examples.kanban.webapi.ChangeTaskStatusRequest;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.Task;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.model.AuthResponse;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.KanbanWebSocketEvent;
import net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.webapi.BacklogResponse;
import net.chrisrichardson.eventstore.examples.kanban.testutil.model.TestHistoryEvent;
import net.chrisrichardson.eventstore.examples.kanban.testutil.model.TestHistoryResponse;
import net.chrisrichardson.eventstore.examples.kanban.testutil.util.ValidationUtils;
import net.chrisrichardson.eventstore.examples.kanban.webapi.TaskResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.chrisrichardson.eventstore.examples.kanban.testutil.util.TestUtil.awaitPredicatePasses;
import static net.chrisrichardson.eventstore.examples.kanban.testutil.util.TestUtil.awaitSuccessfulRequest;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class BaseTest {

  @Autowired
  protected ApplicationContext applicationContext;

  @Value("${api.gateway.host}")
  protected String host;

  @Value("${api.gateway.port}")
  protected int port;

  protected String token;

  private ObjectMapper mapper = new ObjectMapper();

  protected String baseUrl(String path) {
    return "http://" + host + ":" + port + "/" + path;
  }

  protected String boardsUrl() {
    return baseUrl("api/boards");
  }

  protected String boardUrl(String id) {
    return baseUrl("api/boards/" + id);
  }

  protected String tasksUrl() {
    return baseUrl("api/tasks");
  }

  protected String tasksUrl(String boardId) {
    return baseUrl("api/tasks?boardId=" + boardId);
  }

  protected String taskUrl(String taskId) {
    return baseUrl("api/tasks/" + taskId);
  }

  protected String taskHistoryUrl(String taskId) {
    return baseUrl("api/tasks/" + taskId + "/history");
  }

  protected String startTaskUrl(String taskId) {
    return baseUrl("api/tasks/" + taskId + "/start");
  }

  protected String scheduleTaskUrl(String taskId) {
    return baseUrl("api/tasks/" + taskId + "/schedule");
  }

  protected String completeTaskUrl(String taskId) {
    return baseUrl("api/tasks/" + taskId + "/complete");
  }

  protected String backlogTaskUrl(String taskId) {
    return baseUrl("api/tasks/" + taskId + "/backlog");
  }

  protected String getWSJsonSchemaPath(String typeName) {
    return "classpath:schemas/websocket-events-schema/" + typeName + ".json";
  }

  public void withToken(Consumer<String> func) throws IOException {
    if (token == null) {
      AuthRequest authRequest = new AuthRequest();
      authRequest.setEmail("test@test.com");
      HttpResponse authHttpResp = Request.Post(baseUrl("api/authenticate"))
              .bodyString(mapper.writeValueAsString(authRequest), ContentType.APPLICATION_JSON)
              .execute().returnResponse();

      Assert.assertEquals(HttpStatus.OK.value(), authHttpResp.getStatusLine().getStatusCode());
      String content = EntityUtils.toString(authHttpResp.getEntity());
      assertNotNull(content);
      AuthResponse authResponse = mapper.readValue(content, AuthResponse.class);
      Assert.assertFalse(authResponse.getToken().isEmpty());
      token = authResponse.getToken();
    }
    func.accept(token);
  }

  protected void awaitTaskCreationInView(String token, String taskId) {
    awaitPredicatePasses(idx -> getBacklogTasks(token),
            (bqr) -> taskListContainsTaskWithId(taskId, bqr.getBacklog()));
  }

  protected void awaitTaskDeletionInView(String token, String taskId) {
    awaitPredicatePasses(idx -> getBacklogTasks(token),
            (bqr) -> !taskListContainsTaskWithId(taskId, bqr.getBacklog()));
  }

  protected BoardInfo transformToBoardInfo(Board board) {
    return new BoardInfo(board.getTitle(),
            new AuditEntry(board.getCreatedBy(), board.getCreatedDate()),
            new AuditEntry(board.getUpdatedBy(), board.getUpdatedDate()));

  }

  protected BoardInfo transformToBoardInfo(BoardResponse boardResponse) {
    return new BoardInfo(boardResponse.getTitle(),
            new AuditEntry(boardResponse.getCreatedBy(), boardResponse.getCreatedDate()),
            new AuditEntry(boardResponse.getUpdatedBy(), boardResponse.getUpdatedDate()));

  }

  protected Board transformToBoard(String id, BoardResponse boardResponse) {
    return new Board(id, boardResponse.getTitle(),
            boardResponse.getCreatedBy(),
            boardResponse.getCreatedDate(),
            boardResponse.getUpdatedDate(),
            boardResponse.getUpdatedBy());

  }

  protected Task transformToTask(String id, TaskResponse taskResponse) {
    return new Task(id,
            taskResponse.getBoardId(),
            taskResponse.getTitle(),
            taskResponse.getCreatedBy(),
            taskResponse.getUpdatedBy(),
            taskResponse.getCreatedDate(),
            taskResponse.getUpdatedDate(),
            taskResponse.getStatus(),
            taskResponse.isDeleted(),
            taskResponse.getDescription() != null ?
                    taskResponse.getDescription().getDescription() : null);
  }

  protected TaskInfo transformToTaskInfo(TaskResponse taskResponse) {
    return new TaskInfo(taskResponse.getBoardId(),
            new TaskDetails(taskResponse.getTitle(), taskResponse.getDescription()),
            new AuditEntry(taskResponse.getCreatedBy(), taskResponse.getCreatedDate()),
            new AuditEntry(taskResponse.getUpdatedBy(), taskResponse.getUpdatedDate()),
            taskResponse.getStatus(),
            taskResponse.isDeleted());
  }

  protected TaskInfo transformToTaskInfo(Task task) {
    return new TaskInfo(task.getBoardId(),
            new TaskDetails(task.getTitle(), new TaskDescription(task.getDescription())),
            new AuditEntry(task.getCreatedBy(), task.getCreatedDate()),
            new AuditEntry(task.getUpdatedBy(), task.getUpdatedDate()),
            task.getStatus(),
            task.isDeleted());
  }

  protected TaskInfo generateTaskInfo() {
    return new TaskInfo("1",
            new TaskDetails("big task", new TaskDescription("data")),
            new AuditEntry("doctor@who.me", new Date()),
            new AuditEntry("doctor@who.me", new Date()),
            TaskStatus.backlog,
            false);
  }

  protected BoardInfo generateBoardInfo() {
    return new BoardInfo("big project",
            new AuditEntry("doctor@who.me", new Date()),
            new AuditEntry("doctor@who.me", new Date()));
  }

  protected void validateWSMessage(String eventType, String jsonText) {
    String eventName = StringUtils.substringAfterLast(eventType, ".");
    assertNotNull(eventName);
    try {
      Resource schemaFileResource = applicationContext.getResource(getWSJsonSchemaPath(eventName));

      String jsonSchema = new BufferedReader(new InputStreamReader(schemaFileResource.getInputStream())).lines().collect(Collectors.joining("\n"));

      assertTrue(ValidationUtils.isJsonValid(ValidationUtils.getSchemaNode(jsonSchema), ValidationUtils.getJsonNode(jsonText)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected KanbanWebSocketEvent filterWSEvents(List<KanbanWebSocketEvent> webSocketEvents, String eventType) {
    Optional<KanbanWebSocketEvent> eventOptional = webSocketEvents.stream().filter(kwse -> kwse.getEventType().contains(eventType)).findFirst();
    assertTrue(eventOptional.isPresent());
    return eventOptional.get();
  }

  protected boolean wsEventsContainsEvent(List<KanbanWebSocketEvent> webSocketEvents, String eventType) {
    return webSocketEvents.stream().anyMatch(kwse -> kwse.getEventType().contains(eventType));
  }

  protected boolean taskListContainsTaskWithId(String taskId, List<Task> tasks) {
    return tasks.stream().anyMatch(task -> task.getId().equals(taskId));
  }

  protected boolean taskListContains(Task expectedTask, List<Task> tasks) {
    return tasks.contains(expectedTask);
  }

  protected void assertBackLogContains(String token, String taskId, TaskResponse taskResponse) {
    BacklogResponse backlogResponse = getBacklogTasks(token);

    assertTrue(taskListContains(transformToTask(taskId, taskResponse),
            backlogResponse.getBacklog()));
  }

  protected void assertTaskNotIn(Task expectedTask, List<Task> tasks) {
    Assert.assertFalse(tasks.contains(expectedTask));
  }

  protected void assertBoardContains(Board expectedBoard, List<Board> boards) {
    assertTrue(boards.contains(expectedBoard));
  }

  protected void assertTaskInfoEquals(TaskInfo expectedTaskInfo, TaskInfo taskInfo) {
    Assert.assertEquals(expectedTaskInfo.getTaskDetails(), taskInfo.getTaskDetails());
    Assert.assertEquals(expectedTaskInfo.getCreation(), taskInfo.getCreation());
    Assert.assertEquals(expectedTaskInfo.getUpdate(), taskInfo.getUpdate());
    Assert.assertEquals(expectedTaskInfo.getStatus(), taskInfo.getStatus());
    Assert.assertEquals(expectedTaskInfo.isDeleted(), taskInfo.isDeleted());
  }

  protected void assertBoardInfoEquals(BoardInfo expectedBoardInfo, BoardInfo boardInfo) {
    Assert.assertEquals(expectedBoardInfo.getTitle(), boardInfo.getTitle());
    Assert.assertEquals(expectedBoardInfo.getCreation(), boardInfo.getCreation());
    Assert.assertEquals(expectedBoardInfo.getUpdate(), boardInfo.getUpdate());
  }

  protected void assertTaskHistoryContainsEvent(List<TestHistoryEvent> historyEvents, String eventName) {
    assertTrue(historyEvents.stream().filter(he -> he.getEventType().equals(eventName)).findFirst().isPresent());
  }

  protected BoardQueryResponse getBoard(String token, String boardId) {
    try {
      HttpResponse httpResp = Request.Get(boardUrl(boardId))
              .addHeader("x-access-token", token)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());
      Assert.assertFalse(responseContent.isEmpty());
      return mapper.readValue(responseContent, BoardQueryResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected BoardsQueryResponse getAllBoards(String token) {
    try {
      HttpResponse httpResp = Request.Get(boardsUrl())
              .addHeader("x-access-token", token)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());
      Assert.assertFalse(responseContent.isEmpty());
      return mapper.readValue(responseContent, BoardsQueryResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected BacklogResponse getBacklogTasks(String token) {
    try {
      HttpResponse httpResp = Request.Get(tasksUrl())
              .addHeader("x-access-token", token)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());
      Assert.assertFalse(responseContent.isEmpty());
      return mapper.readValue(responseContent, BacklogResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected BacklogResponse getTasksForBoard(String token, String boardId) {
    try {
      HttpResponse httpResp = Request.Get(tasksUrl(boardId))
              .addHeader("x-access-token", token)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());
      Assert.assertFalse(responseContent.isEmpty());
      return mapper.readValue(responseContent, BacklogResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected TestHistoryResponse getHistoryForTask(String token, String taskId) {
    try {
      HttpResponse httpResp = Request.Get(taskHistoryUrl(taskId))
              .addHeader("x-access-token", token)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());
      Assert.assertFalse(responseContent.isEmpty());
      return mapper.readValue(responseContent, TestHistoryResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected TaskResponse createTaskAndWaitInView(String token, TaskInfo taskInfo) {
    try {
      HttpResponse httpResp = Request.Post(tasksUrl())
              .addHeader("x-access-token", token)
              .bodyString(mapper.writeValueAsString(taskInfo), ContentType.APPLICATION_JSON)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());

      Assert.assertFalse(responseContent.isEmpty());
      TaskResponse resp = mapper.readValue(responseContent, TaskResponse.class);

      awaitTaskCreationInView(token, resp.getId());

      return resp;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected TaskResponse deleteTaskAndWaitInView(String token, String taskId) {
    try {
      HttpResponse httpResp = Request.Delete(taskUrl(taskId))
              .addHeader("x-access-token", token)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());

      Assert.assertFalse(responseContent.isEmpty());
      TaskResponse resp = mapper.readValue(responseContent, TaskResponse.class);

      awaitTaskDeletionInView(token, resp.getId());

      return resp;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected TaskResponse updateTaskAndWaitInView(String token, String taskId, TaskDetails request) {
    try {
      HttpResponse httpResp = Request.Put(taskUrl(taskId))
              .addHeader("x-access-token", token)
              .bodyString(mapper.writeValueAsString(request), ContentType.APPLICATION_JSON)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());

      Assert.assertFalse(responseContent.isEmpty());
      TaskResponse resp = mapper.readValue(responseContent, TaskResponse.class);

      awaitTaskCreationInView(token, resp.getId());
      return mapper.readValue(responseContent, TaskResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected TaskResponse changeTaskState(String token, String url, ChangeTaskStatusRequest request) {
    try {
      HttpResponse httpResp = Request.Put(url)
              .addHeader("x-access-token", token)
              .bodyString(mapper.writeValueAsString(request), ContentType.APPLICATION_JSON)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());

      Assert.assertFalse(responseContent.isEmpty());
      return mapper.readValue(responseContent, TaskResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected TaskResponse startTaskAndWaitInView(String token, String boardId, String taskId) {
    TaskResponse updatedTaskResponse = changeTaskState(token, startTaskUrl(taskId), new ChangeTaskStatusRequest(boardId));

    awaitPredicatePasses(idx -> getTasksForBoard(token, boardId),
            (bqr) -> taskListContains(transformToTask(taskId, updatedTaskResponse), bqr.getTasks()));
    return updatedTaskResponse;
  }

  protected TaskResponse scheduleTaskAndWaitInView(String token, String boardId, String taskId) {
    TaskResponse updatedTaskResponse = changeTaskState(token, scheduleTaskUrl(taskId), new ChangeTaskStatusRequest(boardId));

    awaitPredicatePasses(idx -> getTasksForBoard(token, boardId),
            (bqr) -> taskListContains(transformToTask(taskId, updatedTaskResponse), bqr.getTasks()));
    return updatedTaskResponse;
  }

  protected TaskResponse completeTaskAndWaitInView(String token, String boardId, String taskId) {
    TaskResponse updatedTaskResponse = changeTaskState(token, completeTaskUrl(taskId), new ChangeTaskStatusRequest(boardId));

    awaitPredicatePasses(idx -> getTasksForBoard(token, boardId),
            (bqr) -> taskListContains(transformToTask(taskId, updatedTaskResponse), bqr.getTasks()));
    return updatedTaskResponse;
  }

  protected BoardResponse createBoardAndWaitInView(String token, BoardInfo boardInfo) {
    try {
      HttpResponse httpResp = Request.Post(boardsUrl())
              .addHeader("x-access-token", token)
              .bodyString(mapper.writeValueAsString(boardInfo), ContentType.APPLICATION_JSON)
              .execute().returnResponse();
      Assert.assertEquals(HttpStatus.OK.value(), httpResp.getStatusLine().getStatusCode());
      String responseContent = EntityUtils.toString(httpResp.getEntity());
      Assert.assertFalse(responseContent.isEmpty());
      BoardResponse resp = mapper.readValue(responseContent, BoardResponse.class);

      awaitBoardCreationInView(token, resp.getId());

      return resp;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void awaitBoardCreationInView(String token, String boardId) {
    awaitSuccessfulRequest(() -> {
      try {
        return Request.Get(boardUrl(boardId))
                .addHeader("x-access-token", token)
                .execute().returnResponse();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}