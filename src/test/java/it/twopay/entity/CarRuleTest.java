package it.twopay.entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.*;

public class CarRuleTest {

	@Test
	public void testPrecheck() {
		Car car = new Car();
		
		assertThat(car.isRunning(), is(false));
		assertThat(car.getState(), is("init"));
		
		car.perform(Car.Actions.FILL_UP_TANK);
		car.perform(Car.Actions.CHECK_MIRROR);
		car.perform(Car.Actions.RELEASE_PARKING_BRAKE);
		car.perform(Car.Actions.FASTEN_SEATBELT);
		
		car.process();
		
		assertThat(car.isRunning(), is(true));
	}
}

