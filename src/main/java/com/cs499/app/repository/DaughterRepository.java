package com.cs499.app.repository;

import com.cs499.app.domain.Daughter;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Daughter entity.
 */
@SuppressWarnings("unused")
public interface DaughterRepository extends JpaRepository<Daughter,Long> {

}
