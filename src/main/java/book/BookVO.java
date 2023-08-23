package book;

import java.sql.Date;

public class BookVO {
	private int bno;
	private String title,writer;
	private int price;
	private String publisher;
	private Date regdate;
	private String content;
	private String srcfilename,savefilename,savepath;
	public BookVO() {
		super();
	}
	
	public BookVO(String title, String writer, int price, String publisher, String content, String srcfilename,
			String savefilename, String savepath) {
		super();
		this.title = title;
		this.writer = writer;
		this.price = price;
		this.publisher = publisher;
		this.content = content;
		this.srcfilename = srcfilename;
		this.savefilename = savefilename;
		this.savepath = savepath;
	}

	public BookVO(String title, String writer, int price, String publisher, String content) {
		super();
		this.title = title;
		this.writer = writer;
		this.price = price;
		this.publisher = publisher;
		this.content = content;
	}

	public BookVO(int bno, String title, String writer, int price, String publisher, Date regdate) {
		super();
		this.bno = bno;
		this.title = title;
		this.writer = writer;
		this.price = price;
		this.publisher = publisher;
		this.regdate = regdate;
	}
		
	public BookVO(int bno, String title, String writer, int price, String publisher, Date regdate, String content,
			String srcfilename, String savefilename, String savepath) {
		super();
		this.bno = bno;
		this.title = title;
		this.writer = writer;
		this.price = price;
		this.publisher = publisher;
		this.regdate = regdate;
		this.content = content;
		this.srcfilename = srcfilename;
		this.savefilename = savefilename;
		this.savepath = savepath;
	}

	public BookVO(int bno, String title, String writer, int price, String publisher, String content,
			String srcfilename, String savefilename, String savepath) {
		super();
		this.bno = bno;
		this.title = title;
		this.writer = writer;
		this.price = price;
		this.publisher = publisher;
		this.content = content;
		this.srcfilename = srcfilename;
		this.savefilename = savefilename;
		this.savepath = savepath;
	}


	public String getSrcfilename() {
		return srcfilename;
	}

	public void setSrcfilename(String srcfilename) {
		this.srcfilename = srcfilename;
	}

	public String getSavefilename() {
		return savefilename;
	}

	public void setSavefilename(String savefilename) {
		this.savefilename = savefilename;
	}

	public String getSavepath() {
		return savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	@Override
	public String toString() {
		return "BookVO [bno=" + bno + ", title=" + title + ", writer=" + writer + ", price=" + price + ", publisher="
				+ publisher + ", regdate=" + regdate + "]";
	}	
}
