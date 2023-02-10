package ru.iteco.fmh.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.iteco.fmh.model.document.Document;
import ru.iteco.fmh.model.document.DocumentStatus;

import java.util.Collection;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Page<Document> findAllByStatusIn(Collection<DocumentStatus> statuses, Pageable pageable);
    List<Document> findAllByStatusIn(Collection<DocumentStatus> statuses);

    @Query("select p from Document p where p.name = :name and p.id <> :id")
    Document findDuplicateDocumentByName(@Param("name") String name, @Param("id") int id);
}
