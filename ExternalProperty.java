package com.simens.contest.gll;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * 
 * @author GHuang
 */
public class ExternalProperty {
	private static final String BUNDLE_NAME = "com.simens.contest.gll.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private ExternalProperty() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
