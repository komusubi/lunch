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
package jp.dip.komusubi.lunch.wicket.page.settings;

import jp.dip.komusubi.lunch.wicket.WicketSession;
import jp.dip.komusubi.lunch.wicket.page.Login;
import jp.dip.komusubi.lunch.wicket.page.VariationBase;
import jp.dip.komusubi.lunch.wicket.panel.Footer;
import jp.dip.komusubi.lunch.wicket.panel.Header;
import jp.dip.komusubi.lunch.wicket.panel.Profile;

public class Account extends VariationBase {

	private static final long serialVersionUID = 5109190494409699152L;

	public Account() {
		add(new Header("header"));
		add(new Profile("profile"));
		add(new Footer("footer"));
	}

	// check login 
	@Override
	public void onBeforeRender() {
		if (!WicketSession.get().isSignedIn())
			setResponsePage(Login.class);
		
		super.onBeforeRender();
	}
}
