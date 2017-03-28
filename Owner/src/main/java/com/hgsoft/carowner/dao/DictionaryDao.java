package com.hgsoft.carowner.dao;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.dao.BaseDao;
@Repository
public class DictionaryDao extends BaseDao<Dictionary> {
	
		//根据code获取唯一的字典记录
		public Dictionary getDicByCode(String code) {
			Dictionary dictionary = null;
			if (code != null && !"".equals(code)) {
				dictionary = (Dictionary) uniqueResult("from Dictionary d where d.code='" + code + "'");
			}
			return dictionary;
		}
		//根据code获取唯一的字典记录
		public Dictionary getDicByCodeAndType(String code,String type) {
			Dictionary dictionary = null;
			if (code != null && !"".equals(code)) {
				dictionary = (Dictionary) uniqueResult("from Dictionary d where d.code='" + code + "' and type ='"+type+"'");
			}
			return dictionary;
		}

}
