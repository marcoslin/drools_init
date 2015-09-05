package it.twopay.entity;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import it.twopay.rules.StatelessRunner;

public class StatelessTest {
	
	@Test
	public void testSimple() {
		Logic logic = new Logic("Logic");
		logic.setState("build");
		
		StatelessRunner runner = new StatelessRunner("StatelessKS");
		
		runner.insert(logic);
		runner.process();
		
		assertThat(logic.getActions(), is(Arrays.asList("build-result")));
	}
	
	@Test
	public void testCallAnswer() {
		Logic logic = new Logic("Logic");
		logic.setState("call");
		
		StatelessRunner runner = new StatelessRunner("StatelessKS");
		
		runner.insert(logic);
		runner.process();
		
		assertThat(logic.getActions(), is(Arrays.asList("call-result")));
	}
	
	@Test
	public void testPingPong() {
		Logic logic = new Logic("Logic");
		logic.setState("ping");
		
		StatelessRunner runner = new StatelessRunner("StatelessKS");
		
		runner.insert(logic);
		runner.process();
		
		assertThat(logic.getActions(), is(Arrays.asList("ping-result", "pong-result")));
	}
	
	
}
