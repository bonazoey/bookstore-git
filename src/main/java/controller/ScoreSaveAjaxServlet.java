package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import book.BookscoreDAO;
import book.BookscoreVO;

/**
 * Servlet implementation class ScoreSaveServlet
 */
@WebServlet("/scoreSaveAjax")
public class ScoreSaveAjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// encoding
		request.setCharacterEncoding("utf-8");
		// 파라메타 값 받기
		String id = request.getParameter("id");
		if (id == null || id == "")
			id = "ezen11";
		String strbno = request.getParameter("bno");
		System.out.println("bno:" + strbno);
		int bno = 39;
		if (strbno != null && strbno != "")
			bno = Integer.parseInt(strbno);
		String strscore = request.getParameter("score").trim();
		double score = 0;
		if (strscore != null && strscore != "")
			score = Double.parseDouble(strscore);
		String cmt = request.getParameter("cmt");
		// dao 객체 생성
		BookscoreDAO dao = BookscoreDAO.getInstance();
		// vo 객체 생성
		BookscoreVO vo = new BookscoreVO(0, bno, id, score, cmt, null);
		// dao 삽입 메소드 수행
		int result = dao.insertBookscore(vo);
		// 페이지 이동 필요 없음, 비동기 통신 메소드를 다 수행하고나면 불렀던 곳으로 돌아가서 callback 수행
		ArrayList<Number> scoreS = dao.getAvgScore(bno);
		// json data로 보내주기
		JsonObject ob = new JsonObject();
		ob.addProperty("avgScore", scoreS.get(0));
		ob.addProperty("cntScore", scoreS.get(1));
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(ob);
		out.flush();
		out.close();

	}
}
