package dk.eazyit.halalguide.domain;

/**
 * Created by Privat on 08/07/15.
 */
public enum HGCreationStatus {

    AwaitingApproval(0),
    Approved(1),
    NotApproved(2);

    private Number id;

    HGCreationStatus(Number id) {
        this.id = id;
    }

    public Number getId() {
        return id;
    }


}
