package com.ice.showdata.user.service;

import java.util.List;

import com.ice.showdata.user.bean.User;

/**
 * @author ice
 * 
 */
public interface IUserService {

	/**
	 * 保存更新用户
	 * 
	 * @param user
	 *            用户实例
	 */
	public void saveOrUpdateUser(User user);

	/**
	 * 获取用户列表
	 * 
	 * @param page
	 *            页数
	 * @param pageSize
	 *            页数大小
	 * @return 用户列表
	 */
	public List<User> getUserList(int page, int pageSize);

	/**
	 * 删除用户
	 * 
	 * @param user
	 *            用户
	 * @return 用户列表
	 */
	public void deleteUser(User user);

	/**
	 * 事务测试方法
	 * 
	 * @return 用户列表
	 */
	public List<User> transactionTest();
}
