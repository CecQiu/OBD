package com.hgsoft.common.dao;


import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.Projections;

import javax.annotation.Resource;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class BaseDao<T extends Serializable> {

	@Resource
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private Class<T> className;

	private Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[0];

	public void save(T entity) {
		getSession().save(entity);
	}

	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
		getSession().flush();
	}

	public void update(Object entity) {
		getSession().merge(entity);
		getSession().flush();
	}

	public void delete(Object entity) {
		getSession().delete(entity);
		getSession().flush();
	}

	public T find(Serializable id) {
		T entity = (T) getSession().get(clazz, id);
		return entity;
	}

	public List<?> findBySql(String sql, Pager pager) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (pager != null) {
			query.setFirstResult((pager.getCurrentPage() - 1)
					* pager.getPageSize());
			query.setMaxResults(pager.getPageSize());
		}
		return query.list();
	}
	
	public List<T> findBySql(String sql, Pager pager,boolean hasEntity) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (pager != null) {
			query.setFirstResult((pager.getCurrentPage() - 1)
					* pager.getPageSize());
			query.setMaxResults(pager.getPageSize());
		}
		if(hasEntity){
			query.addEntity(clazz);
		}
		return query.list();
	}
	
	/**
	 * 根据原生sql查询结果对象
	 * @param sql
	 * @return
	 * liujialin
	 */
	public Object findBySql(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.uniqueResult();
	}

	public List<?> findByHql(String hql, Pager pager) {
		Query query = getSession().createQuery(hql);
		
		if (pager != null) {
			query.setFirstResult((pager.getCurrentPage() - 1)
					* pager.getPageSize());
			query.setMaxResults(pager.getPageSize());
		}
		return query.list();
	}
	
	public List<T> findByHql2(String hql, Pager pager, Object... values) {
		Query query = getSession().createQuery(hql);
		
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		if (pager != null) {
			List<T> tempList = query.list();
			pager.setTotalSize(tempList != null ? tempList.size() : 0);
			query.setFirstResult((pager.getCurrentPage() - 1)
					* pager.getPageSize());
			query.setMaxResults(pager.getPageSize());
		}
		return query.list();
	}

	public List<T> findAll(Order order, Property... propertys) {
		return findByPager(null, new Order[] { order }, propertys);
	}

	public List<T> findAll(Order[] orders, Property... propertys) {
		return findByPager(null, orders, propertys);
	}

	public List<T> findByPager(Pager pager, Order order, Property... propertys) {
		return findByPager(pager, new Order[] { order }, propertys);
	}

	@SuppressWarnings("rawtypes")
	public List<T> findByPager(Pager pager, Order[] orders,
			Property... propertys) {
		Criteria criteria = getSession().createCriteria(clazz);
		// 查询条件
		for (Property property : propertys) {
			if (property != null)
				criteria.add(property.getCriterion());
		}
		// 分页
		if (pager != null) {
			criteria.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();

			if (count == null)
				return new ArrayList();
			pager.setTotalSize(count);
			criteria.setProjection(null);

			criteria.setFirstResult((pager.getCurrentPage() - 1)
					* pager.getPageSize());
			criteria.setMaxResults(pager.getPageSize());

		}
		// 排序
		if (orders != null && orders.length > 0) {
			for (Order order : orders) {
				if (order != null)
					criteria.addOrder(order.getHibernateOrder());
			}
		}
		List list = criteria.list();
		List list2 = new LinkedList();

		if (list != null && !list.isEmpty()) {
			Object[] objects = null;
			try {
				objects = (Object[]) list.get(0);
			} catch (Exception e) {
				return list;
			}
			int i = objects.length - 1;
			if (i > 0) {
				for (Object object : list) {
					Object[] objects2 = (Object[]) object;
					list2.add(objects2[i]);
				}
			}

		}
		return list2;
	}

	public Integer countByProperty(String propertyName, Property... propertys) {
		Criteria criteria = getSession().createCriteria(clazz);
		// 查询条件
		for (Property property : propertys) {
			if (property != null)
				criteria.add(property.getCriterion());
		}
		criteria.setProjection(Projections.count(propertyName));
		return Integer.parseInt(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("rawtypes")
	protected List executeQuery(String hql, Object... values) {
		return executeQuery(null, hql, values);
	}

	@SuppressWarnings("rawtypes")
	public List queryByHQL(String hql, Object... values) {
		return executeQuery(hql, values);
	}

	@SuppressWarnings("rawtypes")
	public List queryByHQL(Pager pager, String hql, Object... values) {
		if(pager!=null){
			long count = executeCount(hql, values);
			pager.setTotalSize(count);
		}
		return executeQuery(pager, hql, values);
	}

	@SuppressWarnings("rawtypes")
	public List queryByHQL(Pager pager, String hql, String order,Object... values) {
		if(pager!=null){
			long count = executeCount(hql, values);
			pager.setTotalSize(count);
		}
		if (StringUtils.isNotBlank(order)) {
			hql += order;
		}
		return executeQuery(pager, hql, values);
	}

	@SuppressWarnings("rawtypes")
	public List queryBySQL(String sql, Object... values) {

		Query query = getSession().createSQLQuery(sql);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query.list();
	}

	@SuppressWarnings("rawtypes")
	protected List executeQuery(Pager pager, String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		if (pager != null) {
			query.setFirstResult((pager.getCurrentPage() - 1)
					* pager.getPageSize());
			query.setMaxResults(pager.getPageSize());
		}
		return query.list();
	}

	protected Long executeCount(String hql, Object... values) {
		Query query = getSession().createQuery("select count(*) " + hql);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return (Long) query.uniqueResult();
	}

	/**
	 * 根据赋给对象的属性值进行查询
	 * 
	 * @param pager
	 * @param object
	 *            要查询的对象
	 * @param dateMap
	 *            不需要查询时间时该属性可设置为空
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query(Pager pager, Object object, Map dateMap) {
		StringBuffer sb = new StringBuffer();
		StringBuffer name = new StringBuffer("obj");
		String hql = getQueryString(object, name, sb, 0, dateMap);
		if (hql == null)
			// hql="from "+className.substring(className.lastIndexOf(".")+1);
			hql = "from " + clazz.getName();
		long count = executeCount(hql);
		pager.setTotalSize(count);
		hql = hql.concat(" order by id desc");
		// System.out.println(hql);
		return executeQuery(pager, hql);
	}

	/**
	 * 当需要对某一时间属性查询时，只要设置一个不为空任意值
	 * 
	 * @param obj
	 *            需要进行查询的对像
	 * @param name
	 *            类名字符串如（student.card.) 给hql的第一层的类任取自己名字，如类Student则将可取为student
	 * @param sb
	 *            传入一个空的StringBuffer
	 * @param control
	 *            控制符,设置为零传入
	 * @param dateMap
	 *            按时间属性名，传入时间范围值 如 birthDay 则
	 *            dateMap.put("birthDay1",date1);dateMap.put("birthDay2",date2);
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getQueryString(Object obj, StringBuffer name,
			StringBuffer sb, int control, Map dateMap) {
		if (obj == null)
			return null;
		List list = new ArrayList();
		list.add("java.lang.Integer");
		list.add("java.lang.Object");
		list.add("java.lang.String");
		list.add("java.lang.Float");
		list.add("java.lang.Double");
		list.add("java.lang.Byte");
		list.add("java.lang.Char");
		list.add("java.lang.Long");
		list.add("java.lang.Boolean");
		list.add("java.lang.Short");
		list.add("java.util.Date");
		list.add("java.sql.Date");
		String str = null;
		Class c = obj.getClass();
		if (control == 0) {// 如果control等于0则执行该属性
			sb.append("from " + c.getName() + " " + name.toString() + " where");
			str = sb.toString();
		}
		Field[] fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			try {
				Object val = f.get(obj);
				if (val != null && !"".equals(val)) {// 如果值为空则跳过
					if (list.contains(val.getClass().getName())) {// 基本属性则依次加入
						sb.append(" " + name.toString() + "." + f.getName()
								+ "='" + val + "' and");
					} else {// 如果是对象，则递归调用
						name.append("." + f.getName());
						getQueryString(val, name, sb, 1, dateMap);
						name.replace(
								name.length() - (f.getName().length() + 1),
								name.length(), "");
					}
				} else if ("java.util.Date".equals(f.getGenericType()
						.toString().substring(6))
						|| "java.sql.Date".equals(f.getGenericType().toString()
								.substring(6))) {// 如果值为空，则判断是否是时间对象
					if (dateMap != null) {
						DateFormat sf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date lDate = (Date) dateMap.get(f.getName() + "1");
						Date hDate = (Date) dateMap.get(f.getName() + "2");
						if (lDate != null) {
							sb.append(" " + name.toString() + "." + f.getName()
									+ ">='" + sf.format(lDate) + "' and");
						}
						if (hDate != null) {
							sb.append(" " + name.toString() + "." + f.getName()
									+ "<='" + sf.format(hDate) + "' and");
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (control == 0) {// 判断是最外层的query函数时，对最终的字符串进行判断处理
			if (sb.toString().equals(str))
				return sb.replace(sb.length() - 6, sb.length(), "").toString();
			sb.replace(sb.length() - 4, sb.length(), "");

		}
		return sb.toString();
	}

	public List<T> query(Pager pager, String tableName, String[] projection,
			String[] selection, String[] selectionArg, String order) {
		String OBJ = "obj.";
		StringBuffer hql = new StringBuffer();

		if (projection != null) {
			hql.append("select ");
			for (int i = 0; i < projection.length; i++) {
				hql.append(OBJ + projection[i] + ",");
			}
			hql.replace(hql.length() - 1, hql.length(), " ");
		}

		hql.append("from " + tableName + " obj");

		if (selection != null) {
			hql.append(" where (1=1 ");
			for (int j = 0; j < selection.length; j++) {
				if (!selectionArg[j].equals("") && selectionArg[j] != null) {
					hql.append("AND " + OBJ + selection[j] + " = '"
							+ selectionArg[j] + "' ");
				}
			}
			hql.append(") ");
		}
		long count = executeCount(hql.toString());
		pager.setTotalSize(count);
		if (order != null) {
			hql.append("order by " + OBJ + order);
		}
		return executeQuery(pager, hql.toString());
	}

	protected T queryFirst(String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}
	
	public int executeCountQuery(String hql){
		String hqlCount = "select count(*)"+hql;
		return Integer.parseInt(getSession().createQuery(hqlCount).uniqueResult().toString());
	}
	
	public Object uniqueResult(String hql){
		return getSession().createQuery(hql).uniqueResult();
	}
	
	public void updateByHql(String hql){
		getSession().createQuery(hql).executeUpdate();
	}
	
	public T get(Serializable id) {
		T entity = (T)getSession().get(className, id);
		return entity;
	}

	/**
	 * 批量保存入库
	 * @param lists
	 */
	public void batchSave(List<T> lists) throws Exception{
		Session session = getSession();
		int count = 0;
		for (T t : lists) {
			session.save(t);
			if(++count % 50 == 0){
				session.flush();
				session.clear();
			}
		}
	}
}
