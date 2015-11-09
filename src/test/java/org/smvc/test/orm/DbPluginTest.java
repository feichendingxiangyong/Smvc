package org.smvc.test.orm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.smvc.framework.orm.C3P0Plugin;
import org.smvc.framework.orm.DbPlugin;
import org.smvc.framework.orm.annotation.Parameter;
import org.smvc.test.orm.model.Grade;
import org.smvc.test.orm.model.Student;

/**
 * TEST
 * 
 * @author Big Martin
 *
 */
public class DbPluginTest {
	
	private static C3P0Plugin dsPlugin;
	
	static{
		//db property file
		String FILE_NAME = "/db.properties";
		
		InputStream in = DbPluginTest.class.getResourceAsStream(FILE_NAME);
		Properties property = new Properties();
		try {
			property.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String jdbcUrl = property.getProperty("jdbc.url");
		String user = property.getProperty("jdbc.username");
		String password = property.getProperty("jdbc.password");
		String driverClass = property.getProperty("jdbc.driverClassName");
		int maxPoolSize = Integer.parseInt(property
				.getProperty("maxPoolSize"));
		int minPoolSize = Integer.parseInt(property
				.getProperty("minPoolSize"));
		int initialPoolSize = Integer.parseInt(property
				.getProperty("initialPoolSize"));
		int maxIdleTime = Integer.parseInt(property
				.getProperty("maxIdleTime"));
		int acquireIncrement = Integer.parseInt(property
				.getProperty("acquireIncrement"));
		
		//init data source plugin
		dsPlugin = new C3P0Plugin(jdbcUrl, user, password,
				driverClass, maxPoolSize, minPoolSize, initialPoolSize,
				maxIdleTime, acquireIncrement);
	}

	@Test
	public void testAll() throws IllegalArgumentException,
			IllegalAccessException {
		DbPlugin<Student> studentDao = new DbPlugin<Student>(dsPlugin);
		DbPlugin<Grade> gradeDao = new DbPlugin<Grade>(dsPlugin);
		Student stu = new Student("martin", "121", "TaiWan");

		// query all users named martin
		Parameter param = new Parameter("name", "martin");

		// clear all users named martin
		studentDao.delete(Student.class, param);

		// clear all grades
		gradeDao.deleteAll(Grade.class);

		List<Student> students = studentDao.queryList(Student.class, param);
		List<Grade> grades = gradeDao.queryAllList(Grade.class);
		Assert.assertTrue(students.isEmpty());
		Assert.assertTrue(grades.isEmpty());

		// insert
		// int affectedRows = dao.save(stu);
		long studentId = studentDao.saveWithGeneratedKeys(stu);

		List<Grade> gradesList = new ArrayList<Grade>();
		gradesList.add(new Grade(1, (int) studentId, "Math", 94));
		gradesList.add(new Grade(2, (int) studentId, "English", 98));
		gradeDao.saveList(gradesList);

		List<Student> students2 = studentDao.queryList(Student.class, param);
		List<Grade> grades2 = gradeDao.queryAllList(Grade.class);
		Assert.assertTrue(students2.size() == 1);
		Assert.assertTrue(grades2.size() == 2);

		// update
		stu.setSchool("Nanjin");

		studentDao.update(stu, param);

		// query
		Student student = studentDao.query(Student.class, param);
		Assert.assertEquals("Nanjin", student.getSchool());

	}

	@Test
	public void testQueryWithSql() throws IOException {
		
		DbPlugin<Grade> gradeDao = new DbPlugin<Grade>(dsPlugin);

		// query part of rows with sql, this feel good
		List<Grade> gradesList = gradeDao
				.queryListWithSql(
						"select id, class_name, score from grade t",
						Grade.class);
		Assert.assertTrue(gradesList.size() == 2);

		Grade grade = gradeDao
				.queryWithSql(
						"select id, class_name, score from grade t where t.class_name = ?",
						Grade.class, new Object[] { "Math" });

		Assert.assertNotNull(grade);

	}

}
