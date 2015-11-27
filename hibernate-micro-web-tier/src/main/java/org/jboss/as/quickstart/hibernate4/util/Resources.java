/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstart.hibernate4.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstart.hibernate4.model.Member;
import org.jboss.as.quickstart.hibernate4.service.MemberListProducerService;
import org.jboss.as.quickstart.hibernate4.service.MemberRegistrationService;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
@ManagedBean
public class Resources {


    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    @Produces
    public MemberRegistrationService produceMemberRegistrationService()
    {
    	try
    	{
	    	Properties jndiProps = new Properties();
	   	
	    	
	    	jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
	    	
	    	final Context ctx = new InitialContext(jndiProps);
	   	
	    	MemberRegistrationService service = (MemberRegistrationService) ctx.lookup("ejb:/jboss-hibernate4-micro-business/MemberRegistration!"+MemberRegistrationService.class.getName());
	    	return service;
    	}
    	catch(Exception e)
    	{
    		System.err.println(e);
    	}
    	return null;
    	
    }
    
    @Produces
    @Named
    public List<Member> getMembers() throws NamingException
    {

    	Properties jndiProps = new Properties();
   	
    	
    	jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    	
    	final Context ctx = new InitialContext(jndiProps);
   	
    	MemberListProducerService service = (MemberListProducerService) ctx.lookup("ejb:/jboss-hibernate4-micro-business/MemberListProducer!"+MemberListProducerService.class.getName()+"?stateful");
    	return service.getMembers();
    	
    	
    }
    
    @Produces
    @Named
    public String getHostIpAddress()
    {
    	
    	InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "IP Not Found";
    	
    }
    

    


}
