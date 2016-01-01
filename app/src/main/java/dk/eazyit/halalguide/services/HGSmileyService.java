package dk.eazyit.halalguide.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.eazyit.halalguide.domain.HGSmiley;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Privat on 20/08/15.
 */
public class HGSmileyService {

    private static HGSmileyService instance = null;

    protected HGSmileyService() {
    }

    public static HGSmileyService getInstance() {
        if (instance == null) {
            instance = new HGSmileyService();
        }
        return instance;
    }


    public Observable<List<HGSmiley>> getSmileysObservable(final List<String> nameLoebenumre) {
        return Observable.create(new Observable.OnSubscribe<List<HGSmiley>>() {
            @Override
            public void call(Subscriber<? super List<HGSmiley>> subscriber) {


                List<HGSmiley> smileys = new ArrayList<>();

                if (nameLoebenumre == null) {
                    subscriber.onCompleted();
                }

                try {
                    for (int i = 0; i < nameLoebenumre.size(); i++) {
                        Document doc = Jsoup.connect("http://www.findsmiley.dk/da-DK/Searching/DetailsView.htm?virk=" + nameLoebenumre.get(i)).get();
                        Elements smileyElements = doc.select("a.link_pdf");
                        for (int z = 0; z < smileyElements.size(); z++) {
                            Element smileyElement = smileyElements.get(z);
                            String report = smileyElement.attr("href");
                            String image = smileyElement.childNode(0).attr("src");
                            String smileyType = "http://www.findsmiley.dk" + image;
                            String date = smileyElement.childNode(2).toString();
                            HGSmiley smiley = new HGSmiley(report, smileyType, date);
                            smileys.add(smiley);
                        }
                    }
                } catch (IOException e) {
                    //TODO Logging
                    subscriber.onError(e);
                }

                subscriber.onNext(smileys);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}