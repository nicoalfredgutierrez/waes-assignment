package com.waes.assignment.repositories;

import com.waes.assignment.model.Diff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiffRepository extends JpaRepository<Diff, Integer> {
}
