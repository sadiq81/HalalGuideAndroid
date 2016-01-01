package dk.eazyit.halalguide.ui.common.models;

import com.parse.ParseException;

import rx.subjects.BehaviorSubject;

/**
 * Created by Privat on 08/07/15.
 */
public abstract class HGBaseViewModel {

    public BehaviorSubject<Integer> fetchCountSubject = BehaviorSubject.create();
    public BehaviorSubject<ParseException> exceptionSubject = BehaviorSubject.create();
    protected Integer fetchCount = 0;
    protected ParseException exception = null;

    protected void increaseCount() {
        fetchCount++;
        fetchCountSubject.onNext(fetchCount);
    }

    protected void increaseCount(int increase) {
        fetchCount += increase;
        fetchCountSubject.onNext(fetchCount);
    }

    protected void decreaseCount() {
        fetchCount--;
        fetchCountSubject.onNext(fetchCount);
    }

}
