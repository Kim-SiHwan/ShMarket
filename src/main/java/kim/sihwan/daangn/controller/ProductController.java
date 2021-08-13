package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.dto.product.ProductListResponseDto;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.dto.product.ProductResponseDto;
import kim.sihwan.daangn.dto.product.ProductUpdateRequestDto;
import kim.sihwan.daangn.service.product.ProductLikeService;
import kim.sihwan.daangn.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductLikeService likeService;

    @GetMapping("/list/{page}")
    public ResponseEntity<Result> getProductByPaging(@RequestParam("list") List<String> categories,
                                                     @RequestParam(required = false) String nickname,
                                                     @PathVariable int page) {
        return new ResponseEntity<>(productService.paging(page, categories, nickname), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return new ResponseEntity(productService.findById(productId), HttpStatus.OK);
    }

    @GetMapping("/my/{page}")
    public ResponseEntity<Result> getAllProductsByNickname(@RequestParam String nickname,
                                                           @PathVariable int page) {
        return new ResponseEntity<>(productService.findAllProductByNickname(nickname, page), HttpStatus.OK);
    }

    @GetMapping("/my/like")
    public ResponseEntity<List<ProductListResponseDto>> getAllLikeProductsByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(productService.findAllLikeProductByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping(value = "/download", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        Resource resource = new FileSystemResource("C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\images\\" + fileName);
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @PostMapping
    public void addProduct(@Valid @ModelAttribute ProductRequestDto productRequestDto) {
        productService.addProduct(productRequestDto);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Boolean> setLike(@PathVariable Long productId) {
        return new ResponseEntity<>(likeService.pushInterest(productId), HttpStatus.ACCEPTED);
    }

    @PatchMapping("/status/{productId}")
    public void setStatus(@PathVariable Long productId,
                          @RequestParam("status") String status) {
        productService.setStatus(productId, status);
    }

    @PatchMapping
    public void updateProduct(@Valid @ModelAttribute ProductUpdateRequestDto updateRequestDto) {
        productService.updateProduct(updateRequestDto);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

}
