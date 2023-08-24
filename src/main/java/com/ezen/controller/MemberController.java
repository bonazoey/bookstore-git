package com.ezen.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ezen.book.MemberService;

/**
 * Servlet implementation class MemberController
 */
@WebServlet("/member")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// encoding
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// MemberService 객체 생성 -> exec()
		String view = new MemberService(request, response).exec(); 
		if (view != null) {
			request.getRequestDispatcher(view).forward(request, response);
		}
	}

}
