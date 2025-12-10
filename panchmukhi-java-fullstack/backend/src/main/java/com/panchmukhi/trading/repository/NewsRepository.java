package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
    
    List<News> findByLanguageOrderByPublishedAtDesc(String language);
    
    List<News> findBySourceOrderByPublishedAtDesc(String source);
    
    List<News> findByCategoryOrderByPublishedAtDesc(String category);
    
    Page<News> findByLanguage(String language, Pageable pageable);
    
    @Query("SELECT n FROM News n WHERE n.sentimentScore > :minScore ORDER BY n.sentimentScore DESC")
    List<News> findPositiveNews(@Param("minScore") BigDecimal minScore);
    
    @Query("SELECT n FROM News n WHERE n.sentimentScore < :maxScore ORDER BY n.sentimentScore ASC")
    List<News> findNegativeNews(@Param("maxScore") BigDecimal maxScore);
    
    @Query("SELECT n FROM News n WHERE n.publishedAt > :since ORDER BY n.publishedAt DESC")
    List<News> findRecentNews(@Param("since") LocalDateTime since);
    
    @Query("SELECT n FROM News n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword% ORDER BY n.publishedAt DESC")
    List<News> searchByKeyword(@Param("keyword") String keyword);
    
    @Query(value = "SELECT * FROM news WHERE MATCH(title, content) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE) ORDER BY published_at DESC", nativeQuery = true)
    List<News> fullTextSearch(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT n FROM News n WHERE n.relevanceScore > :minRelevance ORDER BY n.relevanceScore DESC, n.publishedAt DESC")
    List<News> findRelevantNews(@Param("minRelevance") BigDecimal minRelevance);
    
    @Query("SELECT DISTINCT n.source FROM News n ORDER BY n.source")
    List<String> findDistinctSources();
    
    @Query("SELECT DISTINCT n.category FROM News n ORDER BY n.category")
    List<String> findDistinctCategories();
}