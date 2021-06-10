package kim.sihwan.daangn.service.product;

import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductAlbum;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.repository.product.ProductAlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductAlbumService {

    private final ProductAlbumRepository productAlbumRepository;

    private static final String fileUrl = "C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\static\\images\\";
    private static final String saveUrl = "http://localhost:8080/api/product/download?fileName=";
    private String addUrl = "";

    @Transactional
    public Product addProductAlbums(ProductRequestDto productRequestDto) {
        Product product = productRequestDto.toEntity(productRequestDto);
        addImage(product, productRequestDto.getFiles());
        return product;
    }

    @Transactional
    public void appendImages(Product product, List<MultipartFile> files) {
        addImage(product, files);
    }

    @Transactional
    public void addImage(Product product, List<MultipartFile> files) {
        Long tempThumbnailId = 0L;
        try {
            for (MultipartFile file : files) {
                String newFilename = createNewFilename(file.getOriginalFilename());
                addUrl = saveUrl + newFilename;
                File dest = new File(fileUrl + newFilename);
                file.transferTo(dest);

                ProductAlbum productAlbum = ProductAlbum.builder()
                        .filename(file.getOriginalFilename())
                        .url(addUrl)
                        .build();
                productAlbum.addProduct(product);
                tempThumbnailId = productAlbum.getId();
            }
            if (product.getThumbnail().equals(saveUrl + "default.png")) {
                product.addThumbnail(tempThumbnailId, addUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteImages(Product product, List<Long> ids) {
        productAlbumRepository.deleteAllByProductAlbumIds(ids);
        if (product.getProductAlbums().size() == 0) {
            product.removeThumbnail();
            return;
        }
        List<ProductAlbum> list = new ArrayList<>(product.getProductAlbums());
        product.addThumbnail(list.get(0).getId(), list.get(0).getUrl());
    }

    public String createNewFilename(String filename) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + filename;
    }


}
