package me.dio.controller;
import me.dio.controller.dto.ProductDto;
import me.dio.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/product")
@Tag(name = "Products Controller", description = "RESTful API for managing products.")
public record ProductController(ProductService productService) {

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<ProductDto>> findAll() {
        var product = productService.findAll();
        var productsDto = product.stream().map(ProductDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(productsDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Product by ID", description = "Retrieve a specific product based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(new ProductDto(product));
    }

    @PostMapping
    @Operation(summary = "Create a new Product", description = "Create a new Product and return the created user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid product data provided")
    })
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        var product = productService.create(productDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).body(new ProductDto(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update the data of an existing product based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "422", description = "Invalid product data provided")
    })
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto productDto) {
        var user = productService.update(id, productDto.toModel());
        return ResponseEntity.ok(new ProductDto(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete an existing product based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
