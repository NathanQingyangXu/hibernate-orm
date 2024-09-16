/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.jpa.orphan.onetomany;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.orm.junit.DialectFeatureChecks;
import org.hibernate.testing.orm.junit.EntityManagerFactoryScope;
import org.hibernate.testing.orm.junit.Jpa;
import org.hibernate.testing.orm.junit.RequiresDialectFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Jpa(
		annotatedClasses = {
				EagerOneToManyOrphanWithIdentityIdTest.Parent.class,
				EagerOneToManyOrphanWithIdentityIdTest.Child.class
		}
)
@JiraKey(value = "HHH-15258")
@RequiresDialectFeature(feature = DialectFeatureChecks.SupportsIdentityColumns.class)
public class EagerOneToManyOrphanWithIdentityIdTest {

	@AfterEach
	public void tearDown(EntityManagerFactoryScope scope) {
		scope.inTransaction(
				entityManager -> {
					entityManager.createQuery( "delete from Child" ).executeUpdate();
					entityManager.createQuery( "delete from Parent" ).executeUpdate();
				}
		);
	}

	@Test
	public void testPersistParentAndQueryInTheSameSession(EntityManagerFactoryScope scope) {
		scope.inTransaction(
				entityManager -> {
					Parent parent = new Parent();
					entityManager.persist( parent );

					List result = entityManager.createQuery( "from Parent" ).getResultList();

					Parent p = (Parent) result.get( 0 );

					assertTrue( Hibernate.isInitialized( p.getChildren() ) );
					assertThat( p.getChildren().size(), is( 0 ) );

					entityManager.createQuery( "from Parent" ).getResultList();
				}
		);
	}

	@Test
	public void testPersistParentWithChildAndQueryInTheSameSession(EntityManagerFactoryScope scope) {
		scope.inTransaction(
				entityManager -> {
					Parent parent = new Parent();
					Child child = new Child();
					parent.addChild( child );
					entityManager.persist( child );
					entityManager.persist( parent );

					List result = entityManager.createQuery( "from Parent" ).getResultList();

					Parent p = (Parent) result.get( 0 );

					assertTrue( Hibernate.isInitialized( p.getChildren() ) );
					assertThat( p.getChildren().size(), is( 1 ) );
					assertSame( child, parent.getChildren().get( 0 ) );
					entityManager.createQuery( "from Parent" ).getResultList();
				}
		);
	}

	@Entity(name = "Parent")
	static class Parent {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		Long id;

		String name;

		@OneToMany(mappedBy = "parent", orphanRemoval = true, fetch = FetchType.EAGER)
		List<Child> children = new ArrayList<>();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public List<Child> getChildren() {
			return children;
		}

		public void setChildren(List<Child> children) {
			this.children = children;
		}

		public void addChild(Child child) {
			children.add( child );
			child.setParent( this );
		}
	}

	@Entity(name = "Child")
	static class Child {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		Long id;

		String name;

		@ManyToOne
		Parent parent;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Parent getParent() {
			return parent;
		}

		public void setParent(Parent parent) {
			this.parent = parent;
		}
	}

}
