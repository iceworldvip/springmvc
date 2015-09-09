package com.ice.showdata.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ice.showdata.user.bean.User;
import com.ice.showdata.user.dao.UserDAO;

/**
 * @author ice
 * 
 */
public class UserServiceImpl implements IUserService {

	@Autowired
	private HibernateTransactionManager hibernateTransactionManager;

	/**
	 * 用户DAO
	 */
	@Autowired
	private UserDAO userDAO;

	@Override
	public void saveOrUpdateUser(User user) {
		userDAO.saveOrUpdate(user);
	}

	@Override
	public List<User> getUserList(int page, int pageSize) {
		List<User> list = userDAO.getUserList(page, pageSize);
		return list;
	}

	@Override
	public void deleteUser(User user) {
		userDAO.deleteUser(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ice.showdata.user.service.IUserService#transactionTest()
	 */
	@Override
	public List<User> transactionTest() {
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = hibernateTransactionManager.getTransaction(definition);
		List<User> list = userDAO.getUserList(1, 10);
		try {
			User user = new User();
			user.setTransaction(1);
			userDAO.saveOrUpdate(user);
			user = new User();
			user.setTransaction(2);
			userDAO.saveOrUpdate(user);
			hibernateTransactionManager.commit(status);
		} catch (Exception exception) {
			hibernateTransactionManager.rollback(status);
		}
		return list;
	}

}
