package net.chrisrichardson.eventstore.examples.kanban.testutil.model;

import java.util.List;

public class TestHistoryResponse {

    private List<TestHistoryEvent> data;

    public TestHistoryResponse() {
    }

    public TestHistoryResponse(List<TestHistoryEvent> data) {
        this.data = data;
    }

    public List<TestHistoryEvent> getData() {
        return data;
    }

    public void setData(List<TestHistoryEvent> data) {
        this.data = data;
    }
}
