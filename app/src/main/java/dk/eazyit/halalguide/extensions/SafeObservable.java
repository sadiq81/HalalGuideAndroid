package dk.eazyit.halalguide.extensions;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Privat on 30/12/2015.
 */
public class SafeObservable<T> extends Observable<T> {

    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p/>
     * <em>Note:</em> Use {@link #create(rx.Observable.OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f {@link rx.Observable.OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected SafeObservable(OnSubscribe<T> f) {
        super(f);
    }

    public Subscription safeSubscribe(Subscriber<T> subscriber) {
        WeakSubscriberDecorator<T> weakSubscriberDecorator = new WeakSubscriberDecorator<>(subscriber);
        return subscribe(weakSubscriberDecorator);
    }
}
