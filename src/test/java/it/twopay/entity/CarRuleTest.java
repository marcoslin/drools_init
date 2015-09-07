package it.twopay.entity;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class CarRuleTest {
	
	@Test
	public void testRunning() throws Exception {
		// Perform all action needed to start a car
		Car car = new Car();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("init"));
		
		car.perform(CarCommands.CHECK_MIRROR);
		car.perform(CarCommands.RELEASE_PARKING_BRAKE);
		car.perform(CarCommands.FASTEN_SEATBELT);
		car.perform(CarCommands.START_TRIP);
		
		car.process();
		
		assertThat(car.isRunning(), is(true));
		assertThat(car.getState(), is("running"));
		assertThat(car.getCommands(), hasItem(CarCommands.FILL_UP_TANK));
		assertThat(car.getGear(), is(CarGears.GEAR_6));
	}
	
	@Test
	public void testEndTrip()  throws Exception {
		// Perform all action needed to start, travel and stop the car
		Car car = new Car();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("init"));
		
		car.perform(CarCommands.CHECK_MIRROR);
		car.perform(CarCommands.RELEASE_PARKING_BRAKE);
		car.perform(CarCommands.FASTEN_SEATBELT);
		car.perform(CarCommands.START_TRIP);
		car.perform(CarCommands.END_TRIP);
		
		car.process();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("endtrip"));
		assertThat(car.getCommands(), hasItems(CarCommands.PARK, CarCommands.APPLY_PARKING_BRAKE));
		assertThat(car.getGear(), is(nullValue()));
	}
	
	
	@Test
	public void testAllowStart()  throws Exception {
		// Allow the car to be at the allow-start state without actually running
		Car car = new Car();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("init"));
		
		car.perform(CarCommands.CHECK_MIRROR);
		car.perform(CarCommands.RELEASE_PARKING_BRAKE);
		car.perform(CarCommands.FASTEN_SEATBELT);
		car.perform(CarCommands.FILL_UP_TANK);
		
		car.process();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("allow-start"));
	}
	
	
	@Test
	public void testPreCheck()  throws Exception {
		/*
		Omit START_TRIP so FILL_UP_TANK wont be auto added by ops-auto rules.
		- This will cause the rule to set the state to "allow-start"
		- However, the AUTO_IGNITION commands causes the no gas error
		 */
		Car car = new Car();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("init"));
		
		car.perform(CarCommands.CHECK_MIRROR);
		car.perform(CarCommands.RELEASE_PARKING_BRAKE);
		car.perform(CarCommands.FASTEN_SEATBELT);
		
		car.process();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("checked"));
		assertThat(car.isError(), is(false));
	}
	
	
	@Test
	public void testPreCheckFailure() throws Exception {
		Car car = new Car();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("init"));
		
		car.perform(CarCommands.CHECK_MIRROR);
		car.perform(CarCommands.FASTEN_SEATBELT);
		
		car.process();
		
		assertThat(car.getState(), is("error"));
		assertThat(car.getErrors(), contains("Missing action " + CarCommands.RELEASE_PARKING_BRAKE.name()));
	}
	
}

