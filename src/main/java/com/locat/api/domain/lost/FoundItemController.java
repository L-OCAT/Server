package com.locat.api.domain.lost;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/founds")
public class FoundItemController {

  @GetMapping
  public ResponseEntity<?> getFoundItems() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getFoundItem(@PathVariable final Long id) {
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<?> createFoundItem(@RequestParam MultipartFile foundItemImage, @RequestBody FoundItemRegisterRequest request) {
    return ResponseEntity.ok().build();
  }

}
