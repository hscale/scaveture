package com.scaveture.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Hunt implements Serializable {
	private static final long serialVersionUID = -5785592130083747837L;
	
	public Hunt() {}
	
	public Hunt(Hunt that) {
		this.copiedId = that.id.getId();
		this.description = that.description;
		this.latitude = that.latitude;
		this.longitude = that.longitude;
		
		for(Submission s : that.getSubmissions()) {
			// create a detached copy that GWT can deserialize
			Submission submission = new Submission(s);
			submission.setHunt(this);
			getSubmissions().add(submission);
		}
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;

	@Persistent
	private String description;

	@Persistent
	private double latitude;

	@Persistent
	private double longitude;
	
    @Persistent
    private List<String> geocells;
	
    @Persistent(mappedBy = "hunt")
    private List<Submission> submissions;
    
    private Long copiedId = null;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

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

	public void setGeocells(List<String> geocells) {
		this.geocells = geocells;
	}

	public List<String> getGeocells() {
		return geocells;
	}
	
	public void setSubmissions(List<Submission> submissions) {
		this.submissions = submissions;
	}

	public List<Submission> getSubmissions() {
		if(submissions == null) {
			submissions = new ArrayList<Submission>();
		}
		return submissions;
	}

	public long getId() {
		if(copiedId != null) {
			return copiedId;
		}
		return id.getId();
	}
}
