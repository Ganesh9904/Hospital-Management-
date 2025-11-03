package com.hospital.management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.management.dto.ContactDTO;
import com.hospital.management.service.ContactService;
@RestController
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @PostMapping("/submit")
    public ResponseEntity<String> submitContactMessage(@RequestBody ContactDTO contactDTO) {
        Optional<ContactDTO> saved = contactService.saveContactMessage(contactDTO);
        if (saved.isPresent()) {
            return ResponseEntity.ok("Thank you for your message! We will get back to you soon.");
        } else {
            return ResponseEntity.badRequest().body("Failed to submit message. Please try again.");
        }
    }
    @GetMapping("/messages")
    public ResponseEntity<List<ContactDTO>> getAllContactMessages() {
        List<ContactDTO> messages = contactService.getAllContactMessages();
        return ResponseEntity.ok(messages);
    }
}
