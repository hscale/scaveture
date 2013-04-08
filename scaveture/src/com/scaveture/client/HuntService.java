package com.scaveture.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.scaveture.shared.Hunt;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("hunt")
public interface HuntService extends RemoteService {
	Hunt saveHunt(Hunt h);
	List<Hunt> findHuntsNear(double northLat, double eastLon, double westLon, double southLat);
}
