package com.waes.assignment.repositories;

import com.waes.assignment.model.persistence.DiffRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiffRequestRepository extends JpaRepository<DiffRequest, Integer> {
}
