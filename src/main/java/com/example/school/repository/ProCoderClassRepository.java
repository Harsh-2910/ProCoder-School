package com.example.school.repository;

import com.example.school.model.ProCoderClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProCoderClassRepository extends JpaRepository<ProCoderClass, Integer> {
}
