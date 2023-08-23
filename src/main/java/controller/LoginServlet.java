package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.MemberDAO;
import member.MemberVO;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("member/login.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// encoding x
		// 파라미터 받기
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		// dao 객체 생성
		MemberDAO dao = MemberDAO.getInstance();
		// dao 메소드 수행 결과 받기
		MemberVO mvo = dao.login(id, pwd);
		if (mvo != null) { // 로그인 성공
			// 세션 저장
			HttpSession session = request.getSession();
			// 페이지에서 필요로 하는 정보 저장
			session.setAttribute("mvo", mvo);
			// 페이지 이동 : list
			RequestDispatcher rd = request.getRequestDispatcher("bList");
			rd.forward(request, response);
		} else { // 로그인 실패
			request.setAttribute("message", "로그인 실패, 당신 인생도 실패!");
			RequestDispatcher rd = request.getRequestDispatcher("/member/login.jsp");
			rd.forward(request, response);
		}		
	}

}
