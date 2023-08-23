package com.ezen.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ezen.book.BookDAO;
import com.ezen.book.BookVO;
import com.ezen.book.BookscoreDAO;
import com.ezen.util.PageVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class BookService {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final String path = "/WEB-INF/views/book/";
	private String uploadPath = "d:/upload/img";

	public BookService(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public String exec() throws IOException {
		String cmd = request.getParameter("cmd");
		String view = null;
		if (cmd == null || cmd.equals("list")) {
			return BookListService();
		} else if (cmd.equals("view")) {
			return BookViewService();
		} else if (cmd.equals("new")) {
			return BookNewService();
		} else if (cmd.equals("edit")) {
			return BookUpdateService();
		} else if (cmd.equals("delete")) {
			return BookDeleteService();
		} else if (cmd.equals("imgDown")) {
			return BookImgDownService();
		} 
		return view;
	}

	private String BookImgDownService() throws IOException {
		String upload = request.getParameter("upload"); // 경로포함 다운로드 파일명
		String originFname = request.getParameter("originFname"); // 원본 파일명
		String savedFname = request.getParameter("savedFname");
		String agent = request.getHeader("User-Agent");
		String filename = upload+"/"+savedFname;
		System.out.println(agent);
		boolean ieBrowser = (agent.indexOf("Trident") > -1) || (agent.indexOf("Edge") > -1);
		if (ieBrowser) {
			originFname = URLEncoder.encode(originFname, "utf-8").replace("\\", "%20");
		} else { // firefox, edge, chrome
			originFname = new String(originFname.getBytes("utf-8"), "iso-8859-1");
		}
		response.setContentType("image/jpg");
		// 다운로드 되는 파일명 설정
		response.setHeader("Content-Disposition", "attachment;filename = " + originFname);
		FileInputStream in = new FileInputStream(filename); // 파일 open
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		int numRead;
		byte b[] = new byte[4096]; //4K만큼
		while ((numRead = in.read(b, 0, b.length)) != -1) {
			out.write(b, 0, numRead);
		}
		out.flush(); // 버퍼에 남은 것 출력
		in.close();
		out.close();
		return null;
	}

	private String BookDeleteService() {
		int bno = Integer.parseInt(request.getParameter("bno"));
		BookDAO dao = BookDAO.getInstance();
		int result = dao.deleteBook(bno);
		if (result == 1) {
			return "book?cmd=list";
		}
		return null;
	}

	private String BookUpdateService() throws IOException {
		// mr 객체만 생성하면 mr안에 모든 넘어온 파라메타가 다 저정되고, 업로드 파일이 있으면 파일 저장폴더에 저장 된다
		MultipartRequest mr = new MultipartRequest(request, uploadPath, 2 * 1024 * 1024, "utf-8",
				new DefaultFileRenamePolicy());
		// 넘어오는 데이터를 받아서 수정작업
		// MultipartRequest 사용
		// 첨부파일 있는지 없는지 확인?? 있다면 수정, 없다면 수정 x
		if (mr != null) {
			// System.out.println("mr.getOriginalFileName="+mr.getOriginalFileName("file"));
			// System.out.println("mr.getFilesystemName="+mr.getFilesystemName("file"));
			// BookVO 넘어온 데이터를 담고 수정작업
			// 넘어온 데이터 받기
			// bno 받기
			System.out.println("bno=" + mr.getParameter("bno"));

			int bno = Integer.parseInt(mr.getParameter("bno"));
			String title = mr.getParameter("title").trim();
			String writer = mr.getParameter("writer").trim();
			String publisher = mr.getParameter("publisher").trim();
			String content = mr.getParameter("content");
			System.out.println(content);
			int price = Integer.parseInt(mr.getParameter("price"));
			String oFileName = mr.getOriginalFileName("file");
			if (oFileName == null)
				uploadPath = null;
			BookVO vo = new BookVO(bno, title, writer, price, publisher, content, oFileName,
					mr.getFilesystemName("file"), uploadPath);
			// dao 객체 만들기
			BookDAO dao = BookDAO.getInstance();
			// dao 수정 메소드 호출
			int result = dao.updateBook(vo);
			System.out.println(result);
			if (result == 1) {
				return "book?cmd=view&bno=" + bno;
			}
		}
		return null;
	}

	private String BookListService() {
		BookDAO dao = BookDAO.getInstance();
		// List<BookVO> list=dao.getBookList();// 전체 행을 출력
		// 페이징을 위한 변수 선언
		// parmaPage : 파라메타(?뒤에 ?page=${i})---현재 페이지 설정
		String parmaPage = request.getParameter("page");
		int page = 0;
		if (parmaPage == null)
			page = 1;
		else
			page = Integer.parseInt(parmaPage);

		int displayRow = 5;
		int displayPage = 5;
		int rowCnt = 0;
		List<BookVO> list = null;

		// 검색어 관련 파라메타 받아서 저장
		String searchtype = request.getParameter("searchtype");
		String searchword = request.getParameter("searchword");
		;
		System.out.println("searchtype=" + searchtype);
		if (searchword == null || searchword.equals("")) {
			list = dao.getBookList(page, displayRow);
			rowCnt = dao.getRowConut();// booktbl 전체 행의 개수
		} else {// 검색어 있음
				// searchtype=
			list = dao.getBookList(page, displayRow, searchtype, searchword);
			rowCnt = dao.getRowConut(searchtype, searchword);// booktbl 해당검색어가 있는 전체 행의 개수
		}

		// PageVO 객체 생성
		PageVO pVo = new PageVO(page, rowCnt, displayRow, displayPage);
		pVo.setSearchword(searchword);// 검색어를 PageVO 객체에 저장
		pVo.setSearchtype(searchtype);
		// pVo.setPage(page);//page설정
		// pVo.setPage(6);
		// pVo.setTotalCount(rowCnt);
		// 브라우저에서 접근가능한 객체에 list 저장
		request.setAttribute("list", list);
		request.setAttribute("pVo", pVo);
		return path + "booklist.jsp";
	}

	private String BookViewService() {
		BookDAO dao = BookDAO.getInstance();
		int bno = Integer.parseInt(request.getParameter("bno"));
		BookVO vo = dao.getBook(bno);
		int page = 0;
		if (request.getParameter("page") == null) {
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
		}
		return path + "bookView.jsp";
	}

	private String BookNewService() throws IOException {
		String method = request.getMethod().toLowerCase();
		if (method.equals("get")) {
			return path + "bookNew.jsp";
		} else {
			File imgPath = new File(uploadPath);
			if (!imgPath.exists()) {
				imgPath.mkdirs();
			}
			MultipartRequest mr;
			mr = new MultipartRequest(request, uploadPath, 10 * 1024 * 1024, "utf-8", new DefaultFileRenamePolicy());
			if (mr != null) {
				String title = mr.getParameter("title").trim();
				String writer = mr.getParameter("writer").trim();
				String publisher = mr.getParameter("publisher").trim();
				String content = mr.getParameter("content").trim();
				int price = Integer.parseInt(mr.getParameter("price").trim());
				BookVO vo = new BookVO(title, writer, price, publisher, content, mr.getOriginalFileName("file"),
						mr.getFilesystemName("file"), uploadPath);

				// dao 객체 생성
				BookDAO dao = BookDAO.getInstance();
				// dao 객체 insertBook 메소드 수행
				int result = dao.insertBook(vo);
				if (result == 1) {// 삽입완료 - 전체목록 이동
					return "book?cmd=list";
				}
			}
			return path + "bookNew.jsp";
		}
	}
}
