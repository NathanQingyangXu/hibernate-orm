/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.tool.schema.spi;

import org.hibernate.HibernateException;

/**
 * Indicates a problem
 * @author Steve Ebersole
 */
public class SqlScriptException extends HibernateException {
	public SqlScriptException(String message) {
		super( message );
	}

	public SqlScriptException(String message, Throwable cause) {
		super( message, cause );
	}
}
