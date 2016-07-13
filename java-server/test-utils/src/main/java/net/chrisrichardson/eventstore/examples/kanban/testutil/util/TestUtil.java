package net.chrisrichardson.eventstore.examples.kanban.testutil.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TestUtil {

    public static <T> T awaitPredicatePasses(Func1<Long, T> func, Func1<T, Boolean> predicate) {
        try {
            return Observable.interval(400, TimeUnit.MILLISECONDS)
                    .take(50)
                    .map(func)
                    .filter(predicate)
                    .toBlocking().first();

        } catch (Exception e) {
            // Rx Java throws an exception with a stack trace from a different thread
            //  https://github.com/ReactiveX/RxJava/issues/3558
            throw new RuntimeException(e);
        }
    }

    public static String awaitSuccessfulRequest(Supplier<HttpResponse> func) {
        try {
            return EntityUtils.toString(Observable.interval(400, TimeUnit.MILLISECONDS)
                    .take(50)
                    .map(x -> func.get())
                    .filter(httpResp -> httpResp.getStatusLine().getStatusCode() == HttpStatus.OK.value() && httpResp.getEntity() != null)
                    .toBlocking().first().getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
