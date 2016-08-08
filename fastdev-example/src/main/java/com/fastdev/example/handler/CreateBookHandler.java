package com.fastdev.example.handler;

import java.util.Map;
import javax.inject.Inject;

import com.fastdev.core.code.Params;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.OnCommand;
import com.fastdev.example.entity.Book;
import com.fastdev.example.service.BookService;

@OnCommand(name="createBook",rolesAllowed={"user"})
public class CreateBookHandler implements Handler{
	
	@Inject
	private BookService bookServce;

	@Override
	public Book call(Map<String, Object> params) {
		
		System.out.println(params.get(Params.SESSION));
		
		Book book = new Book();
		book.setName((String)params.get("name"));
		bookServce.createBook(book);
		return book;
	}

}
