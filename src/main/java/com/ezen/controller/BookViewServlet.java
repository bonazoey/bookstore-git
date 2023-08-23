package com.ezen.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import com.ezen.book.BookDAO;
import com.ezen.book.BookVO;
import com.ezen.book.BookscoreDAO;
import com.ezen.util.PageVO;

/**
 * Servlet implementation class BookView
 */
@WebServlet("/bView")
public class BookViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookViewServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BookDAO dao = BookDAO.getInstance();
		int bno = Integer.parseInt(request.getParameter("bno"));
		BookVO vo = dao.getBook(bno);
		int page = 0;
			if (request.getParameter("page") == null ) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));
			}
		String searchtype = request.getParameter("searchtype");
		String searchword = request.getParameter("searchword");
		if (vo != null) {
			// 현 도서의 평균 평점을 조회해서 브라우저에서 접근 가능한 객체에 값을 저장 
			// BookscoreDAO 객체 필요
			BookscoreDAO sdao = BookscoreDAO.getInstance();
			ArrayList<Number> score = sdao.getAvgScore(bno);
			System.out.println();
			request.setAttribute("avgScore", score.get(0));
			request.setAttribute("cntScore", score.get(1));
			request.setAttribute("vo", vo);
			request.setAttribute("page", page);
			request.setAttribute("searchtype", searchtype);
			request.setAttribute("searchword", searchword);
			RequestDispatcher rd = request.getRequestDispatcher("/book/bookView.jsp");
			rd.forward(request, response);
		} else {
			
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
