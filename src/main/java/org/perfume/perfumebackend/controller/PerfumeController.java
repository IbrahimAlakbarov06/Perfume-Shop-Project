package org.perfume.perfumebackend.controller;

import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.service.PerfumeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/perfumes")
public class PerfumeController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping
    public ResponseEntity<List<PerfumeDto>> getAllPerfumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PerfumeDto> perfumes = perfumeService.findAll(page, size);
        return ResponseEntity.ok(perfumes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfumeDto> getPerfumeById(@PathVariable Long id) {
        PerfumeDto perfume = perfumeService.findById(id);
        return ResponseEntity.ok(perfume);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PerfumeDto>> searchPerfumes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PerfumeDto> perfumes = perfumeService.searchPerfumes(name, brandName, categoryName, page, size);
        return ResponseEntity.ok(perfumes);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = perfumeService.findAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAllBrands() {
        List<String> brands = perfumeService.findAllBrands();
        return ResponseEntity.ok(brands);
    }

    @PostMapping
    public ResponseEntity<PerfumeDto> createPerfume(@RequestBody PerfumeDto perfumeDto) {
        PerfumeDto createdPerfume = perfumeService.save(perfumeDto);
        return new ResponseEntity<>(createdPerfume, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfumeDto> updatePerfume(
            @PathVariable Long id,
            @RequestBody PerfumeDto perfumeDto
    ) {
        perfumeDto.setId(id);
        PerfumeDto updatedPerfume = perfumeService.update(perfumeDto);
        return ResponseEntity.ok(updatedPerfume);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        perfumeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-image/{perfumeId}")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long perfumeId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = UUID.randomUUID() + fileExtension;

            Path targetLocation = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/api/v2/perfumes/images/" + uniqueFilename;
            perfumeService.updatePerfumeImage(perfumeId, imageUrl);

            return ResponseEntity.ok(imageUrl);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload image: " + ex.getMessage());
        }
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getPerfumeImage(@PathVariable Long id) {
        try {
            PerfumeDto perfume = perfumeService.findById(id);

            if (perfume.getImageUrl() == null) {
                return ResponseEntity.notFound().build();
            }

            String filename = Paths.get(perfume.getImageUrl()).getFileName().toString();

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filename);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (MalformedURLException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
}