package com.example.CRUDAPI.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.CRUDAPI.model.BooksModel;
import com.example.CRUDAPI.repository.BooksRepo;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")

public class BooksController {

  @Autowired
  BooksRepo booksRepo;

  @GetMapping("/books")
  public ResponseEntity<List<BooksModel>> getAllBooks(@RequestParam(required = false) String title) {
    try {
      List<BooksModel> bModel = new ArrayList<BooksModel>();

      if (title == null) {
        booksRepo.findAll().forEach(bModel::add);
      }
        else {
        booksRepo.findByTitleContaining(title).forEach(bModel::add);
        }
      if (bModel.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(bModel, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<BooksModel> getBooksById(@PathVariable("id") long id) {
    Optional<BooksModel> bookData = booksRepo.findById(id);

    if (bookData.isPresent()) {
      return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/books")
  public ResponseEntity<BooksModel> createBook(@RequestBody BooksModel booksModel) {
    try {
    	BooksModel _bModel = booksRepo
          .save(new BooksModel(booksModel.getTitle(), booksModel.getDes(), false));
      return new ResponseEntity<>(_bModel, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/books/{id}")
  public ResponseEntity<BooksModel> updateBook(@PathVariable("id") long id, @RequestBody BooksModel booksModel) {
    Optional<BooksModel> modelData = booksRepo.findById(id);

    if (modelData.isPresent()) {
    	BooksModel _bModel = modelData.get();
    	_bModel.setTitle(booksModel.getTitle());
    	_bModel.setDes(booksModel.getDes());
    	_bModel.setPublished(booksModel.isPublished());
      return new ResponseEntity<>(booksRepo.save(_bModel), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/books/{id}")
  public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
    try {
      booksRepo.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/books")
  public ResponseEntity<HttpStatus> deleteAllBooks() {
    try {
      booksRepo.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/books/published")
  public ResponseEntity<List<BooksModel>> findByPublished() {
    try {
      List<BooksModel> tutorials = booksRepo.findByPublished(true);

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}