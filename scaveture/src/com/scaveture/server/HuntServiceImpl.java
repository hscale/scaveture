package com.scaveture.server;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.BoundingBox;
import com.beoui.geocell.model.Point;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.scaveture.client.HuntService;
import com.scaveture.shared.Hunt;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class HuntServiceImpl extends RemoteServiceServlet implements HuntService {
	private static final Logger LOG = Logger.getLogger(HuntServiceImpl.class.getName());
	
	private void computeAndAddGeoboxes(Hunt h) {
		// just re-compute all
		Point p = new Point(h.getLatitude(), h.getLongitude());
        List<String> cells = GeocellManager.generateGeoCell(p);
        h.setGeocells(cells);
	}

	@Override
	public Hunt saveHunt(Hunt h) { // should return hunt not boolean
		try {
			computeAndAddGeoboxes(h);
			Hunt saved = DAO.mergeTransient(h, DAO.getPersistenceManager());
			return new Hunt(saved);
		}
		catch(Throwable ex) {
			LOG.log(Level.SEVERE, "Exception in saveHunt: ", ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hunt> findHuntsNear(double northLat, double eastLon, double westLon, double southLat) {
		try {
			PersistenceManager pm = DAO.getPersistenceManager();
			BoundingBox bb = new BoundingBox(northLat, eastLon, southLat, westLon);
			List<String> cells = GeocellManager.bestBboxSearchCells(bb, null);
			String queryString = "select from " + Hunt.class.getName() + " where geocellsParameter.contains(geocells)";
			Query query = pm.newQuery(queryString);
			query.declareParameters("String geocellsParameter");
			List<Hunt> persistedResults = (List<Hunt>)query.execute(cells);
			List<Hunt> detachedResults = new LinkedList<Hunt>();
			for(Hunt h : persistedResults) {
				h.getSubmissions(); // load the submissions
				// create a detached copy that GWT can deserialize
				Hunt temp = new Hunt(h);
				detachedResults.add(temp);
			}
			return detachedResults;
		}
		catch(Throwable ex) {
			LOG.log(Level.SEVERE, "Exception in findHuntsNear: ", ex);
		}
		return null;
	}
}
