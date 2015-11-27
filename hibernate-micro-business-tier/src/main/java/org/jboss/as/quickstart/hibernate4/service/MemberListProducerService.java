package org.jboss.as.quickstart.hibernate4.service;


import java.util.List;

import javax.ejb.Remote;

import org.jboss.as.quickstart.hibernate4.model.Member;

@Remote
public interface MemberListProducerService {
	
	public List<Member> getMembers();

}
