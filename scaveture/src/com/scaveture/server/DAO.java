package com.scaveture.server;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

public class DAO {
	private static final Logger LOG = Logger.getLogger(DAO.class.getName());
	private static PersistenceManagerFactory PMF = null;
	
	public static synchronized PersistenceManager getPersistenceManager() {
		if(PMF == null) {
			PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		}
		return PMF.getPersistenceManager();
	}
	
	public static <T> Object findPrimaryKey(T tInstance) {
		if (tInstance == null) {
			return null;
		}
		for (Field l : tInstance.getClass().getDeclaredFields()) {
			if (l.getAnnotation(PrimaryKey.class) != null
					|| l.getAnnotation(Id.class) != null) {
				l.setAccessible(true);
				try {
					return l.get(tInstance);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return new IllegalArgumentException("Class "
				+ tInstance.getClass().getName()
				+ " does not have a method called getId()");
	}

	private static <T> void copyPersistentFields(Object entity, T tInstance) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		for (Method f : tInstance.getClass().getMethods()) {
			if (f.getName().startsWith("set")
					&& Character.isUpperCase(f.getName().charAt(3))) {
				f.setAccessible(true);
				Method getter = tInstance.getClass().getMethod(
						"get" + f.getName().substring(3));
				getter.setAccessible(true);
				Object value = getter.invoke(tInstance);
				if(value instanceof String) {
					value = escapeHtml((String)value);
				}
				f.invoke(entity, value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T mergeTransient(T tInstance, PersistenceManager pm) {
		Object primaryKey = DAO.findPrimaryKey(tInstance);
		if (primaryKey != null) {
			Object entity = pm.getObjectById(tInstance.getClass(), primaryKey);
			if (entity == null) {
				pm.makePersistent(tInstance);
				return tInstance;
			} 
			else {
				try {
					copyPersistentFields(entity, tInstance);
				} 
				catch (IllegalAccessException e1) {
					LOG.log(Level.SEVERE, "IllegalAccessException in mergeTransient: ", e1);
					throw new IllegalArgumentException("Can't copy fields from transient class to persistent class.");
				} 
				catch (NoSuchMethodException e1) {
					LOG.log(Level.SEVERE, "NoSuchMethodException in mergeTransient: ", e1);
					throw new IllegalArgumentException("Can't copy fields from transient class to persistent class.");
				} 
				catch (InvocationTargetException e1) {
					LOG.log(Level.SEVERE, "InvocationTargetException in mergeTransient: ", e1);
					throw new IllegalArgumentException("Can't copy fields from transient class to persistent class.");
				}
				pm.makePersistent(entity);
				return (T)entity;
			}
		} else {
			// primary key may be null, assume insert
			pm.makePersistent(tInstance);
			return tInstance;
		}
	}

	private static String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
