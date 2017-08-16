package com.tianjin.MobileInspection.databasetable;

import java.util.List;

interface TBase<T> {

	public static final String ID = "id";
	/**
	 * 创建表
	 */
	public void create();
	/**
	 * 插入表数据
	 * @param object
	 */
	public int insert(T object);
	/**
	 * 批量插入
	 * @param object
	 */
	public void insertList(List<T> object);
	
	/**
	 * 修改表数据
	 * @param object
	 */
	public void updata(T object);
	/**
	 * 删除表数据
	 * @param name
	 * @param values
	 */
	public void delete(String name,String values);
	
	/**
	 * 查询表数据
	 * @param param
	 * @return
	 */
	public List<T> query(Object... param);

	/**
	 * 清空表数据
	 */
	public void dropTable();
}
