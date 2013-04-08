package com.scaveture.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.scaveture.shared.Hunt;

/**
 * The async counterpart of <code>HuntService</code>.
 */
public interface HuntServiceAsync {
	void saveHunt(Hunt h, AsyncCallback<Hunt> callback);
	void findHuntsNear(double northLat, double eastLon, double westLon, double southLat, AsyncCallback<List<Hunt>> callback);
}
