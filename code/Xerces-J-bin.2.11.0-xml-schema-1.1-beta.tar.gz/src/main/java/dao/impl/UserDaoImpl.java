package dao.impl;

import java.sql.SQLException;
import java.util.List;

import model.User;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.UserDao;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	public Integer save(User user) {
		return (Integer) getHibernateTemplate().save(user);
	}

	public void delete(User user) {
		getHibernateTemplate().delete(user);
	}

	public void update(User user) {
		getHibernateTemplate().merge(user);
	}

	public User getUserById(int id) {
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) getHibernateTemplate().find(
				"from User as u where u.id=?", id);
		User user = users.size() > 0 ? users.get(0) : null;
		return user;
	}
	public Integer check(String username,String password){
		@SuppressWarnings("unchecked")
		List<User> USERS = (List<User>) getHibernateTemplate().find(
				"from User as u where u.username=?and u.password=?", username,password);
		System.out.print(USERS.size());
		if(USERS.size() > 0 ){
			User user = USERS.get(0);
			return user.getId(); 
		}

		return 0;
	}
	public Integer checkUsername(String username,String password){
		@SuppressWarnings("unchecked")
		List<User> USERS = (List<User>) getHibernateTemplate().find(
				"from User as u where u.username=? ", username );
			
		if(USERS.size() > 0 ){
			User user = USERS.get(0);
			return user.getId(); 
		}

		return 0;
	}
	
	public List<User> getAllUsers() {
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) getHibernateTemplate()
				.find("from User");
		return users;
	}
    public int callProcedure(final String procedureName)
    {
        @SuppressWarnings("unchecked")
		int count =    (Integer)this.getHibernateTemplate().execute(new HibernateCallback(){
            public Object doInHibernate(Session session)throws HibernateException, SQLException {
                String procedureSql = "{call "+ procedureName +"()}";
                Query query = session.createSQLQuery(procedureSql);
                Integer num = query.executeUpdate();
                return num;
            }
        });
        return count;
    }
}
