package com.cronos.cronosmanager.repository.base;

import com.cronos.cronosmanager.model.base.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SoftDeletableRepository<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID> {

    String SOFT_DELETE_CLAUSE = "where e.status <> 'DELETED'";

    @Override
    @Query("select e from #{#entityName} e " + SOFT_DELETE_CLAUSE)
    List<T> findAll();

    @Override
    @Query("select e from #{#entityName} e " + SOFT_DELETE_CLAUSE)
    List<T> findAll(Sort sort);

    @Override
    @Query(value = "select e from #{#entityName} e " + SOFT_DELETE_CLAUSE, countQuery = "select count(e) from #{#entityName} e " + SOFT_DELETE_CLAUSE)
    Page<T> findAll(Pageable pageable);

    @Query("select e from #{#entityName} e where e.id = ?1 and e.status <> 'DELETED'")
    Optional<T> findById(ID id);

    @Query("select count(e) > 0 from #{#entityName} e where e.id = ?1 and e.status <> 'DELETED'")
    boolean existsById(ID id);

    @Override
    @Query("select count(e) from #{#entityName} e " + SOFT_DELETE_CLAUSE)
    long count();
}
