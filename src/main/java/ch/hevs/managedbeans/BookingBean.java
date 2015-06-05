package ch.hevs.managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;


import javax.faces.event.ValueChangeEvent;

import ch.hevs.businessobject.Account;
import ch.hevs.businessobject.Flight;
import ch.hevs.businessobject.Gender;
import ch.hevs.businessobject.Paraglider;
import ch.hevs.businessobject.Pilot;
import ch.hevs.businessobject.Plane;
import ch.hevs.businessobject.Site;
import ch.hevs.businessobject.SiteType;
import ch.hevs.businessobject.WingApproval;
import ch.hevs.clubservice.Club;

/**
 * TransferBean.java
 * 
 */

public class BookingBean
{
	private List<String> departureNameList;
	private String departureName;
	private List<String> arrivalNameList;
	private String arrivalName;
	
	private long planeId;
	
	private List<String> planesNameList;	
	private List<Plane> planes;
	
	private List<Flight> incomingFlights;
	
	@EJB(name = "ClubBean") 
	private Club club;
	
	@PostConstruct
    public void initialize() {
 		
		// create departure sites name
		List<Site> departureSites = club.getDepartureSites();
		departureNameList = new ArrayList<String>();
	
		for (Site d : departureSites) {
			departureNameList.add(d.getName());
		}	
		
		// create arrival sites name
		List<Site> arrivalSites = club.getArrivalSites();
		arrivalNameList = new ArrayList<String>();
	
		for (Site a : arrivalSites) {
			arrivalNameList.add(a.getName());
		}	
		
		// create arrival sites name
		planes = club.getAll();
		planesNameList = new ArrayList<String>();
	
		for (Plane p : planes) {
			planesNameList.add(p.getClass().getCanonicalName());
		}	
		
		// get incoming flights
		incomingFlights = club.getIncomingFlights();
    }
	
	public List<Plane> getPlanes() {
		return planes;
	}
	
	public void setPlanes(List<Plane> planes) {
		this.planes = planes;
	}

	public List<String> getDepartureNameList() {
		return departureNameList;
	}

	public void setDepartureNameList(List<String> departureNameList) {
		this.departureNameList = departureNameList;
	}

	public List<String> getArrivalNameList() {
		return arrivalNameList;
	}

	public void setArrivalNameList(List<String> arrivalNameList) {
		this.arrivalNameList = arrivalNameList;
	}

	public List<String> getPlanesNameList() {
		return planesNameList;
	}

	public void setPlanesNameList(List<String> planesNameList) {
		this.planesNameList = planesNameList;
	}
   
	/**
	 * Set selected value on list change
	 * 
	 * @param e
	 */
	public void departureChanged(ValueChangeEvent e) {
    	departureName = e.getNewValue().toString();
    }

	public String getDepartureName() {
		return this.departureName;
	}
	
	public void setDepartureName(String departureName) {
		this.departureName = departureName;
	}
	

	/**
	 * Set selected value on list change
	 * 
	 * @param e
	 */
	public void arrivalChanged(ValueChangeEvent e) {
    	arrivalName = e.getNewValue().toString();
    }

	public String getArrivalName() {
		return this.arrivalName;
	}
	
	public void setArrivalName(String arrivalName) {
		this.arrivalName = arrivalName;
	}
	
	
	/**
	 * Set selected value on list change
	 * 
	 * @param e
	 */
	public void planeChanged(ValueChangeEvent e) {
		//planeId = (long) e.getNewValue();
		
		planeId = Long.valueOf(e.getNewValue().toString()).longValue();
    }

	public double getPlaneId() {
		return this.planeId;
	}
	
	public void setPlaneId(long planeId) {
		this.planeId = planeId;
	}
	
	
	
	public String performBooking() {
    	
		
    	try {
			if (departureName.isEmpty() || arrivalName.isEmpty()) {
				System.out.print("Arrival or departure are empty");
			} else if(departureName.equals(arrivalName)) {
				System.out.print("Departure and arrival are identical !");
			} else {
				
				// get arrival and departure Sites by dropdown list value
				Site departure = club.getDepartureSiteByName(departureName);
				Site arrival = club.getArrivalSiteByName(arrivalName);
				
				Plane plane = club.getById(planeId);
			
				Pilot pilot = new Pilot("Vincent", "Huck", Gender.MALE, "2386330");
				
				Date date = new Date();
				
				
				// Save the new flight
				Flight f = club.bookFlight(departure, arrival, plane, pilot, date);	
				
				// notify flight list that a new was saved
				incomingFlights.add(f);
				incomingFlights.notifyAll();
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

		return "showTransferResult";
	}



	public List<Flight> getIncomingFlights() {
		return incomingFlights;
	}

	public void setIncomingFlights(List<Flight> incomingFlights) {
		this.incomingFlights = incomingFlights;
	} 
	
}