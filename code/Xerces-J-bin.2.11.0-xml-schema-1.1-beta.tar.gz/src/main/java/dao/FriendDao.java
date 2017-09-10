package dao;

import java.util.List;

import model.Friend;

public interface FriendDao {

	public Integer save(Friend Friend);

	public void delete(Friend Friend);

	public void update(Friend Friend);

	public List<Friend> getFriendByUid(int id);
	public Friend getFriendByUids(int uid1,int uid2,int state);
	public List<Friend> getAllFriends();
	public int check(int uid1,int uid2);
}