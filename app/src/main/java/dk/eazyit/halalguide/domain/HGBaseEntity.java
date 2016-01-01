package dk.eazyit.halalguide.domain;

import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Privat on 22/10/2015.
 */
public class HGBaseEntity extends ParseObject {

    private Date createdAt;

    private Date updatedAt;

    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt() != null ? super.getCreatedAt() : this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return super.getUpdatedAt() != null ? super.getUpdatedAt() : this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
