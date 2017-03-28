package com.hgsoft.common.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcUtil {
	private JdbcTemplate jdbcTemplate;

	@Resource
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	//qinzuohai
	public void delete(String sql) {
		 jdbcTemplate.execute(sql);
	}
	
	//qinzuohai
	public void save(String sql) {
		 jdbcTemplate.execute(sql);
	}
	
	public int update(String sql) {
		return jdbcTemplate.update(sql);
	}
	
	public int select(String sql) {
		try {
			return jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			return 0;

		}

		
	}
	public String selectForString(String sql){
		try{
			return (String)jdbcTemplate.queryForObject(sql, String.class);
		}catch(IncorrectResultSizeDataAccessException e){
			return "";
			
		}
	}
	@SuppressWarnings("unchecked")
	public List selectForList(String sql) {
		List list = new ArrayList();
		try {
			list = jdbcTemplate.queryForList(sql);			
		} catch (Exception e) {
		}
		return list;
	}
	
	/**
	 * ����ִ��sql���
	 * 2013-12-23
	 * */
	public int[] batchUpdate(String[] sql) throws Exception{
		return jdbcTemplate.batchUpdate(sql);
	}
	

}
