package com.fastdev.example.service.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fastdev.core.transaction.TransactionManager;
import com.fastdev.example.dao.BookDao;
import com.fastdev.example.entity.Book;
import com.fastdev.example.service.BookService;

@Singleton
public class BookServiceImpl implements BookService{
	
	@Inject
	@Named("centralTrx")
	private TransactionManager transactionManager;
	
	@Inject
	private BookDao bookDao;

	@Override
	public void createBook(Book book) {
		
		transactionManager.doInTransaction(()->{
			book.setId(bookDao.createBook(book));
			return;
		});
		
	}

}
