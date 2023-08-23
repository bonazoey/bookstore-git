package com.ezen.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.ezen.book.BookscoreDAO;
import com.ezen.book.BookscoreVO;
import com.ezen.util.PageVO;

/**
 * Servlet implementation class ScoreListAjaxServlet
 */
@WebServlet("/scoreListAjax")
public class ScoreListAjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("score/score_list_ajax.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("scoreListAjax post 메소드");
		// encoding : 2가지
		request.setCharacterEncoding("utf-8");
		// 파라메타 값 받기
		int bno = 39;
		String strbno = request.getParameter("bno").trim();
		if (strbno != null && strbno != "")
			bno = Integer.parseInt(strbno);
		int page = 1;
		String strpage = request.getParameter("page").trim();
		if (strpage != null && strpage != "")
			page = Integer.parseInt(strpage);
		// 페이지
		BookscoreDAO dao = BookscoreDAO.getInstance();
		// 특정 도서(bno값) 전체 개수 세기
		int rowCnt = dao.getRowCount(bno);
		// 다음페이지가 있을지??
		// int page, int totalCount, int displayRow, int displayPage
		int displayRow = 5;
		PageVO pvo = new PageVO(page, rowCnt, displayRow, 0);
		boolean next = pvo.nextPageScore();
		System.out.println("next=" + next);
		// 해당페이지 검색하기
		List<BookscoreVO> list = dao.getBookScore(page, bno, displayRow);
		// json 데이터를 조립해서 호출쪽으로 출력해 준다.
		// json데이터 조립 :gson 라이브러리 사용
		JsonObject jObj = new JsonObject();
		JsonArray arr = new JsonArray();
		if (list != null) {
			jObj.addProperty("next", next);// 더보기버튼 활성화
			// list를 json array로 만들기
			// json 객체 만들기
			JsonObject data = null;
			for (BookscoreVO vo : list) {
				data = new JsonObject();
				data.addProperty("score", vo.getScore());
				data.addProperty("id", vo.getId());
				data.addProperty("cmt", vo.getCmt());
				arr.add(data);
			}
		} else {
			
		}
		jObj.add("arr", arr);
		// Gson gson=new Gson();
		// System.out.println(gson.toJson(jObj));
		// 보내기 전에 encoding
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jObj);
		out.flush();
		out.close();
	}

}
