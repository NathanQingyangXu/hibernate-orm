/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.query.hql.spi;

import org.hibernate.Incubating;
import org.hibernate.query.sqm.StrictJpaComplianceViolation;

/**
 * Options for semantic analysis
 *
 * @author Steve Ebersole
 */
@Incubating
public interface SqmCreationOptions {
	/**
	 * Should we interpret the query strictly according to the JPA specification?  In
	 * other words, should Hibernate "extensions" to the query language be disallowed?
	 *
	 * @see StrictJpaComplianceViolation
	 */
	default boolean useStrictJpaCompliance() {
		return false;
	}

	/**
	 * @see org.hibernate.cfg.AvailableSettings#PORTABLE_INTEGER_DIVISION
	 */
	default boolean isPortableIntegerDivisionEnabled() {
		return false;
	}
}
