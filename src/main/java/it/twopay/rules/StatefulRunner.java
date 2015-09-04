package it.twopay.rules;

import org.kie.api.runtime.KieSession;

public class StatefulRunner extends Runner {
	private final String SESSION_NAME="StatefulKS";
	private int firedRulesCount;
		
	private void insertObjects(KieSession ksession) {
    	for (Object object : this.objects) {
    		ksession.insert(object);
    	}
	}
	
	private void fireRules(KieSession ksession) {
		firedRulesCount = ksession.fireAllRules();
    	log.info("*** Fired " + firedRulesCount + " rules");
	}
	
	private void cleanUp(KieSession ksession) {
        ksession.dispose();
	}
	
    @Override
    public void process() {
    	KieSession ksession = kc.newKieSession(SESSION_NAME);
    	
    	insertObjects(ksession);
    	fireRules(ksession);
    	cleanUp(ksession);

    }

	public int getFiredRulesCount() {
		return firedRulesCount;
	}

}
