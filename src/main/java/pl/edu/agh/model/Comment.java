package pl.edu.agh.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "comments", uniqueConstraints = @UniqueConstraint(columnNames = {"isbn", "date"}))
public class Comment {

	public Comment(Long bookId, Long userId, String content, Date date) {
		super();
		this.bookId = bookId;
		this.userId = userId;
		this.content = content;
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long bookId;
	private Long userId;
	@Column(columnDefinition = "TEXT")
	private String content;
	private Date date;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
