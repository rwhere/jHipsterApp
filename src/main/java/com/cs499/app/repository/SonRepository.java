package com.cs499.app.repository;

import com.cs499.app.domain.Son;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Son entity.
 */
@SuppressWarnings("unused")
public interface SonRepository extends JpaRepository<Son,Long> {

}
