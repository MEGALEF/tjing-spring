/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.tjing.signin;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.web.context.request.NativeWebRequest;

import se.tjing.pool.PoolService;
import se.tjing.user.PersonService;

public class SimpleSignInAdapter implements SignInAdapter {

	private final RequestCache requestCache;

	@Autowired
	Facebook facebook;

	@Autowired
	PersonService personService;
	
	@Autowired
	PoolService poolService;

	@Inject
	public SimpleSignInAdapter(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	@Override
	public String signIn(String localUserId, Connection<?> connection,
			NativeWebRequest request) {
		User userObj = new User(localUserId, "", AuthorityUtils.NO_AUTHORITIES); // TODO
																					// This
																					// definitely
		// isn't right. See
		// Other signin
		// location.

		SignInUtils.signin(userObj);
		
		if(facebook.isAuthorized()){
			//poolService.importFacebookGroups(personService.getCurrentUser());
		} 
		
		return extractOriginalUrl(request);
	}

	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request
				.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request
				.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}

	private void removeAutheticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
