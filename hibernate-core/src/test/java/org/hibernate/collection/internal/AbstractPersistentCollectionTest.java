package org.hibernate.collection.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.build.AllowSysOut;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Assert;
import org.junit.Test;

import static org.hibernate.testing.transaction.TransactionUtil.doInHibernate;

/**
 * @author Nathan Xu
 */
public class AbstractPersistentCollectionTest extends BaseCoreFunctionalTestCase {

	private College college;

	private College loadedCollege;

	private final ExecutorService executorService = Executors.newFixedThreadPool(5);

	@Override
	protected void configure(Configuration configuration) {
		configuration.setProperty( AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, Boolean.TRUE.toString() );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				College.class
		};
	}

	@Override
	protected void prepareTest() throws Exception {
		doInHibernate( this::sessionFactory, session -> {
			college = new College();
			college.setStudents( Arrays.asList( "student1", "student2", "student3" ) );
			session.persist( college );
		} );
	}

	@Test
	@TestForIssue( jiraKey = "HHH-13790" )
	@AllowSysOut
	public void testConnectionLeakWhenExceptionThrown() throws Exception {
		doInHibernate( this::sessionFactory, session -> {
			loadedCollege = session.load( College.class, college.getId() );
		} );

		final List<Callable<String>> futureTasks = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			futureTasks.add(() -> loadedCollege.getStudents().get(0));
		}
		List<Future<String>> futures = executorService.invokeAll(futureTasks);
		for (Future<String> future : futures) {
			try {
				future.get();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		AbstractPersistentCollection students = ( AbstractPersistentCollection )(loadedCollege.getStudents());
		Assert.assertNull(students.getSession());
	}

	@Entity( name = "College" )
	@Table( name = "college" )
	public static class College {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@ElementCollection(fetch = FetchType.LAZY)
		@OrderColumn
		private List<String> students = new ArrayList<>();

		public long getId() {
			return id;
		}

		public List<String> getStudents() {
			return students;
		}

		public void setStudents(List<String> students) {
			this.students = students;
		}
	}

}


