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
package jp.dip.komusubi.lunch.wicket.page;

import jp.dip.komusubi.lunch.wicket.panel.Footer;
import jp.dip.komusubi.lunch.wicket.panel.Header;
import jp.dip.komusubi.lunch.wicket.panel.Registry;
import jp.dip.komusubi.lunch.wicket.panel.SignIn;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

public class Login extends VariationBase {

	private static final long serialVersionUID = -5101224283988545642L;
	
	public Login() {
		this(new PageParameters());
	}
	
	public Login(PageParameters params) {
		StringValue activateValue = params.get("activate");
		SignIn signIn;
		if (!activateValue.isEmpty())
			signIn = new SignIn("signInPanel", activateValue.toString());
		else
			signIn = new SignIn("signInPanel");
		
		add(signIn);
		add(new Header("header", Model.of(getDefaultHeaderBean(getString("page.title"))), false));
		add(new Registry("registry"));
		add(new Footer("footer"));
	}
}
