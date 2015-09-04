package it.twopay.rules;

import org.kie.api.runtime.KieSession;

public class StatefulRunner extends Runner {
	private final String SESSION_NAME="StatefulKS";
	private KieSession ksession;
	
	public StatefulRunner() {
		super();
		ksession = kc.newKieSession(SESSION_NAME);
	}
	
    public void insert(Object object) {
        ksession.insert(object);
    }

    public int fire() {
        int fired = ksession.fireAllRules();
        //TODO use name of class that uses that method
        log.info("*** Fired " + fired + " rules");
        return fired;

    }

    public void dispose() {
        ksession.dispose();
        ksession = kc.newKieSession(SESSION_NAME);
    }
	
}
