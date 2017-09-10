package dao.impl;

import java.util.List;

import model.Comment;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.CommentDao;

public class CommentDaoImpl extends HibernateDaoSupport implements
		CommentDao {

	public Integer save(Comment Comment) {
		return (Integer) getHibernateTemplate().save(Comment);
	}

	public void delete(Comment Comment) {
		getHibernateTemplate().delete(Comment);
	}

	public void update(Comment Comment) {
		getHibernateTemplate().merge(Comment);
	}

	public Comment getCommentById(int id) {
		@SuppressWarnings("unchecked")
		List<Comment> Comments = (List<Comment>) getHibernateTemplate()
				.find("from Comment as oi where oi.id=?", id);
		Comment Comment = Comments.size() > 0 ? Comments.get(0) : null;
		return Comment;
	}

	public List<Comment> getAllComments() {
		@SuppressWarnings("unchecked")
		List<Comment> Comments = (List<Comment>) getHibernateTemplate()
				.find("from comment");
		return Comments;
	}
	public List<Comment>  getCommentsByPid(int pid) {
		@SuppressWarnings("unchecked")
		List<Comment> Comments = (List<Comment>) getHibernateTemplate()
		.find("from Comment as oi where oi.pid=?", pid);
		return Comments;
	}
	public List<Comment>  getCommentsByRid(int rid) {
		@SuppressWarnings("unchecked")
		List<Comment> Comments = (List<Comment>) getHibernateTemplate()
		.find("from Comment as oi where oi.rid=?", rid);
		return Comments;
	}
}
