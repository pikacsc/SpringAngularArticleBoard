package com.articles.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.articles.entity.Article;
import com.articles.entity.ArticleRowMapper;

@Transactional
@Repository
public class ArticleDAOImpl implements ArticleDAO{

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Article getArticleById(int articleId) {
		String sql = " SELECT articleId, title, category, contents FROM articles WHERE articleId= ?";
		RowMapper<Article> rowMapper = new BeanPropertyRowMapper<Article>(Article.class);
		Article article = jdbcTemplate.queryForObject(sql, rowMapper, articleId);
		return article;
	}
	
	@Override
	public List<Article> getAllArticles() {
		String sql = "SELECT articleId, title, category FROM articles";
		RowMapper<Article> rowMapper = new ArticleRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}


	@Override
	public void addArticle(Article article) {
		//Add article
		String sql = "INSERT INTO articles (articleId, title, category, contents) values (?,?,?,?)";
		jdbcTemplate.update(sql, article.getArticleId(), article.getTitle(), article.getCategory(), article.getContents());
		
		//Fetch article id
		sql = "SELECT articleId FROM articles WHERE title=? and category=?";
		int articleId = jdbcTemplate.queryForObject(sql, Integer.class, article.getTitle(), article.getCategory());
		
		//Set article id
		article.setArticleId(articleId);
	}

	@Override
	public void updateArticle(Article article) {
		String sql ="UPDATE articles SET title=?, category=?, contents=? WHERE articleId=?";
		jdbcTemplate.update(sql, article.getTitle(), article.getCategory(), article.getContents(), article.getArticleId());
	}

	@Override
	public void deleteArticle(int articleId) {
		String sql = "DELETE FROM articles WHERE articlesId=?";
		jdbcTemplate.update(sql, articleId);
	}

	@Override
	public boolean articleExists(String title, String category) {
		String sql = "SELECT count(*) FROM articles WHERE title=? and category=?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, title, category);
		if(count == 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
