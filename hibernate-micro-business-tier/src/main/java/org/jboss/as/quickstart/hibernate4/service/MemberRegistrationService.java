package org.jboss.as.quickstart.hibernate4.service;

import javax.ejb.Remote;

import org.jboss.as.quickstart.hibernate4.model.Member;

@Remote
public interface MemberRegistrationService {
	
	 public void register(Member member) throws Exception;

}
