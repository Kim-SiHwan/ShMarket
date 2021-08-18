package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "6. Product")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductLikeService likeService;

    @ApiOperation(value = "페이지로 상품 조회", notes = "전체 상품 페이징 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryList", dataType = "List<String>", value = "게시글 카테고리 리스트", required = true),
            @ApiImplicitParam(name = "nickname", dataType = "String", value = "조회 대상 닉네임 ( 없다면 전체 상품 조회 )", required = false),
            @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호 ( 없다면 첫번째 페이지 조회 )", required = false)
    })
    @GetMapping("/list/{page}")
    public ResponseEntity<Result> getProductByPaging(@RequestParam("list") List<String> categories,
                                                     @RequestParam(required = false) String nickname,
                                                     @PathVariable int page) {
        return new ResponseEntity<>(productService.paging(page, categories, nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "상품 단건 조회", notes = "상품의 PK로 상품 단건 조회")
    @ApiImplicitParam(name = "productId", dataType = "Long", value = "조회 대상 PK", required = true)
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return new ResponseEntity(productService.findById(productId), HttpStatus.OK);
    }

    @ApiOperation(value = "닉네임으로 상품 조회", notes = "사용자의 닉네임으로 해당 사용자의 상품 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", dataType = "String", value = "조회 대상 닉네임", required = true),
            @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호", required = true)
    })
    @GetMapping("/my/{page}")
    public ResponseEntity<Result> getAllProductsByNickname(@RequestParam String nickname,
                                                           @PathVariable int page) {
        return new ResponseEntity<>(productService.findAllProductByNickname(nickname, page), HttpStatus.OK);
    }

    @ApiOperation(value = "관심 상품 조회", notes = "본인의 관심 상품 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping("/my/like")
    public ResponseEntity<List<ProductListResponseDto>> getAllLikeProductsByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(productService.findAllLikeProductByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "이미지 조회", notes = "URL로 이미지 조회")
    @ApiImplicitParam(name = "fileName", dataType = "String", value = "이미지의 URL", required = true)
    @GetMapping(value = "/download", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        Resource resource = new FileSystemResource("C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\images\\" + fileName);
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @ApiOperation(value = "상품 생성", notes = "상품 생성")
    @PostMapping
    public void addProduct(@Valid @ModelAttribute ProductRequestDto productRequestDto) {
        productService.addProduct(productRequestDto);
    }

    @ApiOperation(value = "관심상품 등록", notes = "상품 PK로 관심상품 등록")
    @ApiImplicitParam(name = "productId", dataType = "Long", value = "등록 대상의 PK", required = true)
    @PostMapping("/{productId}")
    public ResponseEntity<Boolean> setLike(@PathVariable Long productId) {
        return new ResponseEntity<>(likeService.pushInterest(productId), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "상품 상태 변경", notes = "상품의 상태를 (SALE, RESERVATION, COMPLETE) 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", dataType = "Long", value = "변경 대상의 PK", required = true),
            @ApiImplicitParam(name = "status", dataType = "String", value = "변경할 상태 (SALE, RESERVATION, COMPLETE)", required = true)
    })
    @PatchMapping("/status/{productId}")
    public void setStatus(@PathVariable Long productId,
                          @RequestParam("status") String status) {
        productService.setStatus(productId, status);
    }

    @ApiOperation(value = "상품 정보 수정", notes = "상품 정보 수정")
    @PatchMapping
    public void updateProduct(@Valid @ModelAttribute ProductUpdateRequestDto updateRequestDto) {
        productService.updateProduct(updateRequestDto);
    }

    @ApiOperation(value = "상품 삭제", notes = "상품의 PK로 상품 삭제")
    @ApiImplicitParam(name = "productId", dataType = "Long", value = "삭제 대상 PK", required = true)
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

}
