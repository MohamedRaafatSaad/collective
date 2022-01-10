package com.collective.myapp.repository;

import com.collective.myapp.domain.Plants;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plants entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantsRepository extends JpaRepository<Plants, Long>,JpaSpecificationExecutor<Plants> {
	
    @Query("SELECT COUNT(*) FROM Plants u WHERE u.pname like %:pname%")
    long countByName(@Param("pname") String pname);

}
