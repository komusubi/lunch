/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package jp.dip.komusubi.lunch.wicket.panel;

import java.text.MessageFormat;

import jp.dip.komusubi.lunch.Configuration;
import jp.dip.komusubi.lunch.model.User;
import jp.dip.komusubi.lunch.service.AccountService;
import jp.dip.komusubi.lunch.wicket.WicketSession;
import jp.dip.komusubi.lunch.wicket.page.Reminder;
import jp.dip.komusubi.lunch.wicket.panel.util.SpecificBehavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.util.string.UrlUtils;

public class SignIn extends SignInPanel {

	private static final long serialVersionUID = 4834818596146852372L;
	
	public SignIn(String id) {
		super(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onBeforeRender() {
		SignInForm form = getForm();
		if (WicketSession.VARIATION_JQUERY_MOBILE.equals(getVariation())) {
			form.add(new AttributeModifier("data-ajax", false));
		}
		
		FormComponent<String> text = (FormComponent<String>) getForm().get("username");
		FormComponent<String> password = (FormComponent<String>) getForm().get("password");
		
		SpecificBehavior.behaveIdField(text);
		SpecificBehavior.behavePasswordField(password);
		
		super.onBeforeRender();
	}
	
	@Override
	public void onSignInFailed() {
		AccountService accountService = Configuration.getInstance(AccountService.class);
		User user = accountService.find(getUsername());
		if (user != null && !user.getHealth().isActive()) {
			get("feedback").setEscapeModelStrings(false);
			
			String u = getRequest().getClientUrl().toAbsoluteString();
			String path = urlFor(Reminder.class, null).toString();
			String urlBase = getRequestCycle().getUrlRenderer().renderFullUrl(getRequest().getClientUrl());
			String url = RequestUtils.toAbsolutePath(urlBase, path);
			error(MessageFormat.format(getString("user.not.active"), user.getName(), url));
		} else {
			super.onSignInFailed();
		}
			
	}
}
