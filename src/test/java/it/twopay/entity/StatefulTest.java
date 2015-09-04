package it.twopay.entity;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import it.twopay.rules.StatefulRunner;

public class StatefulTest {
	
	@Test
	public void testSimple() {
		Logic logic = new Logic("Logic");
		logic.setState("build");
		
		StatefulRunner runner = new StatefulRunner();
		
		runner.insert(logic);
		int firedCount = runner.fire();
		runner.dispose();
		
		assertThat(firedCount, is(1));
		assertThat(logic.getActions(), is(Arrays.asList("build-result")));
	}
	
	@Test
	public void testCallAnswer() {
		Logic logic = new Logic("Logic");
		logic.setState("call");
		
		StatefulRunner runner = new StatefulRunner();
		
		runner.insert(logic);
		int firedCount = runner.fire();
		runner.dispose();
		
		assertThat(firedCount, is(1));
		assertThat(logic.getActions(), is(Arrays.asList("call-result")));
	}
	
	@Test
	public void testPingPong() {
		Logic logic = new Logic("Logic");
		logic.setState("ping");
		
		StatefulRunner runner = new StatefulRunner();
		
		runner.insert(logic);
		int firedCount = runner.fire();
		runner.dispose();
		
		assertThat(firedCount, is(2));
		assertThat(logic.getActions(), is(Arrays.asList("ping-result", "pong-result")));
	}
	
	
}
