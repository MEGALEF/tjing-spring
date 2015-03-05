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
package se.tjing.signup;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import se.tjing.exception.TjingException;
import se.tjing.message.Message;
import se.tjing.message.MessageType;
import se.tjing.signin.SignInUtils;
import se.tjing.user.Person;
import se.tjing.user.PersonRepository;
import se.tjing.user.PersonService;

@Controller
public class SignupController {

	@Inject
	PasswordEncoder encoder;

	@Inject
	PersonRepository accountRepository;

	@Inject
	PersonService personService;

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
		Connection<?> connection = providerSignInUtils
				.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute(
					"message",
					new Message(
							MessageType.INFO,
							"Your "
									+ StringUtils.capitalize(connection
											.getKey().getProviderId())
									+ " account is not associated with a Tjing account. If you're new, please sign up."),
					WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding,
			WebRequest request) {
		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
		if (formBinding.hasErrors()) {
			return null;
		}
		Person account = createAccount(form, formBinding);
		if (account != null) {
			List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
			User userObj = new User(account.getUsername(), account.getPassword(),
					authorities);
			SignInUtils.signin(userObj); // TODO: This probably isn't right
			providerSignInUtils.doPostSignUp(account.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}

	// internal helpers

	private Person createAccount(SignupForm form, BindingResult formBinding) {
		try {
			Person account = new Person(form.getUsername(), encoder.encode(form
					.getPassword()), form.getFirstName(), form.getLastName());
			personService.addPerson(account);
			return account;
		} catch (TjingException e) {
			formBinding.rejectValue("username", "user.duplicateUsername",
					"already in use");
			return null;
		}
	}

}
