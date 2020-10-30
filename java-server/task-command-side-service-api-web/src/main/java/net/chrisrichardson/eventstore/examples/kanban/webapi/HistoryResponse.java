package net.chrisrichardson.eventstore.examples.kanban.webapi;

import java.util.List;

public class HistoryResponse {

  private List<HistoryEvent> data;

  public HistoryResponse() {
  }

  public HistoryResponse(List<HistoryEvent> data) {
    this.data = data;
  }

  public List<HistoryEvent> getData() {
    return data;
  }

  public void setData(List<HistoryEvent> data) {
    this.data = data;
  }
}
