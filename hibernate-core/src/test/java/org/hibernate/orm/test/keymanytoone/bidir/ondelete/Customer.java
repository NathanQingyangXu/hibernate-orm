/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.keymanytoone.bidir.ondelete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lukasz Antoniak
 */
public class Customer implements Serializable {
	private Long id;
	private String name;
	private Collection orders = new ArrayList();

	public Customer() {
	}

	public Customer(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection getOrders() {
		return orders;
	}

	public void setOrders(Collection orders) {
		this.orders = orders;
	}
}
