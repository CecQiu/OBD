/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.DictionaryDao;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;

/**
 * @author liujialin
 * @date 201580723
 */
@Service
public class DictionaryService extends BaseService<Dictionary> {

	@Resource
	public void setDao(DictionaryDao dictionaryDao){
		super.setDao(dictionaryDao);
	}
	
	public DictionaryDao getDictionaryDao() {
		return (DictionaryDao) this.getDao();
	}
	/**
	 * 根据编码和真实值获取显示值
	 * @return
	 */
	public List<Dictionary> getDicByCodeAndTrueValue(String code,String trueValue){
		List<Dictionary> list=this.getDao().findAll(Order.desc("createTime"), Property.eq("code", code),Property.eq("trueValue", trueValue));
		return list;
	}
	/**
	 * 根据编码和类别获取显示值
	 * @return
	 */
	public Dictionary getDicByCodeAndType(String code,String type){
		Dictionary dic=this.getDictionaryDao().getDicByCodeAndType(code, type);
		return dic;
	}
	/**
	 * 根据code获取集合
	 * @param dictionary
	 * @return
	 */
	public List<Dictionary> getDicByCode(String code){
		List<Dictionary> list=this.getDao().findAll(Order.desc("createTime"), Property.eq("code", code));
		return list;
	}
	/**
	 * 获取唯一条字典记录
	 * @param dictionary
	 * @return
	 */
	public Dictionary getOneDicByCode(String code){
		return getDictionaryDao().getDicByCode(code);
	}
	/**
	 * 根据code和type获取唯一条字典记录
	 * @param dictionary
	 * @return
	 */
	public Dictionary getOneDicByCodeAndType(String code,String type){
		return getDictionaryDao().getDicByCodeAndType(code, type);
	}
	
	/**
	 * 根据查询条件获取记录
	 * @param pager  分页条件
	 * @param dictionary   查询条件
	 * @param end    创建结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionary> getDicByEntity(Pager pager,Dictionary dictionary,Date end){
		String hql = "from Dictionary where 1=1";
		String count = "select count(1) ";
		if(dictionary!=null){
			if(null != dictionary.getCode() && !"".equals(dictionary.getCode()))
			{
				hql += " and code like '%"+dictionary.getCode().trim()+"%'";
			}
			if(null != dictionary.getShowValue() && !"".equals(dictionary.getShowValue()))
			{
				hql += " and showValue like '%"+dictionary.getShowValue().trim()+"%'";
			}
			if(null != dictionary.getTrueValue() && !"".equals(dictionary.getTrueValue()))
			{
				hql += " and trueValue = '"+dictionary.getTrueValue().trim()+"'";
			}
			if(null != dictionary.getCreateTime() && !"".equals(dictionary.getCreateTime()))
			{
				hql += " and createTime >= '"+dictionary.getCreateTime()+"'";
			}
			if(null != end)
			{
				hql += " and createTime <= '"+end+"'";
			}
			if(null != dictionary.getCreateUser() && !"".equals(dictionary.getCreateUser()))
			{
				hql += " and createUser = '"+dictionary.getCreateUser()+"'";
			}
			count = count+hql;
			List<Long> counts = (List<Long>)getDao().findByHql(count, null);
			Long totalSize = (counts == null || counts.isEmpty()) ? 0l : counts.get(0);
			pager.setTotalSize(totalSize);
			
			hql += "order by createTime desc";
		}
		return (List<Dictionary>)this.getDao().findByHql(hql, pager);
	}
}
