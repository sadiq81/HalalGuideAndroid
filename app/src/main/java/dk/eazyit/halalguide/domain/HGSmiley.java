package dk.eazyit.halalguide.domain;

import java.io.Serializable;

/**
 * Created by Privat on 20/08/15.
 */
public class HGSmiley implements Serializable{

    public String report;
    public String smiley;
    public String date;

    public HGSmiley(String report, String smiley, String date) {
        this.report = report;
        this.smiley = smiley;
        this.date = date;
    }

    @Override
    public String toString() {
        return "HGSmiley{" +
                "report='" + report + '\'' +
                ", smiley='" + smiley + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
