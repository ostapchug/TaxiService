package test.com.example.taxiservice.dao.mysql;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.taxiservice.dao.Fields;
import com.example.taxiservice.dao.mysql.MySqlPersonDao;
import com.example.taxiservice.model.Person;

@RunWith(MockitoJUnitRunner.class)
public class TestMySqlPersonDao {
	
	@Mock
	private DataSource dataSource;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet set;
	
	@InjectMocks
	private MySqlPersonDao personDao;
	
	private Person person;

	@Before
	public void setUp() throws Exception {
		person = new Person();
		person.setId(4L);
		person.setPhone("0123456783");
		person.setPassword("client3");
		person.setName("Tom");
		person.setSurname("Smith");
		
		when(dataSource.getConnection()).thenReturn(connection);
		
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
		
		when(statement.executeQuery()).thenReturn(set);
		when(statement.getGeneratedKeys()).thenReturn(set);
		
		when(set.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(set.getLong(Fields.PERSON__ID)).thenReturn(person.getId());
		
	}
	
	@Test
	public void testFindById() throws SQLException {
		Person result = personDao.find(4L);
		assertEquals(person, result);
		
	}
	
	@Test
	public void testFindByPhone() throws SQLException {
		Person result = personDao.find(person.getPhone());
		assertEquals(person, result);
		
	}

	@Test
	public void testInsert() throws SQLException {
		
		personDao.insert(person);

		// verify and assert
		verify(connection, times(1)).prepareStatement(anyString(), anyInt());
		verify(statement, times(4)).setString(anyInt(), anyString());
		verify(statement, times(1)).executeUpdate();
		verify(connection, times(1)).commit();
		verify(set, times(2)).next();
		verify(set, times(1)).getLong(anyInt());

	}
	
	@Test
	public void testUpdate() throws SQLException {
		
		personDao.update(person);

		verify(connection, times(1)).prepareStatement(anyString());
		verify(statement, times(4)).setString(anyInt(), anyString());
		verify(statement, times(1)).setLong(anyInt(), anyLong());
		verify(statement, times(1)).executeUpdate();
		verify(connection, times(1)).commit();

	}

}