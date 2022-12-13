package com.example.CRUDAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CRUDAPI.model.BooksModel;

public interface BooksRepo extends JpaRepository<BooksModel, Long> {
	  List<BooksModel> findByPublished(boolean published);
	  List<BooksModel> findByTitleContaining(String title);
	}