package dao.impl;

import java.util.ArrayList;
import java.util.List;

import model.Comment;
import model.Praise;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.PraiseDao;

public class PraiseDaoImpl extends HibernateDaoSupport implements
		PraiseDao {

	public Integer save(Praise Praise) {
		return (Integer) getHibernateTemplate().save(Praise);
	}

	public void delete(Praise Praise) {
		getHibernateTemplate().delete(Praise);
	}

	public void update(Praise Praise) {
		getHibernateTemplate().merge(Praise);
	}

	public List<Praise> getPraiseByUsername(String Username) {
		@SuppressWarnings("unchecked")
		List<Praise> Praises = (List<Praise>) getHibernateTemplate()
				.find("from Praise as oi where oi.username=?", Username);
		 
		return Praises;
	}

	public List<Praise> getAllPraises() {
		@SuppressWarnings("unchecked")
		List<Praise> Praises = (List<Praise>) getHibernateTemplate()
				.find("from Praise");
		return Praises;
	}
	public List<Praise>  getPraisesByPid(int pid) {
		@SuppressWarnings("unchecked")
		List<Praise> Praises = (List<Praise>) getHibernateTemplate()
		.find("from Praise as oi where oi.pid=?", pid);
		return Praises;
	}
	
	public List<Integer> findBest( ) {
		List<Integer> a = new ArrayList<Integer>()  ;
		List list  = getHibernateTemplate()
		.find("select distinct p.pid, count(*) as g   from Praise as p where p.pid<>0 group by p.pid order by g desc  ");
		if(list != null && list.size() > 0) {
		     for(int i = 0 ; (i < list.size())||(i<5) ; i++) {
		          //查询结果返回的是一个包含对象数组的list。
		           Object[] obj = (Object[]) list.get(i);
		           Integer name =(Integer) obj[0];
		           Long age =   (Long) obj[1];
		           System.out.print("asda"+age);
		           a.add(name);
		       }
		}
		
		return a;
	
	}
	
	public List<Praise>  getPraisesByRid(int rid) {
		@SuppressWarnings("unchecked")
		List<Praise> Praises = (List<Praise>) getHibernateTemplate()
		.find("from Praise as oi where oi.rid=?", rid);
		return Praises;
	}	
}
