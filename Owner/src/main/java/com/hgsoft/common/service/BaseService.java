package com.hgsoft.common.service;



import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
import com.hgsoft.common.dao.BaseDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseService<T extends Serializable> {
	
	private BaseDao<T> dao;
	
	public void save(T entity){
		dao.save(entity);
	}
	
	public void saveOrUpdate(T entity){
		dao.saveOrUpdate(entity);
	}
	
	public void update(Object entity) {
		dao.update(entity);
	}
	
	public void delete(Object entity) {
		dao.delete(entity);
	}
	
	public void deleteById(Serializable id) {
		dao.delete(find(id));
	}

	public T find(Serializable id) {
		T entity = (T)dao.find(id);
		return entity;
	}
	
	/**
	 * 修改人 fdf
	 * 修改原因 dao未初始化，报NullPointException
	 */
	public List<T> findAll(){
//		return dao.findAll(Order.asc("id"));
		return getDao().findAll(Order.asc("id"));
	}
	
	public List<T> findByPager(Pager pager){
		return getDao().findByPager(pager, Order.desc("id"));
	}
	
	public List<T> findByPager(Pager pager,Order order){
		return getDao().findByPager(pager, order);
	}
	/**
	 * 多个order排序
	 * @param pager
	 * @param orders
	 * @return 
	 * liujialin
	 */
	public List<T> findByPager(Pager pager,Order... orders){
		List<Order> list = new ArrayList<Order>();
		for (Order order : orders) {
			list.add(order);
		}
		Order[] orderArr = new Order[list.size()];
		list.toArray(orderArr);
		return getDao().findByPager(pager, orderArr);
	}
	
	public List<T> findByPager(Pager pager, Order order, Property... propertys) {
		return  getDao().findByPager(pager, new Order[] { order }, propertys);
	}
	
	public List<?> findByPager(String hql,Pager pager)
	{
		return dao.findByHql(hql,pager);
	}
	
	public BaseDao<T> getDao() {
		return dao;
	}

	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List query(Pager pager, Object object,Map dateMap) {
		return getDao().query(pager,object,dateMap);
	}
	
	public void updateByHql(String hql){
		getDao().updateByHql(hql);
	}
	
	public T get(Serializable id) {
		T entity = (T)dao.get(id);
		return entity;
	}
	
}
