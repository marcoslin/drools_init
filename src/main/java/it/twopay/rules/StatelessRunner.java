package it.twopay.rules;

import org.kie.api.runtime.StatelessKieSession;

public class StatelessRunner extends Runner {	
	public StatelessRunner(String session) {
		super(session);
	}

	@Override
    public void process() {
    	StatelessKieSession ksession = kc.newStatelessKieSession(this.session);
    	ksession.execute(this.objects);
    }

}
