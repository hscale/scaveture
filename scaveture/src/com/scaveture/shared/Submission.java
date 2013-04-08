package com.scaveture.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Submission implements Serializable {
	private static final long serialVersionUID = -4888313117754730924L;

	public Submission() {}
	
	public Submission(Submission that) {
		this.copiedId = that.id.getId();
		this.pictureId = that.pictureId;
		this.latitude = that.latitude;
		this.longitude = that.longitude;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;

    @Persistent
    private long pictureId;
    
    private Long copiedId = null;

	@Persistent
	private double latitude;

	@Persistent
	private double longitude;

	@Persistent
	private Hunt hunt;
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setHunt(Hunt hunt) {
		this.hunt = hunt;
	}

	public Hunt getHunt() {
		return hunt;
	}
	
	public long getId() {
		if(copiedId != null) {
			return copiedId;
		}
		return id.getId();
	}

	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}

	public long getPictureId() {
		return pictureId;
	}
}
