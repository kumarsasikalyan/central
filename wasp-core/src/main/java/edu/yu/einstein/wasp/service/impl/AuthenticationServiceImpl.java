package edu.yu.einstein.wasp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserService;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPendingService userPendingService;
	
	@Autowired
	private MessageService messageService;

// 	
	@Autowired
	private AuthenticationManager authenticationManager;

// TODO: no autowire... so that i can run outside the tomcat container	
//	@Autowired
//	HttpServletRequest request;

	protected HttpServletRequest getHttpServletRequest() {
		try {
			HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			return request;
		} catch (Throwable e) {
			// logger.warn("could not get HttpServletRequest");
		}
		return null;
	}

	@Override
	public User getAuthenticatedUser() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User user = this.userService.getUserByLogin(authentication.getName());
		 return user;
	}
	@Override
	public boolean isAuthenticated(){
		return (getAuthenticatedUser().getUserId() != null);
	}
	
	@Override
	public void logoutUser(){
		if (this.isAuthenticated()){
			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.setInvalidateHttpSession(true);
			logoutHandler.logout(getHttpServletRequest(), null, null);
		}
	}

	@Override
	public String[] getRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    List<GrantedAuthority>  col = (List<GrantedAuthority>) authentication.getAuthorities();
	    String[] roles = new String[col.size()];
	    int i = 0;
	    for (GrantedAuthority role : col) {
	    	roles[i] = role.getAuthority();
	    	i++;
	    }
	    return roles;
	}

	@Override
	public boolean isExternallyAuthenticated(){
		for (String role: this.getRoles()){
			if (role.equals("ldap"))
					return true;
		}
		return false;
	}

	@Override
	public boolean isAuthenticationSetExternal() {
		String authenticationIsExternalString = messageService.getMetadataValue("wasp.isAuthenticationExternal.data");
		if (authenticationIsExternalString != null && 
				!authenticationIsExternalString.isEmpty() && 
				(authenticationIsExternalString.toUpperCase().equals("TRUE") ||
				authenticationIsExternalString.equals("1") ) ){
			return true;
		}
		return false;
	}

	@Override
	public boolean isAuthenticatedGuest() {
		for (String role: this.getRoles()){
			if (role.equals("ag"))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean hasRole(String theRole) {
		
		for (String role: this.getRoles()) {
			if(role.equals(theRole)){return true;}//in case theRole is something like da-*
			String[] splitRole = role.split("-");//if no "-" is within role, then splitRole[0] contains entire original string
			if(splitRole[0].equals(theRole)){
				return true;
			}	
		}
		return false;
	}
	
	@Override
	public boolean isSuperUser() {
		for (String role: this.getRoles()){
			if (role.equals("su"))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean authenticate(String name, String password){
		try {
	        Authentication authRequest = new UsernamePasswordAuthenticationToken(name, password);
	        Authentication result = authenticationManager.authenticate(authRequest);
	        SecurityContextHolder.getContext().setAuthentication(result);
	    } catch(AuthenticationException e) {
	        return false;
	    }
		return true;
	}
	
	@Override
	public boolean authenticates(String name, String password){
		try {
	        Authentication authRequest = new UsernamePasswordAuthenticationToken(name, password);
	        Authentication result = authenticationManager.authenticate(authRequest);
	    } catch(AuthenticationException e) {
	        return false;
	    }
		return true;
	}

	@Override
	public boolean isLoginNameWellFormed(String login) {
		if (login == null || login.isEmpty()){
			return false;
		}
		if (login.matches("[-@\\.\\w]+")){
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * Checks user table then userPending table (in latter checks for no same login name not-yet approved without same email address)
	 * 
	 */
	@Override
	public boolean isLoginAlreadyInUse(String login, String email) throws LoginNameException {
		if (!this.isLoginNameWellFormed(login)){
			throw new LoginNameException();
		}

		// NV 12132011
		//if (userService.getUserByLogin(login).getUserId() != null){
		if (userService.getUserByLogin(login).getUserId() != null){

			return true;
		} else {
			Map loginQueryMap = new HashMap();
			loginQueryMap.put("login", login);
			for (UserPending up : (List<UserPending>) userPendingService.findByMap(loginQueryMap)){
				if ( (up.getStatus().equals("WAIT_EMAIL") || up.getStatus().equals("PENDING")) && !up.getEmail().equals(email) ){
					return true; // login name already chosen by someone with different email address
				}
			}
		}
		return false;
	}
}