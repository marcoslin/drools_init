package it.twopay.rules;

import org.kie.api.runtime.KieSession;

public class StatefulRunner extends Runner {
	private final String SESSION_NAME="StatefulKS";
	private int firedRulesCount;
	private KieSession ksession=null;
	
	public StatefulRunner(String session) {
		super(session);
	}

	private void startSession() {
		firedRulesCount = 0;
		ksession = kc.newKieSession(this.session);
	}
	
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
        ksession = null;
	}
	
	@Override
	public void insert(Object object) {
		super.insert(object);
		if (ksession!=null) {
			ksession.insert(object);
		}
	}
	
    @Override
    public void process() {
    	startSession();
    	insertObjects(ksession);
    	fireRules(ksession);
    	cleanUp(ksession);
    }
    
    public void process(String name) {
    	startSession();
    	insertObjects(ksession);
    	ksession.startProcess(name);
    	fireRules(ksession);
    	cleanUp(ksession);
    }

	public int getFiredRulesCount() {
		return firedRulesCount;
	}

}
