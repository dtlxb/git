package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Route {
	   private static final long serialVersionUID = 3494746809864002552L;

	    public int rid;
	    public String username;
	    public String name;
	    public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ArrayList<MyLatlng> location_list;
	    public String comment;
	    public Double start_time, end_time;
	    public ArrayList<Integer> pids;

	    public int getRid() {
			return rid;
		}

		public void setRid(int rid) {
			this.rid = rid;
		}

		public ArrayList<MyLatlng> getLocation_list() {
			return location_list;
		}

		public void setLocation_list(ArrayList<MyLatlng> location_list) {
			this.location_list = location_list;
		}

		public Route(){
	        location_list = new ArrayList<MyLatlng>();
	        pids = new ArrayList<Integer>();
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getComment() {
	        return comment;
	    }

	    public void setComment(String comment) {
	        this.comment = comment;
	    }

	    public Double getStart_time() {
	        return start_time;
	    }

	    public void setStart_time(Double start_time) {
	        this.start_time = start_time;
	    }

	    public Double getEnd_time() {
	        return end_time;
	    }

	    public void setEnd_time(Double end_time) {
	        this.end_time = end_time;
	    }

	    public ArrayList<Integer> getPids() {
	        return pids;
	    }

	    public void setPids(ArrayList<Integer> pids) {
	        this.pids = pids;
	    }
	    
	     public static class MyLatlng implements Serializable {
	       

	        public Double latitude;
	        public Double longitude;}
}
