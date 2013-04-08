package com.scaveture.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.scaveture.shared.Hunt;
import com.scaveture.shared.Submission;

public class SubmissionServlet extends HttpServlet {
    private static final long serialVersionUID = 7064195749826767486L;
    private static final Logger LOG = Logger.getLogger(SubmissionServlet.class.getName());
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        PersistenceManager pm = null;
        try {
            res.setStatus(599); // custom failure status
            LOG.severe("Attempting to create new Submission in doPost...");
            pm = DAO.getPersistenceManager();
            
            String latitude = req.getParameter("lat");
            LOG.severe("Got latitude == " + latitude);
            String longitude = req.getParameter("long");
            LOG.severe("Got longitude == " + longitude);
            String huntId = req.getParameter("hunt");
            LOG.severe("Got hunt ID == " + huntId);
            Hunt hunt = pm.getObjectById(Hunt.class, Long.parseLong(huntId));
            LOG.severe((hunt != null)? "Found Hunt" : "Didn't find Hunt");
            Blob image = new Blob(IOUtils.toByteArray(req.getInputStream()));
            LOG.severe("Created blob with " + image.getBytes().length + " bytes");
    
            Picture picture = new Picture();
            picture.setImage(image);
            picture.setType(req.getContentType());
            picture = pm.makePersistent(picture);
            LOG.severe("Created Picture with ID == " + picture.getId() + " and content type == " + picture.getType());
            Submission submission = new Submission();
            submission.setPictureId(picture.getId());
            submission.setLatitude(Double.parseDouble(latitude));
            submission.setLongitude(Double.parseDouble(longitude));
            submission.setHunt(hunt);
            hunt.getSubmissions().add(submission);
    
            // respond to query
            res.setContentType("text/plain");
            res.setStatus(299); // custom success status
        }
        catch(Throwable ex) {
            LOG.log(Level.SEVERE, "Exception in doPost: ", ex);
        }
        finally {
            if(pm != null) {
                pm.close();
            }
        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PersistenceManager pm = null;
        try {
            pm = DAO.getPersistenceManager();
            String huntId = request.getParameter("hid");
            LOG.severe("Got hunt ID == " + huntId);
            String submissionId = request.getParameter("sid");
            LOG.severe("Got submission ID == " + submissionId);
            
            // Actual child key contains parent info. Long story.
            Key parentKey = KeyFactory.createKey(Hunt.class.getSimpleName(), Long.parseLong(huntId));
            Key realChildKey = KeyFactory.createKey(parentKey, Submission.class.getSimpleName(), Long.parseLong(submissionId)); 
            
            Submission submission = pm.getObjectById(Submission.class, realChildKey);
            LOG.severe((submission != null)? "Found Submission" : "Didn't find Submission");
            
            Picture picture = pm.getObjectById(Picture.class, submission.getPictureId());
            // Set the appropriate Content-Type header and write the raw bytes to the response's output stream
            response.setContentType(picture.getType());
            response.getOutputStream().write(picture.getImage().getBytes());
        }
        catch(Throwable ex) {
            LOG.log(Level.SEVERE, "Exception in doGet: ", ex);
        }
        finally {
            if(pm != null) {
                pm.close();
            }
        }
    }

}
