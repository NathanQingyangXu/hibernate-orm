/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.bytecode.enhance.internal.bytebuddy;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

// This class must not be nested in the test class, otherwise its private fields will be visible
// from subclasses and we won't reproduce the bug.
@MappedSuperclass
public abstract class MyNonVisibleGenericMappedSuperclass<C> {

	@Embedded
	private C embedded;

	public C getEmbedded() {
		return embedded;
	}

	public void setEmbedded(C embedded) {
		this.embedded = embedded;
	}
}
