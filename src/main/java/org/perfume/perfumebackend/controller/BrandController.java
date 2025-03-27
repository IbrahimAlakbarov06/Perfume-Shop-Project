package org.perfume.perfumebackend.controller;

import org.perfume.perfumebackend.dto.BrandDto;
import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.service.BrandService;
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
@RequestMapping("/api/v2/brands")
public class BrandController {

    private final BrandService brandService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<BrandDto> brands = brandService.findAll(page, size);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Long id) {
        BrandDto brand = brandService.findById(id);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/{id}/perfumes")
    public ResponseEntity<List<PerfumeDto>> getPerfumesByBrand(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PerfumeDto> perfumes = brandService.findPerfumesByBrand(id, page, size);
        return ResponseEntity.ok(perfumes);
    }

    @PostMapping
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandDto brandDto) {
        BrandDto createdBrand = brandService.save(brandDto);
        return new ResponseEntity<>(createdBrand, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> updateBrand(
            @PathVariable Long id,
            @RequestBody BrandDto brandDto
    ) {
        brandDto.setId(id);
        BrandDto updatedBrand = brandService.update(brandDto);
        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-logo/{brandId}")
    public ResponseEntity<String> uploadLogo(
            @PathVariable Long brandId,
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

            String logoUrl = "/api/v2/brands/logos/" + uniqueFilename;
            brandService.updateBrandLogo(brandId, logoUrl);

            return ResponseEntity.ok(logoUrl);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload logo: " + ex.getMessage());
        }
    }

    @GetMapping("/logos/{id}")
    public ResponseEntity<Resource> getBrandLogo(@PathVariable Long id) {
        try {
            BrandDto brand = brandService.findById(id);

            if (brand.getLogoUrl() == null) {
                return ResponseEntity.notFound().build();
            }

            String filename = Paths.get(brand.getLogoUrl()).getFileName().toString();

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