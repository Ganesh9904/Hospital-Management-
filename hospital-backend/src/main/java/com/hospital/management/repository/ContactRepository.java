package com.hospital.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.management.entity.Contact;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
