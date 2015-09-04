package it.twopay.entity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logic {
	public static Logger log = LoggerFactory.getLogger("it.twopay.entity.Logic");
	
	private String name;
	private String state;
	private List<String> actions;
	
	public Logic(String name) {
		this.name = name;
		actions = new ArrayList<>();
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<String> getActions() {
		return actions;
	}
	public void addAction(String action) {
		actions.add(action);
	}
}
