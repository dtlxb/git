package dao.impl;

import java.util.List;

import model.Friend;
 

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.FriendDao;

public class FriendDaoImpl extends HibernateDaoSupport implements
		FriendDao {

	public Integer save(Friend Friend) {
		return (Integer) getHibernateTemplate().save(Friend);
	}

	public void delete(Friend Friend) {
		getHibernateTemplate().delete(Friend);
	}

	public void update(Friend Friend) {
		getHibernateTemplate().merge(Friend);
	}

	public List<Friend> getFriendByUid(int id) {
		@SuppressWarnings("unchecked")
		List<Friend> Friends = (List<Friend>) getHibernateTemplate()
				.find("from Friend as oi where oi.uid1=? or oi.uid2 =?", id,id);
	 
		return Friends;
	}
	
	public int check(int uid1,int uid2) {
		@SuppressWarnings("unchecked")
		List<Friend> Friends = (List<Friend>) getHibernateTemplate()
				.find("from Friend as oi where (oi.uid1=? and oi.uid2 =?) or(oi.uid2=? and oi.uid1 =?) and oi.state = 1 ",uid1,uid2,uid1,uid2);
		if(Friends.size()>0){
			return 0;      // 好友关系
		}
		return 1;			//非好友关系
	}
	public Friend getFriendByUids(int uid1,int uid2,int state) {
		@SuppressWarnings("unchecked")
		List<Friend> Friends = (List<Friend>) getHibernateTemplate().find(
				"from Friend as oi  where  ((oi.uid1=? and oi.uid2 =?) or(oi.uid2=? and oi.uid1 =?) )and oi.state = ?", uid1,uid2,uid1,uid2,state);
		Friend Friend = Friends.size() > 0 ? Friends.get(0) : null;
		return Friend;
	}
	
	
	public List<Friend> getAllFriends() {
		@SuppressWarnings("unchecked")
		List<Friend> Friends = (List<Friend>) getHibernateTemplate()
				.find("from Friend");
		return Friends;
	}

}
