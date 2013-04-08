package com.scaveture.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Picture implements Serializable {
	private static final long serialVersionUID = 1353975490235582651L;

	public Picture() {}
	
	public Picture(Picture that) {
		this.copiedId = that.id.getId();
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;

    @Persistent
    private Blob image;

    @Persistent
    private String type;
    
    private Long copiedId = null;
	
	public long getId() {
		if(copiedId != null) {
			return copiedId;
		}
		return id.getId();
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Blob getImage() {
		return image;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
