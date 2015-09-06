package it.twopay.entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.*;

public class CarTest {
	private class GearSpeed {
		public Car.Gears gear;
		public double rpm;
		public double speed;
		
		public GearSpeed(Car.Gears gear, double rpm, double speed) {
			this.gear = gear;
			this.rpm = rpm;
			this.speed = speed;
		}
	}
	

	@Test
	public void testCarSpeed() {
		Car car = new Car();
		
		List<GearSpeed> config = new ArrayList<>();
		config.add(new GearSpeed(Car.Gears.GEAR_1, 1000, 10.61));
		config.add(new GearSpeed(Car.Gears.GEAR_1, 3000, 31.83));
		
		config.add(new GearSpeed(Car.Gears.GEAR_2, 1000, 15.86));
		config.add(new GearSpeed(Car.Gears.GEAR_2, 3000, 47.57));
		
		config.add(new GearSpeed(Car.Gears.GEAR_3, 1000, 21.71));
		config.add(new GearSpeed(Car.Gears.GEAR_3, 3000, 65.13));
		
		config.add(new GearSpeed(Car.Gears.GEAR_4, 1000, 28.22));
		config.add(new GearSpeed(Car.Gears.GEAR_4, 3000, 84.66));
		
		config.add(new GearSpeed(Car.Gears.GEAR_5, 1000, 38.19));
		config.add(new GearSpeed(Car.Gears.GEAR_5, 3000, 114.56));
		
		config.add(new GearSpeed(Car.Gears.GEAR_6, 1000, 56.54));
		config.add(new GearSpeed(Car.Gears.GEAR_6, 3000, 169.61));
		
		
		for (GearSpeed entry : config) {
			assertEquals("Check speed for " + entry.gear + " at rpm " + entry.rpm, entry.speed, car.getSpeed(entry.gear, entry.rpm), 0.01);
		}
	}
	
	@Test
	public void testRunning() {
		Car car = new Car();
		
		// Car before ignition the speed should be zero
		assertThat(car.getSpeed(), is(0.0));
		
		// Car after ignition speed should be greater than zero
		car.perform(Car.Actions.IGNITION);
		double initSpeed = car.getSpeed();
		assertThat(initSpeed, greaterThan(0.0));
		
		// Speed must increase after accelerate
		car.accelerate();
		double speed = car.getSpeed();
		assertThat(speed, greaterThan(initSpeed));
		
		// Accelerate must continue to increase speed
		car.accelerate();
		assertThat(car.getSpeed(), greaterThan(speed));
		
		// Turn off the car and speed back to zero
		car.perform(Car.Actions.TURN_OFF);
		assertThat(car.getSpeed(), is(0.0));
		
		// Check action list
		assertThat(car.getActions(), contains(Car.Actions.IGNITION, Car.Actions.TURN_OFF));
	}
	
	@Test
	public void testGear() {
		Car car = new Car();
		car.perform(Car.Actions.IGNITION);
		
		// Starting gear should be 1
		assertThat(car.getGear(), is(Car.Gears.GEAR_1));
		
		// Down shift should have no effect
		car.downShift();
		assertThat(car.getGear(), is(Car.Gears.GEAR_1));
		
		// Up shift 5 times
		for (int i=2; i <= 6; i++ ) {
			car.upShift();
			// car.log.info("Checking gear " + car.getGear() + ": " + i);
			assertThat(car.getGear().getValue(), is(i));
		}
		
		// Up shift should have no effect
		car.upShift();
		assertThat(car.getGear(), is(Car.Gears.GEAR_6));
		
		// Down shift 5 times
		for (int i=5; i > 0; i-- ) {
			car.downShift();
			assertThat(car.getGear().getValue(), is(i));
		}
	}
	
	@Test
	public void testState() {
		Car car = new Car();
		
		assertThat(car.getState(), is("init"));
		assertThat(car.getStateCount(), is(0));
		
		// Set state
		car.setState("started");
		assertThat(car.getState(), is("started"));
		assertThat(car.getStateCount(), is(1));
		
		// Set state 4 times
		for (int i=2; i < 6; i++ ) {
			car.setState("started");
			// car.log.info("Checking gear " + car.getGear() + ": " + i);
			assertThat(car.getStateCount(), is(i));
		}
		
		// Set next state
		car.setState("ended");
		assertThat(car.getState(), is("ended"));
		assertThat(car.getStateCount(), is(1));
	}
		
}
