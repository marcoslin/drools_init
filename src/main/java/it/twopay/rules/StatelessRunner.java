package it.twopay.rules;

import org.kie.api.runtime.StatelessKieSession;

public class StatelessRunner extends Runner {
	private final String SESSION_NAME="StatelessKS";
	
	@Override
    public void process() {
    	StatelessKieSession ksession = kc.newStatelessKieSession(SESSION_NAME);
    	ksession.execute(this.objects);
    }

}
