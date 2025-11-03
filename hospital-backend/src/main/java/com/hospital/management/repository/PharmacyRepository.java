package com.hospital.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.management.entity.Pharmacy;
@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
