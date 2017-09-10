package dao;

import java.util.List;

import model.Comment;

public interface CommentDao {

	public Integer save(Comment Comment);

	public void delete(Comment Comment);

	public void update(Comment Comment);

	public Comment getCommentById(int id);
	public List<Comment>  getCommentsByPid(int pid);
	public List<Comment> getAllComments();
	public List<Comment>  getCommentsByRid(int rid);
}