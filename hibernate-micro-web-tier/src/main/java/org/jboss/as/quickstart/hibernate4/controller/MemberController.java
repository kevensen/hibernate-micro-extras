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
package org.jboss.as.quickstart.hibernate4.controller;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstart.hibernate4.model.Member;
import org.jboss.as.quickstart.hibernate4.service.MemberListProducerService;
import org.jboss.as.quickstart.hibernate4.service.MemberRegistrationService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class MemberController {

    @Inject
    private FacesContext facesContext;

    private MemberRegistrationService memberRegistration;

    private Member newMember;
    
    private MemberListProducerService memberListProducer;
    
    private Properties jndiProps;

    @Produces
    @Named
    public Member getNewMember() {
        return newMember;
    }
    
    @Produces
    @Named
    public List<Member> getMembers() {
        return memberListProducer.getMembers();
    }

    public void register() {
        try {
            memberRegistration.register(newMember);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            init();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Registration unsuccessful"));
        }
    }

    @PostConstruct
    public void init() 
    {
    	newMember = new Member();
    	jndiProps = new Properties();   	
    	jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    	
        try 
        {
        	final Context ctx = new InitialContext(jndiProps);
			memberRegistration = (MemberRegistrationService) ctx.lookup("ejb:/hibernate-micro-business-tier//MemberRegistration!org.jboss.as.quickstart.hibernate4.service.MemberRegistrationService");
			memberListProducer = (MemberListProducerService) ctx.lookup("ejb:/hibernate-micro-business-tier//MemberListProducer!org.jboss.as.quickstart.hibernate4.service.MemberListProducerService?stateful");
        } 
        catch (NamingException e) 
        {
			e.printStackTrace();
		}
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
