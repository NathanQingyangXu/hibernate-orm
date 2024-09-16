/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.model.source.spi;

import org.hibernate.boot.model.JavaTypeDescriptor;

/**
 * @author Steve Ebersole
 */
public interface JavaTypeDescriptorResolvable {
	void resolveJavaTypeDescriptor(JavaTypeDescriptor descriptor);
}
