package com.fastdev.example.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import com.fastdev.core.transaction.TransactionManager;
import com.fastdev.example.dao.BookDao;
import com.fastdev.example.entity.Book;

@Singleton
public class BookDaoImpl implements BookDao{
	
	@Inject
	@Named("centralTrx")
	private TransactionManager transactionManager;

	@Override
	public int createBook(Book book) {
		
		try ( PreparedStatement statement = transactionManager.getConnection().prepareStatement("INSERT INTO Book ( name ) VALUES (?)",PreparedStatement.RETURN_GENERATED_KEYS); ){
			statement.setString(1, book.getName());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
