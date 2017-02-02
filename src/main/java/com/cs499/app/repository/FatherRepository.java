package com.cs499.app.repository;

import com.cs499.app.domain.Father;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Father entity.
 */
@SuppressWarnings("unused")
public interface FatherRepository extends JpaRepository<Father,Long> {

}
